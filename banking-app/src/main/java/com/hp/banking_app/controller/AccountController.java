package com.hp.banking_app.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hp.banking_app.dto.AccountDto;
import com.hp.banking_app.dto.FundtransferDto;
import com.hp.banking_app.dto.TransactionsDto;
import com.hp.banking_app.service.AccountService;

@RestController
@RequestMapping("api/accounts")
public class AccountController {

	private AccountService accountService;

	public AccountController(AccountService accountService) {
		super();
		this.accountService = accountService;
	}
	
	//add account rest api
	
	@PostMapping("/add")
	public ResponseEntity<AccountDto> addAccount(@RequestBody AccountDto accountDto){
		
		
		return new ResponseEntity<AccountDto>(accountService.createAccount(accountDto), HttpStatus.CREATED);
		
	}
	
	
	@GetMapping("/{id}")
	public ResponseEntity<AccountDto> getAccountByid(@PathVariable Long id){
		AccountDto accountDto= accountService.getAccountById(id);
		
		return ResponseEntity.ok(accountDto);
		
	}
	
	
	//deposit rest api
	
	@PutMapping("/{id}/deposit")
	public ResponseEntity<AccountDto> depositById(@PathVariable("id") Long id,
														@RequestBody Map<String,Double> map) {
		
		Double amount=map.get("amount");
		
		AccountDto accountDto=accountService.deposit(id, amount);
		
		return ResponseEntity.ok(accountDto);
		
	}

	
	//withdraw rest api
	
	@PutMapping("/{id}/withdraw")
	public ResponseEntity<AccountDto> withdraw(@PathVariable Long id, 
														@RequestBody Map<String,Double> map){
		
		double amount=map.get("amount");
		
		AccountDto accountDto= accountService.withdraw(id, amount);
		
		return ResponseEntity.ok(accountDto);
		
	}
	
	//get all accounts rest api
	
	@GetMapping
	public ResponseEntity<List<AccountDto>> getAllAccounts(){		
		
		return ResponseEntity.ok(accountService.getAllAccounts());
	}
	
	
	// delete rest api
	
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteAccount(@PathVariable long id){
		
		accountService.deleteAccount(id);
		
		return ResponseEntity.ok("account is  deleted successfully.");
	}
	
	@PostMapping("/transfer")
	public ResponseEntity<String> transferFunds(@RequestBody FundtransferDto fundtransferDto){
		
		accountService.transferFunds(fundtransferDto);
		
		return ResponseEntity.ok("transfer funds executed successfully");
		
		
	}
	
	@GetMapping("/{accountId}/transactions")
	public ResponseEntity<List<TransactionsDto>> getAllTransactions(@PathVariable Long accountId){
		
		List<TransactionsDto> list= accountService.getAccountTransactions(accountId);
		
		System.out.println("hello world");
		
		return ResponseEntity.ok(list);
	}
}
