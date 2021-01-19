package api.bank.challenge.transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.data.mongodb.core.mapping.Document;

import api.bank.challenge.account.Account;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@Document(collection = "transaction")
public class Transaction {
	
	private Account fromAccount;
	
	private Account toAccount;
	
	private TransactionType type;
	
	@NonNull
	private BigDecimal value;
	
	private LocalDateTime date;
	
	public Transaction(@NonNull BigDecimal value) {
		super();
		this.value = value;
	}
	
	public Transaction(@NonNull Account fromAccount, @NonNull BigDecimal value) {
		super();
		this.fromAccount = fromAccount;
		this.value = value;
	}

	public Transaction(@NonNull Account fromAccount, Account toAccount, @NonNull BigDecimal value) {
		super();
		this.fromAccount = fromAccount;
		this.toAccount = toAccount;
		this.value = value;
	}
	
	public Transaction(TransactionType type, @NonNull BigDecimal value) {
		super();
		this.type = type;
		this.value = value;
	}
	
	public Transaction(@NonNull Account fromAccount, TransactionType type, @NonNull BigDecimal value) {
		super();
		this.fromAccount = fromAccount;
		this.type = type;
		this.value = value;
	}
	
	public Transaction(@NonNull Account fromAccount, Account toAccount, TransactionType type, @NonNull BigDecimal value) {
		super();
		this.fromAccount = fromAccount;
		this.toAccount = toAccount;
		this.type = type;
		this.value = value;
	}

	public Transaction(@NonNull Account fromAccount, Account toAccount, TransactionType type, @NonNull BigDecimal value,
			@NonNull LocalDateTime date) {
		super();
		this.fromAccount = fromAccount;
		this.toAccount = toAccount;
		this.type = type;
		this.value = value;
		this.date = date;
	}

}
