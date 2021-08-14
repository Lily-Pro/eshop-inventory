package com.eshop.inventory.controller;

import com.eshop.inventory.model.ProductInventory;
import com.eshop.inventory.request.ProductInventoryCacheRefreshRequest;
import com.eshop.inventory.request.ProductInventoryDbUpdateRequest;
import com.eshop.inventory.request.Request;
import com.eshop.inventory.service.ProductInventoryService;
import com.eshop.inventory.service.RequestAsyncProcessService;
import com.eshop.inventory.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * * 商品库存controller
 *
 * @author: RenLiLi
 * @date: 2021/8/13 22:48
 */
@Controller
public class ProductInventoryController {

    @Autowired
    ProductInventoryService productInventoryService;
    @Autowired
    RequestAsyncProcessService requestAsyncProcessService;

    /**
     * 修改商户库存
     *
     * @param productInventory
     * @return
     */
    @RequestMapping("/updateProductInventory")
    @ResponseBody
    public Response updateProductInventory(ProductInventory productInventory) {
        Response response = null;
        try {
            System.out.println("------------日志---------:接收到修改商户库存的请求， productId=" + productInventory.getProductId());
            Request request = new ProductInventoryDbUpdateRequest(productInventory, productInventoryService);
            requestAsyncProcessService.process(request);
            response = new Response(Response.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            response = new Response(Response.FAILURE);
        }
        return response;
    }

    /**
     * 获取商户库存
     *
     * @param productId
     * @return
     */
    @RequestMapping("/getProductInventory")
    @ResponseBody
    public ProductInventory getProductInventory(Integer productId) {
        ProductInventory productInventory = null;
        try {
            System.out.println("------------日志---------:接收到在缓存中获取商户库存的请求， productId=" + productId);
            Request request = new ProductInventoryCacheRefreshRequest(productId, productInventoryService, false);
            requestAsyncProcessService.process(request);
            //将请求扔给service异步去处理以后，就需要while（true）一会儿，在这里hang猪住
            //去尝试等待前面有商品库存更新的操作，同时缓存刷新的操作，将最新的数据刷新到缓存中
            long startTime = System.currentTimeMillis();
            long endTime = 0L;
            long waitTime = 0L;

            //等待超过200ms 没有从缓存中获取商户库存信息
            while (true) {
                if (waitTime > 20000) {
                    break;
                }
                //尝试从redis缓存中读取一次商品库存的缓存数据
                productInventory = productInventoryService.getProductInventoryCache(productId);
                //如果读取到了结果，就返回
                if (productInventory != null) {
                    return productInventory;
                }
                //如果没有读取到，那么等待一段时间
                else {
                    Thread.sleep(20);
                    endTime = System.currentTimeMillis();
                    waitTime = endTime - startTime;
                }
            }
            System.out.println("------------日志---------:获取商户库存等待超时直接从数据库中获取， productId=" + productId);
            //那么直接尝试从数据库中读取数据
            productInventory = productInventoryService.findProductInventory(productId);
            if (productInventory != null) {
                System.out.println("------------日志---------:获取商户库存等待超时直接从数据库中获取， productId=" + productId);
                request = new ProductInventoryCacheRefreshRequest(productInventory.getProductId(), productInventoryService, true);
                //代码运行到这里情况
                //1.上一次也是读请求，数据刷到redis，redis被清理掉了，标志位还是false，下一次读请求从缓存中拿不到数据
                //2.可能在200ms内，上一次的读请求可能就是队列中积压着，还没有执行（这就要做扩容等各种优化了）
                //3.数据库本身就没有 （缓存穿透，每次都会穿透redis， 请求到达mysql库）
                requestAsyncProcessService.process(request);
                return productInventory;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ProductInventory(productId, -1L);
    }
}
