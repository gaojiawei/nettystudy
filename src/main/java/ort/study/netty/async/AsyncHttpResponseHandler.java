package ort.study.netty.async;

import com.google.common.base.Charsets;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.LastHttpContent;

/**
 * .
 * User: jiawei.gao
 * Date: 14-5-9
 * qunar.com
 */
public class AsyncHttpResponseHandler extends SimpleChannelInboundHandler<HttpObject> {

    StringBuilder stringBuffer = new StringBuilder();
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        HttpObject response =  msg;
        ListenableFutureImpl<String> attachment = ctx.attr(HttpClient.LISTENABLE_FUTURE).get();

        if(!(response instanceof LastHttpContent)){
            if (response instanceof HttpContent){
                //如果是content的话
                ByteBuf content = ((HttpContent) response).content();
                byte[] bytes = new byte[content.readableBytes()];
                content.readBytes(bytes);
                stringBuffer.append(new String(bytes, Charsets.UTF_8));
//            }else{
//                System.out.println("Read Header"+System.currentTimeMillis()+response+"======================");
            }

        }else{
            //http 接受完后 通知
            System.out.println("Thread"+Thread.currentThread().getName()+"HandlerInstance"+this.toString());
            attachment.set(stringBuffer.toString());
            attachment.done();
            stringBuffer = new StringBuilder();
        }


    }


}