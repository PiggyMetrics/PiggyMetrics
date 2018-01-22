package com.piggymetrics.account.controller;

import com.piggymetrics.account.domain.Account;
import com.piggymetrics.account.domain.User;
import com.piggymetrics.account.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RestController
public class AccountController {

	@Autowired
	private AccountService accountService;

	/**
	 * 升级异常报告spring java.lang.IllegalArgumentException: Name for argument type [java.lang.String] 异常
	 * @link: http://blog.csdn.net/liuguanghaoputin/article/details/8695600
	 * 
	 * @param name
	 * @return
	 */
	@PreAuthorize("#oauth2.hasScope('server') or #name.equals('demo')")
	@RequestMapping(path = "/{name}", method = RequestMethod.GET)
	public Account getAccountByName(@PathVariable(value="name") String name) {
		return accountService.findByName(name);
	}

	/**
	 * Why authentication is required for this endpoint?
     *
	 * @link: http://www.baeldung.com/get-user-in-spring-security Retrieve User Information in Spring Security.
	 *
	 * @param principal
	 * @return
	 */
	@RequestMapping(path = "/current", method = RequestMethod.GET)
	public Account getCurrentAccount(Principal principal) {
		return accountService.findByName(principal.getName());
	}

	@RequestMapping(path = "/current", method = RequestMethod.PUT)
	public void saveCurrentAccount(Principal principal, @Valid @RequestBody Account account) {
		accountService.saveChanges(principal.getName(), account);
	}

	/**
	 * No authentication is required for this endpoint, why?
	 *
	 * @param user
	 * @return
	 */
	@RequestMapping(path = "/", method = RequestMethod.POST)
	public Account createNewAccount(@Valid @RequestBody User user) {
		return accountService.create(user);
	}
}
