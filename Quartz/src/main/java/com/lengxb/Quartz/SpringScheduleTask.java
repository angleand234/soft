package com.lengxb.Quartz;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * spring自带任务框架，有弊端
 */
@Component
@EnableScheduling //必须加噢
public class SpringScheduleTask {

    /**
     *  每分钟执行一次
     */
    @Scheduled(cron = "*/12 * * * * ?")
    public void reptilian(){
        System.out.println("spring执行调度任务："+new Date());
        try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    @Scheduled(cron = "*/5 * * * * ?")
    public void reptilian1(){
        System.out.println("spring执行调度任务1："+new Date());
    }
}

