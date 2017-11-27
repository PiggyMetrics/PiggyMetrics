package com.piggymetrics.account.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.piggymetrics.account.domain.Account;

@Component
public class StatisticsServiceClientFallback implements StatisticsServiceClient {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public void updateStatistics(String accountName, Account account) {
		logger.warn("Fallback() called.");
	}

}
