package org.cloud.utils.process;

import org.cloud.utils.CollectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

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

    public <E> List<E> runCablles(List<Callable<E>> callables, int poolSize, long timeout) {
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

    public <E> List<E> runCablles(List<Callable<E>> callables, int poolSize) {
        return this.runCablles(callables, poolSize, 60L);
    }

    public <E> List<E> runCablles(List<Callable<E>> callables) {
        return this.runCablles(callables, 10);
    }

    /**
     *
     */
    public void submitRunnable(List<Runnable> runables) {
        for (Runnable runnable : runables) {
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
}
