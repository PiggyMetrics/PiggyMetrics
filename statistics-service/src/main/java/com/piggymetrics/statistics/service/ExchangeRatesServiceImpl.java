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
                container = getDefaultContainer(Currency.getBase());
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

    private ExchangeRatesContainer getDefaultContainer(Currency base) {
        if (base == Currency.EUR) {
            return getDefaultContainer4EUR();
        } else if (base == Currency.USD) {
            return getDefaultContainer4USD();
        } else if (base == Currency.valueOf("GBP")) {
            return getDefaultContainer4GBP();
        } else {
            log.info("No default one for Currency:" + base.name());
            return null;
        }
    }

    private ExchangeRatesContainer getDefaultContainer4EUR() {
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

    private ExchangeRatesContainer getDefaultContainer4GBP() {
        Map<String, BigDecimal> rates = new HashMap<>();
        rates.put("AUD", BigDecimal.valueOf(1.7837));
        rates.put("BGN", BigDecimal.valueOf(2.1958));
        rates.put("BRL", BigDecimal.valueOf(4.5016));
        rates.put("CAD", BigDecimal.valueOf(1.7867));
        rates.put("CHF", BigDecimal.valueOf(1.2966));
        rates.put("CNY", BigDecimal.valueOf(8.7605));
        rates.put("CZK", BigDecimal.valueOf(28.521));
        rates.put("DKK", BigDecimal.valueOf(8.3606));
        rates.put("EUR", BigDecimal.valueOf(1.1227));
        rates.put("HKD", BigDecimal.valueOf(10.823));
        rates.put("HRK", BigDecimal.valueOf(8.3421));
        rates.put("HUF", BigDecimal.valueOf(352.26));
        rates.put("IDR", BigDecimal.valueOf(19017.0));
        rates.put("ILS", BigDecimal.valueOf(4.7805));
        rates.put("INR", BigDecimal.valueOf(89.961));
        rates.put("ISK", BigDecimal.valueOf(138.88));
        rates.put("JPY", BigDecimal.valueOf(145.98));
        rates.put("KRW", BigDecimal.valueOf(1491.7));
        rates.put("MXN", BigDecimal.valueOf(26.113));
        rates.put("MYR", BigDecimal.valueOf(5.3956));
        rates.put("NOK", BigDecimal.valueOf(10.816));
        rates.put("NZD", BigDecimal.valueOf(1.9115));
        rates.put("PHP", BigDecimal.valueOf(71.801));
        rates.put("PLN", BigDecimal.valueOf(4.7052));
        rates.put("RON", BigDecimal.valueOf(5.2339));
        rates.put("RUB", BigDecimal.valueOf(78.924));
        rates.put("SEK", BigDecimal.valueOf(11.416));
        rates.put("SGD", BigDecimal.valueOf(1.8231));
        rates.put("THB", BigDecimal.valueOf(43.414));
        rates.put("TRY", BigDecimal.valueOf(5.2699));
        rates.put("USD", BigDecimal.valueOf(1.3817));
        rates.put("ZAR", BigDecimal.valueOf(16.412));

        ExchangeRatesContainer c = new ExchangeRatesContainer();

        c.setBase(Currency.valueOf("GBP"));
        c.setDate(LocalDate.now());
        c.setRates(rates);

        return c;
    }

    private ExchangeRatesContainer getDefaultContainer4USD() {
        Map<String, BigDecimal> rates = new HashMap<>();
        rates.put("AUD", BigDecimal.valueOf(1.2909));
        rates.put("BGN", BigDecimal.valueOf(1.5892));
        rates.put("BRL", BigDecimal.valueOf(3.258));
        rates.put("CAD", BigDecimal.valueOf(1.2931));
        rates.put("CHF", BigDecimal.valueOf(0.93841));
        rates.put("CNY", BigDecimal.valueOf(6.3403));
        rates.put("CZK", BigDecimal.valueOf(20.642));
        rates.put("DKK", BigDecimal.valueOf(6.0509));
        rates.put("EUR", BigDecimal.valueOf(0.81255));
        rates.put("GBP", BigDecimal.valueOf(0.72373));
        rates.put("HKD", BigDecimal.valueOf(7.8328));
        rates.put("HRK", BigDecimal.valueOf(6.0375));
        rates.put("HUF", BigDecimal.valueOf(254.94));
        rates.put("IDR", BigDecimal.valueOf(13763.0));
        rates.put("ILS", BigDecimal.valueOf(3.4598));
        rates.put("INR", BigDecimal.valueOf(65.108));
        rates.put("ISK", BigDecimal.valueOf(100.51));
        rates.put("JPY", BigDecimal.valueOf(105.65));
        rates.put("KRW", BigDecimal.valueOf(1079.6));
        rates.put("MXN", BigDecimal.valueOf(18.899));
        rates.put("MYR", BigDecimal.valueOf(3.905));
        rates.put("NOK", BigDecimal.valueOf(7.8277));
        rates.put("NZD", BigDecimal.valueOf(1.3834));
        rates.put("PHP", BigDecimal.valueOf(51.965));
        rates.put("PLN", BigDecimal.valueOf(3.4053));
        rates.put("RON", BigDecimal.valueOf(3.7879));
        rates.put("RUB", BigDecimal.valueOf(57.12));
        rates.put("SEK", BigDecimal.valueOf(8.262));
        rates.put("SGD", BigDecimal.valueOf(1.3194));
        rates.put("THB", BigDecimal.valueOf(31.42));
        rates.put("TRY", BigDecimal.valueOf(3.814));
        rates.put("ZAR", BigDecimal.valueOf(11.878));

        ExchangeRatesContainer c = new ExchangeRatesContainer();

        c.setBase(Currency.USD);
        c.setDate(LocalDate.now());
        c.setRates(rates);

        return c;
    }

}
