package com.csair.csairmind.hunter.client.content;


import com.csair.csairmind.hunter.client.service.WrapService;
import com.csair.csairmind.hunter.client.work.DetailsWork;
import com.csair.csairmind.hunter.client.work.HeartWorker;
import com.csair.csairmind.hunter.client.work.ResourceWork;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 程序上下文
 * Created by zhangcheng
 */
@Slf4j
@Component
public class DefaultApplicationContext implements ApplicationContext {

    private static final Object sycObject = new Object();
    private static ApplicationContext instance;

    @Autowired
    WrapService wrapService ;

    @Autowired
    HeartWorker heartWorker;

    @Autowired
    ResourceWork resourceWork;

    @Autowired
    DetailsWork detailsWork;

    public static ApplicationContext context() {
        if (instance == null) {
            synchronized (sycObject) {
                if (instance == null) {
                    instance = new DefaultApplicationContext();
                }
            }
        }
        return instance;
    }

    @Override
    public void start() {
        //机器注册
        if (wrapService.register()) {
            //启动心跳任务
            heartWorker.start();
            //启动心跳资源解析任务
            resourceWork.start();
            //启动详情解析任务
            detailsWork.start();
        } else {
           log.error("注册机器失败........");
        }
    }

    @Override
    public void stop() {

    }

    /**
     * 休息一下
     *
     * @param millionSeconds
     */
    protected void takeARest(int millionSeconds) {
        try {
            Thread.sleep(millionSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 会话key
     */
    public static String SESSIONKEY = "";
    /**
     * 机器编号
     */
    public static String MACHINEID = "";
}
