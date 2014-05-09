package ort.study.netty.async;

import com.google.common.util.concurrent.ExecutionList;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * .
 * User: jiawei.gao
 * Date: 14-5-9
 * qunar.com
 */
public class ListenableFutureImpl<V> implements ListenableFuture<V> {
    private V object;
    private final ExecutionList executionList = new ExecutionList();
    @Override
    public void addListener(Runnable listener, Executor executor) {
        executionList.add(listener,executor);
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;  //TODO
    }

    @Override
    public boolean isCancelled() {
        return false;  //TODO
    }

    @Override
    public boolean isDone() {
        return false;  //TODO
    }

    @Override
    public V get() throws InterruptedException, ExecutionException {
        return object;
    }

    public void done(){
        executionList.execute();
    }

    @Override
    public V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return null;  //TODO
    }

    public void set(V object) {
        this.object = object;
    }
}
