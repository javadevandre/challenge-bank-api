package api.bank.challenge.transaction;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import api.bank.challenge.account.Account;
import api.bank.challenge.account.AccountNotFoundException;
import api.bank.challenge.account.AccountRepository;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {
	
	@Mock
	private TransactionRepository transactionRepository;

	@Mock
	private AccountRepository accountRepository;
	
	private TransactionService transactionService;
	
	@BeforeEach
	public void setup() {
		transactionService = new TransactionService(transactionRepository, accountRepository);
	}
	
	@Test
	public void transfer_whenAccountsExistsAndBalanceIsValid() {
		Account fromAccount = new Account("01234567890", "Andre Luiz Cirino dos Santos");
		fromAccount.setBalance(new BigDecimal(2000));
		Account toAccount = new Account("09876543210", "Outro Andre");
		Transaction transaction = new Transaction(
				fromAccount,
				toAccount,
				TransactionType.TRANSFER,
				new BigDecimal(1500)
		);
		when(accountRepository.findById(fromAccount.getCpf())).thenReturn(Optional.of(fromAccount));
		when(accountRepository.findById(toAccount.getCpf())).thenReturn(Optional.of(toAccount));
		when(accountRepository.saveAll(Arrays.asList(fromAccount, toAccount))).thenReturn(Arrays.asList(fromAccount, toAccount));
		when(transactionRepository.save(transaction)).thenReturn(transaction);
		Transaction createdTransaction = transactionService.transfer(transaction);
		assertThat(createdTransaction).isEqualTo(transaction);
		assertThat(fromAccount.getBalance()).isEqualTo(new BigDecimal(500).setScale(2, RoundingMode.CEILING));
		assertThat(toAccount.getBalance()).isEqualTo(new BigDecimal(1500).setScale(2, RoundingMode.CEILING));
	}
	
	@Test
	public void transfer_whenAccountsExistsAndBalanceIsNotValid() {
		Account fromAccount = new Account("01234567890", "Andre Luiz Cirino dos Santos");
		fromAccount.setBalance(new BigDecimal(1000));
		Account toAccount = new Account("09876543210", "Outro Andre");
		Transaction transaction = new Transaction(
				fromAccount,
				toAccount,
				TransactionType.TRANSFER,
				new BigDecimal(2000)
		);
		when(accountRepository.findById(fromAccount.getCpf())).thenReturn(Optional.of(fromAccount));
		when(accountRepository.findById(toAccount.getCpf())).thenReturn(Optional.of(toAccount));
		assertThrows(TransactionNotAllowedException.class, () -> transactionService.transfer(transaction));
	}
	
	@Test
	public void transfer_whenAnyAccountDoesntExists() {
		Account fromAccount = new Account("01234567890", "Andre Luiz Cirino dos Santos");
		fromAccount.setBalance(new BigDecimal(1000));
		Account toAccount = new Account("09876543210", "Outro Andre");
		Transaction transaction = new Transaction(
				fromAccount,
				toAccount,
				TransactionType.TRANSFER,
				new BigDecimal(1000)
		);
		when(accountRepository.findById(fromAccount.getCpf())).thenThrow(new AccountNotFoundException());
		assertThrows(AccountNotFoundException.class, () -> transactionService.transfer(transaction));
	}
	
	@Test
	public void withdraw_whenAccountExistsAndBalanceIsValid() {
		Account fromAccount = new Account("01234567890", "Andre Luiz Cirino dos Santos");
		fromAccount.setBalance(new BigDecimal(1000));
		Transaction transaction = new Transaction(
				fromAccount,
				TransactionType.WITHDRAW,
				new BigDecimal(500)
		);
		when(accountRepository.findById(fromAccount.getCpf())).thenReturn(Optional.of(fromAccount));
		when(transactionRepository.save(transaction)).thenReturn(transaction);
		Transaction createdTransaction = transactionService.withdraw(transaction);
		assertThat(createdTransaction).isEqualTo(transaction);
		BigDecimal result = new BigDecimal(1000-500*1.01).setScale(2);
		assertThat(fromAccount.getBalance()).isEqualTo(result);
	}
	
	@Test
	public void withdraw_whenAccountExistsAndBalanceIsNotValid() {
		Account fromAccount = new Account("01234567890", "Andre Luiz Cirino dos Santos");
		fromAccount.setBalance(new BigDecimal(1000));
		Transaction transaction = new Transaction(
				fromAccount,
				TransactionType.WITHDRAW,
				new BigDecimal(2000)
		);
		when(accountRepository.findById(fromAccount.getCpf())).thenReturn(Optional.of(fromAccount));
		assertThrows(TransactionNotAllowedException.class, () -> transactionService.withdraw(transaction));
	}
	
	@Test
	public void withdraw_whenAccountDoesntExists() {
		Account fromAccount = new Account("01234567890", "Andre Luiz Cirino dos Santos");
		fromAccount.setBalance(new BigDecimal(1000));
		Transaction transaction = new Transaction(
				fromAccount,
				TransactionType.WITHDRAW,
				new BigDecimal(1000)
		);
		when(accountRepository.findById(fromAccount.getCpf())).thenThrow(new AccountNotFoundException());
		assertThrows(AccountNotFoundException.class, () -> transactionService.withdraw(transaction));
	}
	
	@Test
	public void deposit_whenAccountExists() {
		Account toAccount = new Account("01234567890", "Andre Luiz Cirino dos Santos");
		Transaction transaction = new Transaction(
				TransactionType.DEPOSIT,
				new BigDecimal(1000)
		);
		transaction.setToAccount(toAccount);
		when(accountRepository.findById(toAccount.getCpf())).thenReturn(Optional.of(toAccount));
		when(transactionRepository.save(transaction)).thenReturn(transaction);
		Transaction createdTransaction = transactionService.deposit(transaction);
		Account accountAfterDeposit = accountRepository.findById(toAccount.getCpf()).get();
		assertThat(createdTransaction).isEqualTo(transaction);
		BigDecimal result = new BigDecimal(1000*1.005).setScale(2, RoundingMode.CEILING);
		assertThat(accountAfterDeposit.getBalance()).isEqualTo(result);
	}
	
	@Test
	public void deposit_whenAccountDoesntExists() {
		Account toAccount = new Account("01234567890", "Andre Luiz Cirino dos Santos");
		Transaction transaction = new Transaction(
				TransactionType.DEPOSIT,
				new BigDecimal(1000)
		);
		transaction.setToAccount(toAccount);
		when(accountRepository.findById(toAccount.getCpf())).thenThrow(new AccountNotFoundException());
		assertThrows(AccountNotFoundException.class, () -> transactionService.deposit(transaction));
	}
	
}
