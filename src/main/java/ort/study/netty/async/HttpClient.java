package ort.study.netty.async;

import com.google.common.util.concurrent.ListenableFuture;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.util.AttributeKey;

import java.net.InetSocketAddress;
import java.net.URI;
import java.util.Map;

/**
 * . User: jiawei.gao Date: 14-5-9 qunar.com
 */
public class HttpClient {
    private Bootstrap bootstrap = null;
    EventLoopGroup workerGroup = null;
    public static final AttributeKey<ListenableFutureImpl>  LISTENABLE_FUTURE= AttributeKey.valueOf("MyListenableFuture");

    HttpClient() {
        bootstrap = new Bootstrap();
        workerGroup = new NioEventLoopGroup();
        bootstrap.group(workerGroup).channel(NioSocketChannel.class);
        // NioSocketChannel is being used to create a client-side Channel.
        // Note that we do not use childOption() here unlike we did with
        // ServerBootstrap because the client-side SocketChannel does not have a parent.
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
        bootstrap.handler(new HttpClientChannelInitializer());
    }

    public <T> ListenableFuture<T> get(String url) throws Exception {
        return retrieve("GET", url);
    }

    public <T> ListenableFuture<T> delete(String url) throws Exception {
        return retrieve("DELETE", url);
    }

    public <T> ListenableFuture<T> post(String url, Map<String, Object> data) throws Exception {
        return retrieve("POST", url, data);
    }

    public <T> ListenableFuture<T> retrieve(String method, String url) throws Exception {
        return retrieve(method, url, null, null);
    }

    public <T> ListenableFuture<T> retrieve(String method, String url, Map<String, Object> data) throws Exception {
        return retrieve(method, url, data, null);
    }

    public <T> ListenableFuture<T> retrieve(String method, String url, Map<String, Object> data,
            Map<String, String> cookie) throws Exception {
        if (url == null)
            throw new Exception("url is null");
        URI uri = new URI(url);
        String scheme = uri.getScheme() == null ? "http" : uri.getScheme();
        String host = uri.getHost() == null ? "localhost" : uri.getHost();
        if (!scheme.equals("http")) {
            throw new Exception("just support http protocol");
        }
        HttpRequest request = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.valueOf(method),
                uri.toASCIIString());
        request.headers().add(HttpHeaders.Names.HOST, host);
        request.headers().add(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
        return retrieve(request);
    }


    public <T> ListenableFuture<T> retrieve(HttpRequest request) throws Exception {
        URI uri = new URI(request.getUri());
        int port = uri.getPort() == -1 ? 80 : uri.getPort();
        ChannelFuture future = bootstrap.connect(new InetSocketAddress(request.headers().get(HttpHeaders.Names.HOST),
                port));
        ListenableFutureImpl<T> listenableFuture = new ListenableFutureImpl<T>();
        future.addListener(new ConnectOk(request, listenableFuture));

        return listenableFuture;
    }
}

class ConnectOk implements ChannelFutureListener {
    private HttpRequest request = null;
    private ListenableFutureImpl currnetFuture;

    ConnectOk(HttpRequest req, ListenableFutureImpl future) {
        this.request = req;
        this.currnetFuture = future;
    }

    public void operationComplete(ChannelFuture future) throws InterruptedException {
        if (!future.isSuccess()) {

            return;
        }
        Channel channel = future.channel();
        System.out.println(channel.pipeline().context(AsyncHttpResponseHandler.class)
                + Thread.currentThread().getName());
        channel.pipeline().context(AsyncHttpResponseHandler.class).attr(HttpClient.LISTENABLE_FUTURE)
                .set(currnetFuture);
        channel.writeAndFlush(request);

    }
}
