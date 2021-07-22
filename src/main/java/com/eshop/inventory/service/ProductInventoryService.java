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
     * 修改商户库存
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

    void refreshProductInventoryCache();
}
