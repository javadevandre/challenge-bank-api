package api.bank.challenge.transaction;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import api.bank.challenge.account.Account;
import api.bank.challenge.account.AccountNotFoundException;
import api.bank.challenge.account.AccountRepository;

@Service
public class TransactionService {
	
	private TransactionRepository transactionRepository;
	
	private AccountRepository accountRepository;
	
	public TransactionService(
			TransactionRepository transactionRepository,
			AccountRepository accountRepository) {
		this.transactionRepository = transactionRepository;
		this.accountRepository = accountRepository;
	}
	
	public Transaction transfer(Transaction transaction) {
		Optional<Account> fromAccount = accountRepository.findById(transaction.getFromAccount().getCpf());
		Optional<Account> toAccount = accountRepository.findById(transaction.getToAccount().getCpf());
		if (fromAccount.isEmpty() || toAccount.isEmpty()) {
			throw new AccountNotFoundException();
		}
		BigDecimal updatedBalance = fromAccount.get().getBalance().subtract(transaction.getValue()).setScale(2, RoundingMode.CEILING);
		if (updatedBalance.compareTo(BigDecimal.ZERO) < 0) {
			throw new TransactionNotAllowedException();
		}
		Account fromAccountAfter = fromAccount.get();
		fromAccountAfter.setBalance(updatedBalance);
		Account toAccountAfter = toAccount.get();
		toAccountAfter.setBalance(toAccountAfter.getBalance().add(transaction.getValue()).setScale(2, RoundingMode.CEILING));
		List<Account> updatedAccounts = accountRepository.saveAll(Arrays.asList(fromAccountAfter, toAccountAfter));
		transaction.setFromAccount(updatedAccounts.get(0));
		transaction.setToAccount(updatedAccounts.get(1));
		transaction.setType(TransactionType.TRANSFER);
		transaction.setDate(LocalDateTime.now(ZoneId.of(("America/Sao_Paulo"))));
		return transactionRepository.save(transaction);
	}
	
	public Transaction withdraw(Transaction transaction) {
		Optional<Account> fromAccount = accountRepository.findById(transaction.getFromAccount().getCpf());
		if (fromAccount.isEmpty()) {
			throw new AccountNotFoundException();
		}
		BigDecimal updatedBalance = fromAccount.get().getBalance().subtract(transaction.getValue().multiply(BigDecimal.valueOf(1.01))).setScale(2, RoundingMode.CEILING);
		if (updatedBalance.compareTo(BigDecimal.ZERO) < 0) {
			throw new TransactionNotAllowedException();
		}
		Account accountAfter = fromAccount.get();
		accountAfter.setBalance(updatedBalance);
		transaction.setFromAccount(accountRepository.save(accountAfter));
		transaction.setType(TransactionType.WITHDRAW);
		transaction.setDate(LocalDateTime.now(ZoneId.of(("America/Sao_Paulo"))));
		return transactionRepository.save(transaction);
	}
	
	public Transaction deposit(Transaction transaction) {
		Optional<Account> toAccount = accountRepository.findById(transaction.getToAccount().getCpf());
		if (toAccount.isEmpty()) {
			throw new AccountNotFoundException();
		}
		BigDecimal updatedBalance = toAccount.get().getBalance().add(transaction.getValue().multiply(BigDecimal.valueOf(1.005))).setScale(2, RoundingMode.CEILING);
		Account accountAfter = toAccount.get();
		accountAfter.setBalance(updatedBalance);
		transaction.setToAccount(accountRepository.save(accountAfter));
		transaction.setType(TransactionType.DEPOSIT);
		transaction.setDate(LocalDateTime.now(ZoneId.of(("America/Sao_Paulo"))));
		return transactionRepository.save(transaction);
	}

}
