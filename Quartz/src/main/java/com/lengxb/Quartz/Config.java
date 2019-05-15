package com.lengxb.Quartz;

import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

@Configuration
public class Config {
	@Bean(name="quartzDetail")
	public MethodInvokingJobDetailFactoryBean quartzDetail(Job job) {
		MethodInvokingJobDetailFactoryBean quartzDetail = new MethodInvokingJobDetailFactoryBean();
		quartzDetail.setTargetObject(job);
		quartzDetail.setTargetMethod("todo");
		quartzDetail.setConcurrent(false);
		return quartzDetail;
	}
	@Bean(name="cronTrigger")
	public CronTriggerFactoryBean cronTrigger(@Qualifier("quartzDetail") MethodInvokingJobDetailFactoryBean quartzDetail) {
		CronTriggerFactoryBean cronTrigger = new CronTriggerFactoryBean();
		cronTrigger.setJobDetail(quartzDetail.getObject());
		cronTrigger.setCronExpression("0/10 * * * * ? ");//秒分时日月周年
		return cronTrigger;
	}
	@Bean(name="schedulerFactoryBean")
    public SchedulerFactoryBean schedulerFactoryBean(@Qualifier("cronTrigger") Trigger cronTrigger){
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        schedulerFactoryBean.setTriggers(cronTrigger);
        return  schedulerFactoryBean;
    }
}