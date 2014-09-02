package ort.study.netty.async;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpResponseDecoder;

/**
 * . User: jiawei.gao Date: 14-5-9 qunar.com
 */
public class HttpClientChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        System.out.println("init");
        //read fist decode response
        pipeline.addLast("decoder", new HttpResponseDecoder());
        //async handler
        pipeline.addLast("handler", new AsyncHttpResponseHandler());
        //write first encode request
        pipeline.addLast("encoder", new HttpRequestEncoder());
    }
}