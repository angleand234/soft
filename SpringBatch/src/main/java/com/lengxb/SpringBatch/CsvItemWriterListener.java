package com.lengxb.SpringBatch;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ItemWriteListener;

public class CsvItemWriterListener implements ItemWriteListener<Person> {
	Logger log = LoggerFactory.getLogger(this.getClass());
	int i =1;
	@Override
	public void beforeWrite(List<? extends Person> items) {
		// TODO Auto-generated method stub
		log.info("!!! Writer 执行前!");
	}

	@Override
	public void afterWrite(List<? extends Person> items) {
		// TODO Auto-generated method stub
		log.info("!!! Writer:"+ i++ +" 执行完成!");
	}

	@Override
	public void onWriteError(Exception exception, List<? extends Person> items) {
		// TODO Auto-generated method stub
		log.info("!!! Writer 执行错误!");
	}

}
