package api.bank.challenge.transaction;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import api.bank.challenge.account.Account;

@DataMongoTest
public class TransactionRepositoryTest {

	@Autowired
	private TransactionRepository transactionRepository;
	
	@Test
	public void createTransaction_returnTransactionCreated() {
		Account fromAccount = new Account("01234567890", "Andre Luiz Cirino dos Santos");
		fromAccount.setBalance(new BigDecimal(2000));
		Account toAccount = new Account("09876543210", "Outro Andre");
		Transaction transaction = new Transaction(
				fromAccount,
				toAccount,
				TransactionType.TRANSFER,
				new BigDecimal(1500),
				LocalDateTime.now(ZoneId.of(("America/Sao_Paulo")))
		);
		Transaction createdTransaction = transactionRepository.save(transaction);
		assertThat(createdTransaction).isEqualTo(transaction);
	}
}
