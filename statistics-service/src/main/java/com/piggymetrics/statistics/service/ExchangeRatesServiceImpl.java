package com.piggymetrics.statistics.service;

import com.google.common.collect.ImmutableMap;
import com.piggymetrics.statistics.client.ExchangeRatesClient;
import com.piggymetrics.statistics.domain.Currency;
import com.piggymetrics.statistics.domain.ExchangeRatesContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
public class ExchangeRatesServiceImpl implements ExchangeRatesService {

	private static final Logger log = LoggerFactory.getLogger(ExchangeRatesServiceImpl.class);

	private ExchangeRatesContainer container;

	@Autowired
	private ExchangeRatesClient client;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<Currency, BigDecimal> getCurrentRates() {

		if (container == null || !container.getDate().equals(LocalDate.now())) {
			container = client.getRates(Currency.getBase());

            if (container == null) {
                log.info("Failed to load real rates so use default one.");
                container = getDefaultContainer();
            }

            log.info("exchange rates has been updated: {}", container);
		}

		return ImmutableMap.of(
				Currency.EUR, container.getRates().get(Currency.EUR.name()),
				Currency.RUB, container.getRates().get(Currency.RUB.name()),
				Currency.USD, BigDecimal.ONE
		);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BigDecimal convert(Currency from, Currency to, BigDecimal amount) {

		Assert.notNull(amount);

		Map<Currency, BigDecimal> rates = getCurrentRates();
		BigDecimal ratio = rates.get(to).divide(rates.get(from), 4, RoundingMode.HALF_UP);

		return amount.multiply(ratio);
	}

    private ExchangeRatesContainer getDefaultContainer() {
        Map<String, BigDecimal> rates = new HashMap<>();
        rates.put("AUD", BigDecimal.valueOf(1.5887));
        rates.put("BGN", BigDecimal.valueOf(1.9558));
        rates.put("BRL", BigDecimal.valueOf(4.0096));
        rates.put("CAD", BigDecimal.valueOf(1.5914));
        rates.put("CHF", BigDecimal.valueOf(1.1549));
        rates.put("CNY", BigDecimal.valueOf(7.803));
        rates.put("CZK", BigDecimal.valueOf(25.404));
        rates.put("DKK", BigDecimal.valueOf(7.4468));
        rates.put("GBP", BigDecimal.valueOf(0.8907));
        rates.put("HKD", BigDecimal.valueOf(9.6398));
        rates.put("HRK", BigDecimal.valueOf(7.4303));
        rates.put("HUF", BigDecimal.valueOf(313.76));
        rates.put("IDR", BigDecimal.valueOf(16938));
        rates.put("ILS", BigDecimal.valueOf(4.258));
        rates.put("INR", BigDecimal.valueOf(80.128));
        rates.put("ISK", BigDecimal.valueOf(123.7));
        rates.put("JPY", BigDecimal.valueOf(130.02));
        rates.put("KRW", BigDecimal.valueOf(1328.7));
        rates.put("MXN", BigDecimal.valueOf(23.259));
        rates.put("MYR", BigDecimal.valueOf(4.8059));
        rates.put("NOK", BigDecimal.valueOf(9.6335));
        rates.put("NZD", BigDecimal.valueOf(1.7026));
        rates.put("PHP", BigDecimal.valueOf(63.953));
        rates.put("PLN", BigDecimal.valueOf(4.1909));
        rates.put("RON", BigDecimal.valueOf(4.6618));
        rates.put("RUB", BigDecimal.valueOf(70.298));
        rates.put("SEK", BigDecimal.valueOf(10.168));
        rates.put("SGD", BigDecimal.valueOf(1.6238));
        rates.put("THB", BigDecimal.valueOf(38.669));
        rates.put("TRY", BigDecimal.valueOf(4.6939));
        rates.put("USD", BigDecimal.valueOf(1.2307));
        rates.put("ZAR", BigDecimal.valueOf(14.618));

        ExchangeRatesContainer c = new ExchangeRatesContainer();

        c.setBase(Currency.EUR);
        c.setDate(LocalDate.now());
        c.setRates(rates);

        return c;
    }
}
