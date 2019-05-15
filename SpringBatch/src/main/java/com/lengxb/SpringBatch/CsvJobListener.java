package com.lengxb.SpringBatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;

/** *Job执行监听器  */
public class CsvJobListener extends JobExecutionListenerSupport {
	Logger log = LoggerFactory.getLogger(this.getClass());
	@Override      
	public void afterJob(JobExecution jobExecution) {
		if(jobExecution.getStatus() == BatchStatus.COMPLETED) {
			log.info("!!! JOB 执行完成!");
		}
	}        
	@Override      
	public void beforeJob(JobExecution jobExecution) {
		// TODO Auto-generated method stub
		super.beforeJob(jobExecution);
		log.info("!!! JOB 执行开始!");
	}
}
