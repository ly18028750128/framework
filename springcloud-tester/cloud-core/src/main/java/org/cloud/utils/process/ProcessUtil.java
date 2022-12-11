package org.cloud.utils.process;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.cloud.utils.CollectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ProcessUtil {

    Logger logger = LoggerFactory.getLogger(ProcessUtil.class);

    private ProcessUtil() {
    }

    private static class Handler {

        private Handler() {
        }

        private static ProcessUtil handler = new ProcessUtil();
    }

    public static ProcessUtil single() {
        return Handler.handler;
    }

    public <V> Future<V> runCallable(Callable<V> callable) {
        final ExecutorService fixedThreadPool = Executors.newFixedThreadPool(1);
        try {
            return fixedThreadPool.submit(callable);
        } finally {
            fixedThreadPool.shutdown();
        }
    }

    public <E> List<E> runCallables(List<Callable<E>> callables, int poolSize, long timeout) {
        List<E> listResult = new ArrayList<E>();
        List<List<Callable<E>>> spitCallableList = CollectionUtil.single().spitList(callables, poolSize);
        for (List<Callable<E>> runCallableList : spitCallableList) {
            final ExecutorService fixedThreadPool = Executors.newFixedThreadPool(runCallableList.size());
            try {
                List<Future<E>> futureResuts = fixedThreadPool.invokeAll(runCallableList, timeout, TimeUnit.SECONDS);
                for (Future<E> future : futureResuts) {
                    try {
                        listResult.add(future.get());
                    } catch (Exception e) {
                        listResult.add(null);
                        logger.error("future.get() err:", e.getMessage());
                    }
                }
            } catch (InterruptedException e) {
                logger.error(e.getMessage(), e);
            } finally {
                fixedThreadPool.shutdown();
            }
        }
        return listResult;
    }

    public <E> List<E> runCallables(List<Callable<E>> callables, int poolSize) {
        return this.runCallables(callables, poolSize, 60L);
    }

    public <E> List<E> runCallables(List<Callable<E>> callables) {
        return this.runCallables(callables, 20, 60L);
    }

    /**
     *
     */
    public void submitRunnable(List<Runnable> runnableList) {
        for (Runnable runnable : runnableList) {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            try {
                executor.execute(runnable);
            } finally {
                executor.shutdown();
            }
        }
    }

    public void submitRunnable(Runnable runable) {
        List<Runnable> runables = new ArrayList<>();
        runables.add(runable);
        this.submitRunnable(runables);
    }

    /**
     * 请使用runCallables
     */
    @Deprecated
    public <E> List<E> runCablles(List<Callable<E>> callables, int poolSize, long timeout) {
        return runCallables(callables, poolSize, timeout);
    }

    /**
     * 请使用runCallables
     */
    @Deprecated
    public <E> List<E> runCablles(List<Callable<E>> callables, int poolSize) {
        return this.runCallables(callables, poolSize, 60L);
    }

    /**
     * 请使用runCallables
     */
    @Deprecated
    public <E> List<E> runCablles(List<Callable<E>> callables) {
        return this.runCablles(callables, 10);
    }


}
