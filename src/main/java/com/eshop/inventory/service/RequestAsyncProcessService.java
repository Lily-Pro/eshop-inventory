package com.eshop.inventory.service;

import com.eshop.inventory.request.Request;

/**
 * * 请求异步执行的service
 *
 * @author: RenLiLi
 * @date: 2021/8/13 17:44
 */
public interface RequestAsyncProcessService {

    void process(Request request);
}
