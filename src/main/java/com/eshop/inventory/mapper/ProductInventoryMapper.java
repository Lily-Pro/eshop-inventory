package com.eshop.inventory.mapper;

import com.eshop.inventory.model.ProductInventory;
import org.apache.ibatis.annotations.Param;

/**
 * * 库存数量Dao
 *
 * @author: RenLiLi
 * @date: 2021/7/21 13:44
 */
public interface ProductInventoryMapper {
    /**
     * 更新数据库的库存数量
     *
     * @param productInventory 商品库存
     */
    void updateProductInventory(ProductInventory productInventory);

    /**
     * 根据商户ID查询商户库存信息
     *
     * @param productId 商户ID
     * @return
     */
    ProductInventory findProductInventory(@Param("productId") Integer productId);
}
