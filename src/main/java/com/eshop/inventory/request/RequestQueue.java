package com.eshop.inventory.request;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * * 请求的内存队列
 *
 * @author: RenLiLi
 * @date: 2021/7/19 14:40
 */
public class RequestQueue {
    /**
     * 内存队列 实际项目中，线程池的大小以及内存队列的大小都可以做到一个外部的配置文件中，这里简化直接写死
     */
    private List<ArrayBlockingQueue<Request>> queues = new ArrayList<>();

    /**
     * 标识为map
     */
    private Map<Integer, Boolean> flagMap = new ConcurrentHashMap<>();

    private RequestQueue() {
    }

    private static class Singleton {
        private static RequestQueue instance;

        static {
            instance = new RequestQueue();
        }

        public static RequestQueue getInstance() {
            return instance;
        }

    }

    /**
     * jvm的机制去保证多线程并发安全
     * 内部类的初始化，一定只会发生一次，不管多少个线程并发去初始化
     *
     * @return
     */
    public static RequestQueue getInstance() {
        return Singleton.getInstance();
    }

    public void addQueue(ArrayBlockingQueue<Request> queue) {
        this.queues.add(queue);
    }

    /**
     * 获取队列的大小
     *
     * @return
     */
    public Integer queueSize() {
        return queues.size();
    }

    /**
     * 根据索引获取队列
     *
     * @param index
     * @return
     */
    public ArrayBlockingQueue<Request> getQueue(int index) {
        return queues.get(index);
    }

    public Map<Integer, Boolean> getFlagMap() {
        return flagMap;
    }
}
