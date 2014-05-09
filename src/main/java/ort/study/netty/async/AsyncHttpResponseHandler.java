package ort.study.netty.async;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpObject;
import io.netty.util.AttributeKey;

/**
 * .
 * User: jiawei.gao
 * Date: 14-5-9
 * qunar.com
 */
public class AsyncHttpResponseHandler extends SimpleChannelInboundHandler<HttpObject> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        HttpObject response =  msg;
        ListenableFutureImpl attachment = ctx.attr(HttpClient.LISTENABLE_FUTURE).get();
        attachment.set(response);
        attachment.done();
    }
}