package ort.study.java;

import com.sun.org.apache.xpath.internal.functions.FuncTrue;

import java.util.concurrent.*;

/**
 * .
 * User: jiawei.gao
 * Date: 14-4-29
 * qunar.com
 */
class ForkJoinTest extends RecursiveTask<Integer> {
    static final int threshold = 13;
    volatile int number; // arg/result

    ForkJoinTest(int n) {
        number = n;
    }

    int getAnswer() {
        if (!isDone())
            throw new IllegalStateException();
        return number;
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        long begin =System.nanoTime();
        int i = new ForkJoinTest(40).seqFib(42);
        System.out.println(System.nanoTime()-begin);

        ForkJoinTask fib = new ForkJoinTest(42);
        ForkJoinPool fp = new ForkJoinPool();
        begin =System.nanoTime();
        Future submit = fp.submit(fib);

        System.out.println(submit.get());
        System.out.println(System.nanoTime()-begin);
    }

    int seqFib(int n) {
        if (n <= 1) return n;
        else return seqFib(n-1) + seqFib(n  -2);
    }

    @Override
    protected Integer compute() {
        int n = number;
        if (n <= threshold) {
            number = seqFib(n);
            return number;
        }

        else {
            ForkJoinTest f1 = new ForkJoinTest(n - 1);
            ForkJoinTest f2 = new ForkJoinTest(n - 2);
            f1.fork();
            f2.fork();
            return f1.join() + f2.join();
        }
    }
}
