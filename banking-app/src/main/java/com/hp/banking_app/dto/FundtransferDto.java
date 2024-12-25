package com.hp.banking_app.dto;

public record FundtransferDto(Long fromAccountId,
								Long toAccountId,
								double balance) {

	
}
