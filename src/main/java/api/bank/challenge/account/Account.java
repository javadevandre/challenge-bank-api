package api.bank.challenge.account;

import java.math.BigDecimal;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NonNull;

@Data
@Document(collection = "account")
public class Account {
	
	@NonNull
	@Id
	private String cpf;
	
	@NonNull
	private String fullName;
	
	private BigDecimal balance = BigDecimal.ZERO;
}