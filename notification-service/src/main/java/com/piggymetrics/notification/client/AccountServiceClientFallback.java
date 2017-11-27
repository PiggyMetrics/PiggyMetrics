package com.piggymetrics.notification.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class AccountServiceClientFallback implements AccountServiceClient {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public String getAccount(String accountName) {
		logger.warn("Fallback() called.");
		return null;
	}

}
