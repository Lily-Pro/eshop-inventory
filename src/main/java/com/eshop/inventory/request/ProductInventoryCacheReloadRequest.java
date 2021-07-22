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
public class ProductInventoryCacheReloadRequest implements Request {

    /**
     * 商户库存
     */
    private ProductInventory productInventory;

    private ProductInventoryService productInventoryService;

    public ProductInventoryCacheReloadRequest(ProductInventory productInventory,
                                              ProductInventoryService productInventoryService) {
        this.productInventory = productInventory;
        this.productInventoryService = productInventoryService;
    }

    @Override
    public void process() {
        //从数据库中查询最新的商品库存数量

        ProductInventory search = productInventoryService.findProductInventory(this.productInventory.getProductId());
        //将最新的商品库存数量刷新到redis缓存中
        productInventoryService.updateProductInventory(this.productInventory);
    }

}
