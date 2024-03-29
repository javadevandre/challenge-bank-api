package api.bank.challenge.account;

import org.springframework.stereotype.Service;

@Service
public class AccountService {
	
	private AccountRepository accountRepository;
	
	public AccountService(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}
	
	public Account createAccount(Account account) {
		if (accountRepository.existsById(account.getCpf())) {
			throw new AccountAlreadyExistsException();
		}
		return accountRepository.save(account);
	}

}
