package com.lengxb.SpringBatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ItemReadListener;

public class CsvItemReaderListener implements ItemReadListener<Person>{
	Logger log = LoggerFactory.getLogger(this.getClass());
	int i =1;
	@Override
	public void beforeRead() {
		// TODO Auto-generated method stub
		log.info("!!! Reader 执行前!");
	}

	@Override
	public void afterRead(Person item) {
		// TODO Auto-generated method stub
		log.info("!!! Reader:"+ i++ +" 执行完成!");
	}

	@Override
	public void onReadError(Exception ex) {
		// TODO Auto-generated method stub
		log.info("!!! Reader 执行错误!");
	}

}
