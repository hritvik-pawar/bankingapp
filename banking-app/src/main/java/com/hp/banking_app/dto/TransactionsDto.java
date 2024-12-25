package com.hp.banking_app.dto;

import java.time.LocalDateTime;

public record TransactionsDto(Long id,
							Long accountId,
							double amount,
							String transactionType,
							LocalDateTime timestamp	) {

}
