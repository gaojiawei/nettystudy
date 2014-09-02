package ort.study.netty.async;

import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

/**
 * . User: jiawei.gao Date: 14-3-9 qunar.com
 */
public class NettyAysncHttpExample {

    public static void main(String[] args) throws Exception {
        HttpClient hc = new HttpClient();
        ListenableFuture<String> objectListenableFuture = hc.get("http://www.qunar.com");
        objectListenableFuture.addListener(() -> {
//            System.out.println("baidu.com===================");
            try {
                System.out.println("baidu"+objectListenableFuture.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
//            System.out.println("baidu"+System.currentTimeMillis());
        }, Executors.newSingleThreadExecutor());
//        ListenableFuture<String> objectListenableFuture1 = hc.get("http://www.qunar.com");
//        objectListenableFuture.addListener(() -> {
//            try {
//                System.out.println("google.com===================");
//                System.out.println("google"+objectListenableFuture1.get());
//                System.out.println("google"+System.currentTimeMillis());
//            } catch (InterruptedException | ExecutionException e) {
//                e.printStackTrace();
//            }
//        }, Executors.newSingleThreadExecutor());
//
//        new Thread(()->{
//            ListenableFuture<String> objectListenableFuture2 = null;
//            try {
//                objectListenableFuture2 = hc.get("http://www.baidu.com");
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            objectListenableFuture2.addListener(() -> {
//                //            System.out.println("baidu.com===================");
//                //            try {
//                //                System.out.println("baidu"+objectListenableFuture.get());
//                //            } catch (InterruptedException | ExecutionException e) {
//                //                e.printStackTrace();
//                //            }
//                //            System.out.println("baidu"+System.currentTimeMillis());
//            }, Executors.newSingleThreadExecutor());
//        }).start();
//
//        new Thread(()->{
//
//            ListenableFuture<String> objectListenableFuture3 = null;
//            try {
//                objectListenableFuture3 = hc.get("http://www.qunar.com");
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            objectListenableFuture3.addListener(() -> {
//                //            try {
//                //                System.out.println("google.com===================");
//                //                System.out.println("google"+objectListenableFuture1.get());
//                //                System.out.println("google"+System.currentTimeMillis());
//                //            } catch (InterruptedException | ExecutionException e) {
//                //                e.printStackTrace();
//                //            }
//            }, Executors.newSingleThreadExecutor());
//        }).start();
        System.out.println("Main"+System.currentTimeMillis());
    }

}
