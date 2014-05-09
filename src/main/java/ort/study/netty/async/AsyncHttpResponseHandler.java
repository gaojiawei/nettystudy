package ort.study.netty.async;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.*;
import org.jboss.netty.handler.codec.http.HttpResponse;

/**
 * .
 * User: jiawei.gao
 * Date: 14-5-9
 * qunar.com
 */
public class AsyncHttpResponseHandler extends SimpleChannelUpstreamHandler {
    ChannelBuffer content;
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
        e.getCause().printStackTrace();
        Channel ch = e.getChannel();
        ch.close();
    }
    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e){
        HttpResponse response = (HttpResponse) e.getMessage();
        ListenableFutureImpl attachment = (ListenableFutureImpl) ctx.getAttachment();
        attachment.set(response);
        attachment.done();

    }

}