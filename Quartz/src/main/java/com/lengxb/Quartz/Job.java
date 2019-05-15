package com.lengxb.Quartz;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Component;

@Component
public class Job {
static int count1=0;

    public void todo(){
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            count1++;
        System.out.println("计划任务1执行了"+count1+"次!!!"+time);
    }
}