package ort.study.netty.async;

import com.google.common.util.concurrent.ListenableFuture;
import org.jboss.netty.channel.ChannelPipeline;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

/**
 * . User: jiawei.gao Date: 14-3-9 qunar.com
 */
public class NettyAysncHttpExample {

    public static void main(String[] args) throws Exception {
        HttpClient hc = new HttpClient();
        ListenableFuture<Object> objectListenableFuture = hc.get("http://www.baidu.com");
        objectListenableFuture.addListener(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println(objectListenableFuture.get());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }, Executors.newSingleThreadExecutor());
        ListenableFuture<Object> objectListenableFuture1 = hc.get("http://www.google.com");
        objectListenableFuture.addListener(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println(objectListenableFuture.get());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }, Executors.newSingleThreadExecutor());
    }

}
