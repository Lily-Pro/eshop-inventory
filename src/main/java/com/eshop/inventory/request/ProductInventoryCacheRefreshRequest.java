package com.eshop.inventory.request;

import com.eshop.inventory.model.ProductInventory;
import com.eshop.inventory.service.ProductInventoryService;

/**
 * * 重新加载商户库存的缓存
 * （1） 删除缓存
 * （2） 更新数据库
 *
 * @author: RenLiLi
 * @date: 2021/7/21 13:31
 */
public class ProductInventoryCacheRefreshRequest implements Request {

    /**
     * 商品ID
     */
    private Integer productId;

    /**
     * 商品库存service
     */
    private ProductInventoryService productInventoryService;

    private boolean isForceRefresh;

    public ProductInventoryCacheRefreshRequest(Integer productId, ProductInventoryService productInventoryService, boolean isForceRefresh) {
        this.productId = productId;
        this.productInventoryService = productInventoryService;
        this.isForceRefresh = isForceRefresh;
    }

    @Override
    public void process() {
        System.out.println("------------日志---------:商户库存查询请求开始执行， productId=" + productId);
        //从数据库中查询最新的商品库存数量
        ProductInventory productInventory = productInventoryService.findProductInventory(productId);
        //将最新的商品库存数量刷新到redis缓存中
        productInventoryService.setProductInventoryCache(productInventory);
    }

    @Override
    public Integer getProductId() {
        return productId;
    }

    @Override
    public boolean isForceRefresh() {
        return isForceRefresh;
    }


}
