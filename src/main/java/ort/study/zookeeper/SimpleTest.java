package ort.study.zookeeper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.LongAdder;

/**
 * .
 * User: jiawei.gao
 * Date: 14-4-22
 * qunar.com
 */
public class SimpleTest {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(1000);
        LongAdder longAdder = new  LongAdder();
        for (int i = 0; i < 1000000; i++) {
            executorService.submit(()->{
        longAdder.add(1);


            });
        }
        System.out.println(longAdder) ;


    }
}
