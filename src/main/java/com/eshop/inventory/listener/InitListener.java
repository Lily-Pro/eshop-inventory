package com.eshop.inventory.listener;

import com.eshop.inventory.thread.RequestProcessorThreadPool;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * * 系统初始化监听器
 *
 * @author: RenLiLi
 * @date: 2021/7/19 13:16
 */
public class InitListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        //初始化工作线程池和内存队列
        RequestProcessorThreadPool.init();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("================destroyed====================");
    }
}
