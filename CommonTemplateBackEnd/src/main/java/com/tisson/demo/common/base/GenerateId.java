package com.tisson.demo.common.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tisson.demo.common.util.IdWorker;

import tk.mybatis.mapper.genid.GenId;

public class GenerateId implements GenId<String>{
	private final static Logger LOGGER = LoggerFactory.getLogger(GenerateId.class);

	@Override
	public String genId(String table, String column) {
		try {
			IdWorker worker=IdWorker.getFlowIdWorkerInstance();
			return String.valueOf(worker.nextId());
		} catch (Exception e) {
			LOGGER.error("ERROR in genId,table:[{}],column:[{}]",table,column);
			LOGGER.error("ERROR in genId:",e);
		}
		return null;
	}
}
