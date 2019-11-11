package com.lengxb.SpringBatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.scope.context.ChunkContext;

public class CsvChunkListener implements ChunkListener{
	Logger log = LoggerFactory.getLogger(this.getClass());
	int i =1;

	@Override
	public void beforeChunk(ChunkContext context) {
		// TODO Auto-generated method stub
		log.info("!!! Chunk 执行前!");
	}

	@Override
	public void afterChunk(ChunkContext context) {
		// TODO Auto-generated method stub
		log.info("!!! Chunk:"+ i++ +" 执行完成!");
	}

	@Override
	public void afterChunkError(ChunkContext context) {
		// TODO Auto-generated method stub
		log.info("!!! Chunk 执行错误!");
	}

}
