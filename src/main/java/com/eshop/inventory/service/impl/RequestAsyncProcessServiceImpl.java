package com.eshop.inventory.service.impl;

import com.eshop.inventory.model.ProductInventory;
import com.eshop.inventory.request.Request;
import com.eshop.inventory.request.RequestQueue;
import com.eshop.inventory.service.RequestAsyncProcessService;
import org.springframework.stereotype.Service;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * * 请求异步处理的service实现
 *
 * @author: RenLiLi
 * @date: 2021/8/13 22:16
 */
@Service("requestAsyncProcessService")
public class RequestAsyncProcessServiceImpl implements RequestAsyncProcessService {

    @Override
    public void process(Request request) {
        //做请求的路由，根据每个请求的商品ID，路由到对应的内存队列中去
        try {
            ArrayBlockingQueue<Request> routingQueue = getRoutingQueue(request.getProductId());
            routingQueue.put(request);
            System.out.println("查询到的队列："+ routingQueue.size());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取路由到的内存队列
     *
     * @param productId
     * @return
     */
    private ArrayBlockingQueue<Request> getRoutingQueue(Integer productId) {
        RequestQueue requestQueue = RequestQueue.getInstance();
        //先获取productId的hash值
        String key = String.valueOf(productId);
        int h;
        int hash = (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);

        //对hash值取模，将hash值路由到指定的内存队列中，比如内存队列大小是8
        //用内存队列的数量与hash值取模之后，结果一定是在0～7之间
        //所以任何一个商户ID都会被固定路由到同一个内存队列中去的
        int index = (requestQueue.queueSize() - 1) & hash;
        System.out.println("------------日志---------:获取路由到内存队列，队列index=" + index);
        return requestQueue.getQueue(index);
    }
}
