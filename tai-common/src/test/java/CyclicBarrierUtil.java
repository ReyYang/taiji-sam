//package com.cldev.search.cldevsearch.util;
//
//import com.google.common.util.concurrent.ThreadFactoryBuilder;
//import org.springframework.util.ObjectUtils;
//
//import java.util.concurrent.*;
//
///**
// * Copyright © 2018 eSunny Info. Developer Stu. All rights reserved.
// * <p>
// * code is far away from bug with the animal protecting
// * <p>
// * ┏┓　　　┏┓
// * ┏┛┻━━━┛┻┓
// * ┃　　　　　　　┃
// * ┃　　　━　　　┃
// * ┃　┳┛　┗┳　┃
// * ┃　　　　　　　┃
// * ┃　　　┻　　　┃
// * ┃　　　　　　　┃
// * ┗━┓　　　┏━┛
// * 　　┃　　　┃神兽保佑
// * 　　┃　　　┃代码无BUG！
// * 　　┃　　　┗━━━┓
// * 　　┃　　　　　　　┣┓
// * 　　┃　　　　　　　┏┛
// * 　　┗┓┓┏━┳┓┏┛
// * 　　　┃┫┫　┃┫┫
// * 　　　┗┻┛　┗┻┛
// *
// * @author zpx
// * Build File @date: 2020/1/9 15:20
// * @version 1.0
// * @description
// */
//public class CyclicBarrierUtil {
//
//    /**
//     * 定义线程池，规定核心线程数以及最大线程数
//     */
//    private ExecutorService executorService;
//
//    /**
//     * 规定线程栅栏所管理的线程数个数
//     */
//    private int operationThreadSize;
//
//    /**
//     * 声明线程栅栏对象
//     */
//    private CyclicBarrier cyclicBarrier;
//
//    /**
//     * 执行线程栅栏的核心对象
//     */
//    private Runnable barrierAction;
//
//    public static CyclicBarrierUtil buildBarrier(Runnable barrierAction, String threadNameFormat,
//                                                int corePoolSize, int maximumPoolSize, long keepAliveTime) {
//        return new CyclicBarrierUtil(barrierAction, threadNameFormat, corePoolSize, maximumPoolSize, keepAliveTime);
//    }
//
//    private CyclicBarrierUtil(Runnable barrierAction, String threadNameFormat,
//                             int corePoolSize, int maximumPoolSize, long keepAliveTime) {
//        this.barrierAction = barrierAction;
//        this.executorService = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime,
//                TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(),
//                new ThreadFactoryBuilder().setNameFormat(threadNameFormat).build());
//    }
//
//    public void createBarrier(int operationThreadSize) {
//        this.operationThreadSize = operationThreadSize;
//        this.buildCyclicBarrier(this.barrierAction);
//    }
//
//    private void buildCyclicBarrier(Runnable barrierAction) {
//        if (!ObjectUtils.isEmpty(barrierAction)) {
//            this.cyclicBarrier = new CyclicBarrier(this.operationThreadSize, barrierAction);
//            return;
//        }
//        throw new RuntimeException("Runnable object must not be null");
//    }
//
//    public void barrierOperationThreadRun(CommonCallBack callBack) {
//        this.barrierCoreProcess(callBack);
//    }
//
//    public void barrierMainThreadWait() {
//        try {
//            this.cyclicBarrier.await();
//        } catch (InterruptedException | BrokenBarrierException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void barrierCoreProcess(CommonCallBack callBack) {
//        this.executorService.execute(() -> {
//            try {
//                callBack.run();
//                this.cyclicBarrier.await();
//            } catch (InterruptedException | BrokenBarrierException e) {
//                e.printStackTrace();
//            }
//        });
//    }
//
//}
