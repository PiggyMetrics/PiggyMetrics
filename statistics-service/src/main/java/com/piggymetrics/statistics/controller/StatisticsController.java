package com.piggymetrics.statistics.controller;

import com.piggymetrics.statistics.domain.Account;
import com.piggymetrics.statistics.domain.timeseries.DataPoint;
import com.piggymetrics.statistics.service.StatisticsService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
public class StatisticsController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private StatisticsService statisticsService;

	@RequestMapping(value = "/current", method = RequestMethod.GET)
	public List<DataPoint> getCurrentAccountStatistics(Principal principal) {
		return statisticsService.findByAccountName(principal.getName());
	}

	@PreAuthorize("#oauth2.hasScope('server') or #accountName.equals('demo')")
	@RequestMapping(value = "/{accountName}", method = RequestMethod.GET)
	public List<DataPoint> getStatisticsByAccountName(@PathVariable String accountName) {
		return statisticsService.findByAccountName(accountName);
	}

	@PreAuthorize("#oauth2.hasScope('server')")
	@RequestMapping(value = "/{accountName}", method = RequestMethod.PUT)
	public void saveAccountStatistics(@PathVariable String accountName, @Valid @RequestBody Account account) {
		
		// Use user timeout to simulate timeout 
		if ("timeout".equalsIgnoreCase(accountName)) {
			try {
				Thread.sleep(3 * 1000);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
		
		// Use user timeout to simulate exception 
		if ("exception".equalsIgnoreCase(accountName)) {
			throw new RuntimeException("fake statistics exception");
		}
		
		logger.debug("Calling saveAccountStatistics() for {}", accountName);
		logger.info("Calling saveAccountStatistics() for {}", accountName);
		
		statisticsService.save(accountName, account);
	}
}
