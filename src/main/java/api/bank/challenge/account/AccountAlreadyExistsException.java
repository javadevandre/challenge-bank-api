package api.bank.challenge.account;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "Account Already Exists.")
public class AccountAlreadyExistsException extends RuntimeException {
	private static final long serialVersionUID = 1L;
}
