package api.bank.challenge.account;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

	@Mock
	private AccountRepository accountRepository;
	
	private AccountService accountService;
	
	@BeforeEach
	public void setUp() throws Exception {
		accountService = new AccountService(accountRepository);
	}
	
	@Test
	public void createAccount_whenAccountDoesNotExists() {
		Account account = new Account("01234567890", "Andre Luiz Cirino dos Santos");
		when(accountRepository.save(account)).thenReturn(account);
		Account createdAccount = accountService.createAccount(account);
		assertThat(createdAccount).isEqualTo(account);
	}
	
	@Test
	public void createAccount_whenAccountAlreadyExists() {
		Account account = new Account("01234567890", "Andre Luiz Cirino dos Santos");
		when(accountRepository.existsById(account.getCpf())).thenReturn(true);
		assertThrows(AccountAlreadyExistsException.class, () -> accountService.createAccount(account));
	}
}
