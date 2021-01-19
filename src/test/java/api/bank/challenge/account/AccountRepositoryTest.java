package api.bank.challenge.account;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

@DataMongoTest
public class AccountRepositoryTest {
	
	@Autowired
	private AccountRepository accountRepository;

	@Test
	public void createAccount_returnAccountCreated() {
		Account account = new Account("01234567890", "Andre Luiz Cirino dos Santos");
		Account createdAccount = accountRepository.save(account);
		assertThat(createdAccount).isEqualTo(account);
	}
}
