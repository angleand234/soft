package com.lengxb.SpringBatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.SkipListener;

public class CsvSkipListener implements SkipListener<Person,Person> {
	Logger log = LoggerFactory.getLogger(this.getClass());
	int i =1;
	@Override
	public void onSkipInRead(Throwable t) {
		// TODO Auto-generated method stub
		log.info("!!! Skip 执行前!");
	}

	@Override
	public void onSkipInWrite(Person item, Throwable t) {
		// TODO Auto-generated method stub
		log.info("!!! Skip:"+ i++ +" 执行完成!");
	}

	@Override
	public void onSkipInProcess(Person item, Throwable t) {
		// TODO Auto-generated method stub
		log.info("!!! Skip 执行错误!");
	}

}
