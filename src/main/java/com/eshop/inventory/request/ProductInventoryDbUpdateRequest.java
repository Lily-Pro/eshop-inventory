package com.eshop.inventory.request;

import com.eshop.inventory.model.ProductInventory;
import com.eshop.inventory.service.ProductInventoryService;

/**
 * * 比如说一个商户发生了交易，那么就要修改这个商户对应的缓存
 * 此时就会发送请求过来，要求修改库存，那么这个可能就是所谓的 data update request，数据更新请求
 * <p>
 * cache aside pattern
 * （1） 删除缓存
 * （2） 更新数据库
 *
 * @author: RenLiLi
 * @date: 2021/7/21 13:31
 */
public class ProductInventoryDbUpdateRequest implements Request {

    /**
     * 商户库存
     */
    private ProductInventory productInventory;

    private ProductInventoryService productInventoryService;

    public ProductInventoryDbUpdateRequest(ProductInventory productInventory,
                                           ProductInventoryService productInventoryService) {
        this.productInventory = productInventory;
        this.productInventoryService = productInventoryService;
    }

    @Override
    public void process() {
        System.out.println("------------日志---------:商户句更新请求开始执行， productId=" + productInventory.getProductId());
        //删除redis中的缓存
        productInventoryService.removeProductInventoryCache(productInventory);
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //修改数据库中的库存
        productInventoryService.updateProductInventory(productInventory);

    }

    @Override
    public Integer getProductId() {
        return productInventory.getProductId();
    }

    @Override
    public boolean isForceRefresh() {
        return false;
    }

}
