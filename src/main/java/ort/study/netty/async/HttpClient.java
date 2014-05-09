package ort.study.netty.async;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;
import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.codec.http.*;

import java.net.InetSocketAddress;
import java.net.URI;
import java.util.Map;
import java.util.concurrent.*;

/**
 * .
 * User: jiawei.gao
 * Date: 14-5-9
 * qunar.com
 */
public class HttpClient {
    private ClientBootstrap bootstrap = null;
    ChannelGroup allChannels = null;
    HttpClient(){
        bootstrap = new ClientBootstrap(
                new NioClientSocketChannelFactory(
                        Executors.newCachedThreadPool(),
                        Executors.newCachedThreadPool()));
        bootstrap.setPipelineFactory(new HttpClientPipelineFactory());
        allChannels = new DefaultChannelGroup();
    }
    public void setOption(String key, Object value) {
        bootstrap.setOption(key, value);
    }
    public <T>  ListenableFuture<T> get(String url) throws Exception{
        return retrieve("GET", url);
    }
    public <T>  ListenableFuture<T> delete(String url) throws Exception{
        return retrieve("DELETE", url);
    }
    public <T>  ListenableFuture<T> post(String url, Map<String, Object> data) throws Exception{
        return retrieve("POST", url, data);
    }
    public <T>  ListenableFuture<T> retrieve(String method, String url) throws Exception{
        return retrieve(method, url, null, null);
    }
    public <T>  ListenableFuture<T> retrieve(String method, String url, Map<String, Object> data ) throws Exception{
        return retrieve(method, url, data, null);
    }
    public <T>  ListenableFuture<T>  retrieve(String method, String url, Map<String, Object>data, Map<String, String> cookie) throws Exception{
        if(url == null) throw new Exception("url is null") ;
        URI uri = new URI(url);
        String scheme = uri.getScheme() == null? "http" : uri.getScheme();
        String host = uri.getHost() == null? "localhost" : uri.getHost();
        if (!scheme.equals("http")) {
            throw new Exception("just support http protocol") ;
        }
        HttpRequest request = new DefaultHttpRequest(
                HttpVersion.HTTP_1_1, HttpMethod.valueOf(method), uri.toASCIIString());
        request.setHeader(HttpHeaders.Names.HOST, host);
        request.setHeader(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
        if(cookie != null){
            CookieEncoder httpCookieEncoder = new CookieEncoder(false);
            for (Map.Entry<String, String> m : cookie.entrySet()) {
                httpCookieEncoder.addCookie(m.getKey(), m.getValue());
                request.setHeader(HttpHeaders.Names.COOKIE, httpCookieEncoder.encode());
            }
        }
        return retrieve(request);
    }
    public <T>  ListenableFuture<T> retrieve(HttpRequest request)throws Exception{
        URI uri = new URI(request.getUri());
        int port = uri.getPort() == -1? 80 : uri.getPort();
        ChannelFuture future = bootstrap.connect(new InetSocketAddress(request.getHeader(HttpHeaders.Names.HOST) , port) );
        ListenableFutureImpl<T> listenableFuture= new ListenableFutureImpl<T>();
        future.addListener(new ConnectOk(request,listenableFuture));
        allChannels.add(future.getChannel());

        return   listenableFuture;
    }
    public void close(){
        allChannels.close().awaitUninterruptibly();
        bootstrap.releaseExternalResources();
    }

}
class ConnectOk implements ChannelFutureListener {
    private HttpRequest request=null;
    private ListenableFutureImpl currnetFuture;
    ConnectOk(HttpRequest req,ListenableFutureImpl future){
        this.request = req;
        this.currnetFuture = future;
    }
    public void operationComplete(ChannelFuture future){
        if (!future.isSuccess()) {
            future.getCause().printStackTrace();
            return;
        }
        Channel channel = future.getChannel();
        System.out.println(channel.getPipeline().getContext(AsyncHttpResponseHandler.class)+Thread.currentThread().getName());
        channel.getPipeline().getContext(AsyncHttpResponseHandler.class).setAttachment(currnetFuture);
        channel.write(request);

    }
}
