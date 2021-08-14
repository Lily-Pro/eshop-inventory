package com.eshop.inventory.request;

/**
 * * 请求接口
 *
 * @author: RenLiLi
 * @date: 2021/7/19 14:24
 */
public interface Request {

    void process();

    Integer getProductId();

    /**
     * 是否强制刷新读缓存
     *
     * @return
     */
    boolean isForceRefresh();
}
