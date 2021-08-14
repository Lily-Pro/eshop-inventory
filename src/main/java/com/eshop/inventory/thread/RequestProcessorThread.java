package com.eshop.inventory.thread;

import com.alibaba.fastjson.JSON;
import com.eshop.inventory.request.ProductInventoryCacheRefreshRequest;
import com.eshop.inventory.request.ProductInventoryDbUpdateRequest;
import com.eshop.inventory.request.Request;
import com.eshop.inventory.request.RequestQueue;

import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;

/**
 * * 执行请求的工作线程
 *
 * @author: RenLiLi
 * @date: 2021/7/19 14:28
 */
public class RequestProcessorThread implements Callable<Boolean> {

    /**
     * 自己监控的内存队列
     */
    private ArrayBlockingQueue<Request> queue;

    public RequestProcessorThread(ArrayBlockingQueue<Request> queue) {
        this.queue = queue;
    }

    /**
     * Computes a result, or throws an exception if unable to do so.
     *
     * @return computed result
     * @throws Exception if unable to compute a result
     */
    @Override
    public Boolean call() {
        try {
            while (true) {
                //阻塞队列 是线程安全的
                Request request = queue.take();
                System.out.println("------------日志---------:从队列中消费数据， request=" + JSON.toJSONString(request));
                if (!request.isForceRefresh()) {
                    //先做读请求的去重
                    RequestQueue requestQueue = RequestQueue.getInstance();
                    Map<Integer, Boolean> flagMap = requestQueue.getFlagMap();
                    if (request instanceof ProductInventoryDbUpdateRequest) {
                        //如果是一个更新数据库请求，那么将productID对应的标识设置为true
                        flagMap.put(request.getProductId(), true);
                    } else if (request instanceof ProductInventoryCacheRefreshRequest) {
                        //如果是缓存刷新的请求，那么就先判断，如果标识不为空，而且是true，就说明之前有一个商户更新的操作
                        Boolean flag = flagMap.get(request.getProductId());
                        //如果flag是null
                        if (flag == null) {
                            flagMap.put(request.getProductId(), false);
                        }
                        if (flag != null && flag) {
                            flagMap.put(request.getProductId(), false);
                        }
                        //如果是缓存刷新的请求，而且发现标识不为空，并且是false，说明前面已经有一个数据库更新请求+一个缓存刷新请求了
                        if (flag != null && !flag) {
                            return true;
                        }
                    }
                }
                request.process();
                //假如说，执行完了一个读请求之后，假设数据已经刷入到内存中
                //但是后面可能redis中的内存满了，被自动清理掉
                //如果说数据从redis中被自动清理掉了以后
                //然后后面又来了一个读请求，此时如果读出来，发现标识为是false
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
}
