package com.eshop.inventory.service;

import com.eshop.inventory.model.ProductInventory;

/**
 * * 商户库存service接口
 *
 * @author: RenLiLi
 * @date: 2021/7/22 13:32
 */
public interface ProductInventoryService {

    /**
     * 更新商户库存
     *
     * @param productInventory 商户库存
     */
    void updateProductInventory(ProductInventory productInventory);

    /**
     * 删除缓存商品库存
     *
     * @param productInventory 产品库存
     */
    void removeProductInventoryCache(ProductInventory productInventory);

    /**
     * 根据商户ID 查询产品库存信息
     *
     * @param productId 商户ID
     * @return
     */
    ProductInventory findProductInventory(Integer productId);

    /**
     * 设置商户库存的缓存
     *
     * @param productInventory
     */
    void setProductInventoryCache(ProductInventory productInventory);

    /**
     * 获取商品库存中的缓存
     *
     * @param productId
     * @return
     */
    ProductInventory getProductInventoryCache(Integer productId);
}
