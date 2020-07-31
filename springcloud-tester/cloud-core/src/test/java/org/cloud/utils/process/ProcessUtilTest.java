package org.cloud.utils.process;

public class ProcessUtilTest {
//    Logger logger = LoggerFactory.getLogger(ProcessUtilTest.class);
//
//    @Test
//    public void runCablles() {
//
//        List<Callable<Object>> callables = new ArrayList<>();
//
//        for (int i = 0; i < 100; i++) {
//            final int j = i;
//            callables.add(new ProcessCallable<Object>() {
//                @Override
//                public Object process() {
//                    logger.info(j + "号进程" + System.currentTimeMillis());
//                    return j;
//                }
//            });
//        }
//        List<Object> result = ProcessUtil.single().runCablles(callables, 2, 100L);
//
//        logger.info(JSON.toJSONString(result));
//
//    }
//
//    @Test
//    public void submitCablles() {
//        List<Runnable> runnables = new ArrayList<>();
//        for (int i = 0; i < 100; i++) {
//            final int j = i;
//            runnables.add(new ProcessCallable<Object>() {
//                @Override
//                public Object process() {
//                    logger.info(j + "号进程" + System.currentTimeMillis());
//                    return j;
//                }
//            });
//        }
//        ProcessUtil.single().submitCablles(runnables);
//        try {
//            Thread.sleep(10);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }
}