package com.hp.banking_app.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.hp.banking_app.dto.AccountDto;
import com.hp.banking_app.dto.FundtransferDto;
import com.hp.banking_app.dto.TransactionsDto;
import com.hp.banking_app.entity.Account;
import com.hp.banking_app.entity.Transaction;
import com.hp.banking_app.exception.AccountException;
import com.hp.banking_app.mapper.AccountMapper;
import com.hp.banking_app.repository.AccountRepository;
import com.hp.banking_app.repository.TransactionRepository;
import com.hp.banking_app.service.AccountService;


@Service
public class AccountServiceImpl implements AccountService{

	
	private AccountRepository accountRepository;
	
	private TransactionRepository transactionRepository;
	
	public static final String transaction_type_deposit="DEPOSIT";
	public static final String transaction_type_withdraw="WITHDRAW";
	public static final String transaction_type_transfer="TRANSFER";
	
	public AccountServiceImpl(AccountRepository accountRepository,TransactionRepository transactionRepository) {
		super();
		this.accountRepository = accountRepository;
		this.transactionRepository=transactionRepository;
	}



	@Override
	public AccountDto createAccount(AccountDto accountDto) {

		Account account=AccountMapper.mapToAccount(accountDto);
		Account savAccount= accountRepository.save(account);
		return AccountMapper.mapToAccountDto(savAccount);
	}



	@Override
	public AccountDto getAccountById(Long id) {
		Account account= accountRepository
				.findById(id)
				.orElseThrow(() ->  new AccountException("account does not exists."));
		
		return AccountMapper.mapToAccountDto(account);
	}



	@Override
	public AccountDto deposit(Long id, double amount) {
		Account account=accountRepository
				.findById(id)
				.orElseThrow(() -> new AccountException("account does not exists."));
		
		double total=account.getBalance()+amount;
		account.setBalance(total);
		
		Account savedAccount= accountRepository.save(account);
		
		Transaction transaction=new Transaction();
		transaction.setAccountId(id);
		transaction.setAmount(amount);
		transaction.setTransactionType(transaction_type_deposit);
		transaction.setTimestamp(LocalDateTime.now());
		
		transactionRepository.save(transaction);
		
		return AccountMapper.mapToAccountDto(savedAccount);
	}



	@Override
	public AccountDto withdraw(Long id, double amount) {

		Account account=accountRepository
				.findById(id)
				.orElseThrow(() -> new AccountException("account does not exists."));
		
		if(account.getBalance() <amount) {
			throw new RuntimeException("insufficient balance");
		}
		
		double total= account.getBalance() - amount;
		
		account.setBalance(total);
		
		Account savedAccount= accountRepository.save(account);
		
		Transaction transaction=new Transaction();
		transaction.setAccountId(id);
		transaction.setAmount(amount);
		transaction.setTransactionType(transaction_type_withdraw);
		transaction.setTimestamp(LocalDateTime.now());
		
		transactionRepository.save(transaction);
		
		return AccountMapper.mapToAccountDto(savedAccount);
	}



	@Override
	public List<AccountDto> getAllAccounts() {
		
		List<Account> accounts= accountRepository.findAll();
		
		return accounts.stream().map((account) -> AccountMapper.mapToAccountDto(account)).collect(Collectors.toList());
		
		
	}



	@Override
	public void deleteAccount(long id) {

		Account account=accountRepository
				.findById(id)
				.orElseThrow(() -> new AccountException("account does not exists."));
		
		accountRepository.deleteById(id);
		
		
	}



	@Override
	public void transferFunds(FundtransferDto fundtransferDto) {

		
		Account fromAccount= accountRepository
						.findById(fundtransferDto
								.fromAccountId()).orElseThrow(() -> new AccountException("Account does not exists"));
		
		Account toAccount= accountRepository
						.findById(fundtransferDto.toAccountId())
								.orElseThrow(() -> new AccountException("Account does not exists."));
		
		if(fromAccount.getBalance() <fundtransferDto.balance()) {
			throw new RuntimeException("Insufficient balance");
		}
		
		//debit balance from fromAccount
		
		fromAccount.setBalance(fromAccount.getBalance()-fundtransferDto.balance());
		
		toAccount.setBalance(toAccount.getBalance()+fundtransferDto.balance());
		
		accountRepository.save(fromAccount);
		accountRepository.save(toAccount);
		
		Transaction transaction=new Transaction();
		transaction.setAccountId(fundtransferDto.fromAccountId());
		transaction.setAmount(fundtransferDto.balance());
		transaction.setTransactionType(transaction_type_transfer);
		transaction.setTimestamp(LocalDateTime.now());
		
		transactionRepository.save(transaction);
	}



	@Override
	public List<TransactionsDto> getAccountTransactions(Long accountId) {

		List<Transaction> transactions=transactionRepository.findByAccountIdOrderByTimestampDesc(accountId);
		
		List<TransactionsDto> list= transactions.stream().map((transaction) -> convertEntitytoDto(transaction)).collect(Collectors.toList());
		return list;
	}
	
	private TransactionsDto convertEntitytoDto(Transaction transaction) {
		
		TransactionsDto transactionsDto=new TransactionsDto(transaction.getId(),
				transaction.getAccountId(), transaction.getAmount(), transaction.getTransactionType(),transaction.getTimestamp());
		
		return transactionsDto;
	}

}
