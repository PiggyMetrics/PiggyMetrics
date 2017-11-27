package com.piggymetrics.account.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.piggymetrics.account.domain.User;

@Component
public class AuthServiceClientFallback implements AuthServiceClient {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public void createUser(User user) {
		logger.warn("Fallback() called.");
	}

}
