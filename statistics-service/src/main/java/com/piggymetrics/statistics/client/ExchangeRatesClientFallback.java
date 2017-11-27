package com.piggymetrics.statistics.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.piggymetrics.statistics.domain.Currency;
import com.piggymetrics.statistics.domain.ExchangeRatesContainer;

@Component
public class ExchangeRatesClientFallback implements ExchangeRatesClient {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public ExchangeRatesContainer getRates(Currency base) {
		logger.warn("Fallback() called.");
		return null;
	}

}
