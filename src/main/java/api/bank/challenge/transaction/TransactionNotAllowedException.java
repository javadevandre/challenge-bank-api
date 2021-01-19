package api.bank.challenge.transaction;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "Transaction not allowed: not enough funds.")
public class TransactionNotAllowedException extends RuntimeException {
	private static final long serialVersionUID = 1L;

}
