package com.eshop.inventory.thread;

import com.eshop.inventory.request.Request;
import com.eshop.inventory.request.RequestQueue;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * * 请求处理线程池 单例
 *
 * @author: RenLiLi
 * @date: 2021/7/19 13:26
 */
public class RequestProcessorThreadPool {

    /**
     * 实际项目中，线程池的大小以及内存队列的大小都可以做到一个外部的配置文件中，这里简化直接写死
     */
    private ExecutorService threadPool = Executors.newFixedThreadPool(10);


    private RequestProcessorThreadPool() {
        RequestQueue requestQueue = RequestQueue.getInstance();
        for (int i = 0; i < 10; i++) {
            ArrayBlockingQueue<Request> queue = new ArrayBlockingQueue<>(100);
            requestQueue.addQueue(queue);
            threadPool.submit(new RequestProcessorThread(queue));
        }
    }

    private static class Singleton {
        private static RequestProcessorThreadPool instance;

        static {
            instance = new RequestProcessorThreadPool();
        }

        public static RequestProcessorThreadPool getInstance() {
            return instance;
        }

    }

    /**
     * jvm的机制去保证多线程并发安全
     * 内部类的初始化，一定只会发生一次，不管多少个线程并发去初始化
     *
     * @return
     */
    public static RequestProcessorThreadPool getInstance() {
        return Singleton.getInstance();
    }

    /**
     * 初始化的便携方法
     */
    public static void init() {
        getInstance();
    }
}
