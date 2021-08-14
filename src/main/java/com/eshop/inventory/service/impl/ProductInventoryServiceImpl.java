package com.eshop.inventory.service.impl;

import com.eshop.inventory.dao.RedisDAO;
import com.eshop.inventory.mapper.ProductInventoryMapper;
import com.eshop.inventory.model.ProductInventory;
import com.eshop.inventory.service.ProductInventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * * 商户库存service实现类
 *
 * @author: RenLiLi
 * @date: 2021/7/22 13:32
 */
@Service("productInventoryService")
public class ProductInventoryServiceImpl implements ProductInventoryService {

    @Autowired
    ProductInventoryMapper productInventoryMapper;

    @Resource
    RedisDAO redisDAO;

    //private ProductInventory productInventory;

    @Override
    public void updateProductInventory(ProductInventory productInventory) {
        productInventoryMapper.updateProductInventory(productInventory);
    }

    /**
     * 删除缓存商品库存
     *
     * @param productInventory
     */
    @Override
    public void removeProductInventoryCache(ProductInventory productInventory) {
        //this.productInventory = productInventory;
        String key = "product:inventory:" + productInventory.getProductId();
        redisDAO.delete(key);
    }

    /**
     * 根据商户ID 查询产品库存信息
     *
     * @param productId 商户ID
     * @return
     */
    @Override
    public ProductInventory findProductInventory(Integer productId) {
        return productInventoryMapper.findProductInventory(productId);
    }

    /**
     * 设置商户库存的缓存
     *
     * @param productInventory
     */
    @Override
    public void setProductInventoryCache(ProductInventory productInventory) {
        String key = "product:inventory:" + productInventory.getProductId();
        redisDAO.set(key, String.valueOf(productInventory.getInventoryCnt()));
    }

    /**
     * 获取商品库存中的缓存
     *
     * @param productId
     * @return
     */
    @Override
    public ProductInventory getProductInventoryCache(Integer productId) {
        Long inventoryCnt = 0L;
        String key = "product:inventory:" + productId;
        String result = redisDAO.get(key);
        if (result != null && !"".equals(result)) {
            try {
                inventoryCnt = Long.valueOf(result);
                return new ProductInventory(productId, inventoryCnt);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
