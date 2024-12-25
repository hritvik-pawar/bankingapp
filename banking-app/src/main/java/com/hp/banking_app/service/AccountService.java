package com.hp.banking_app.service;

import java.util.List;

import com.hp.banking_app.dto.AccountDto;
import com.hp.banking_app.dto.FundtransferDto;
import com.hp.banking_app.dto.TransactionsDto;

public interface AccountService {

	AccountDto createAccount(AccountDto accountDto);
	
	AccountDto getAccountById(Long id);
	
	AccountDto deposit(Long id, double amount);
	
	AccountDto withdraw(Long id, double amount);
	
	List<AccountDto> getAllAccounts();
	
	void deleteAccount(long id);
	
	void transferFunds(FundtransferDto  fundtransferDto);
	
	List<TransactionsDto> getAccountTransactions(Long accountId);
}
