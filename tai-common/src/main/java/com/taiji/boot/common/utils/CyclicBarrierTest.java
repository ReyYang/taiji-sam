package com.taiji.boot.common.utils;

import com.google.common.collect.Lists;
import com.google.common.collect.Queues;
import com.google.common.collect.Sets;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * Demo CyclicBarrierTest
 *
 * @author ydy
 * @date 2020/1/13 14:14
 */
public class CyclicBarrierTest {

    private static List<Integer> list = Lists.newArrayList();

    private static ConcurrentLinkedQueue<List<String>> resultList = Queues.newConcurrentLinkedQueue();

    static {
        for (int i = 0; i < 200; i++) {
            list.add(i);
        }
    }

    private static String convert(Integer item) {
        try {
            // 模拟转换耗时
            TimeUnit.MILLISECONDS.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return item + "->=" + item;
    }

    private static int calculateThreadCount(int total, int batch) {
        BigDecimal totalDecimal = BigDecimal.valueOf(total);
        BigDecimal batchDecimal = BigDecimal.valueOf(batch);
        BigDecimal result = totalDecimal.divide(batchDecimal, BigDecimal.ROUND_CEILING);
        return result.intValue();
    }

    public static void testMain() {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 5, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());
//        CyclicBarrier barrier = new CyclicBarrier(5, () -> {
//            System.out.println("线程全部结束");
//            List<String> rs = Lists.newArrayList();
//            for (int i = 0; i < array.length; i++) {
//                rs.addAll(Objects.requireNonNull(array[i]));
//            }
//            System.out.println(rs);
//            executor.shutdown();
//        });
        int batch = 45;
        int thCount = calculateThreadCount(list.size(), batch);
        List<String>[] array = new List[thCount];
        CyclicBarrier barrier = new CyclicBarrier(thCount, () -> {
            System.out.println("线程全部结束");
            List<String> rs = Lists.newArrayList();
            for (int i = 0; i < array.length; i++) {
                rs.addAll(Objects.requireNonNull(array[i]));
            }
            System.out.println(rs);
            executor.shutdown();
        });
        for (int i = 0; i < thCount; i++) {
            if ((batch * i) > list.size()) {
                break;
            }
            List<Integer> subList = list.subList(batch * i, Math.min(batch * (i + 1), list.size()));
            System.out.println("线程开始" + subList.toString());
            int finalI = i;
            executor.execute(() -> {
                List<String> stringList = subList.stream().map(CyclicBarrierTest::convert).collect(Collectors.toList());
                array[finalI] = stringList;
                System.out.println("---> " + stringList);
                try {
                    barrier.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    public static void main(String[] args) {
//        int i = calculateThreadCount(200, 33);
//        System.out.println(i);
//        testMain();
        List<Integer> collect = list.stream().filter(o -> !(o > 40)).collect(Collectors.toList());
        Set<String> strings = Sets.newHashSet();
        strings.add("111");
        strings.add("111");
        System.out.println(strings);
    }


}
