package api.bank.challenge.transaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("transaction")
public class TransactionController {
	
	@Autowired
	private TransactionService transactionService;
	
	@PostMapping("transfer")
	@ApiOperation(
			value = "Transfer funds from one account to another",
            notes = "This will transfer funds from one bank account to another",
            response = Object.class
    )
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Transfer completed.", response = String.class),
			@ApiResponse(code = 404, message = "Account not found.", response = String.class),
			@ApiResponse(code = 409, message = "Transfer not allowed: not enough funds.", response = String.class)
	})
	private ResponseEntity<Object> transfer(@RequestBody Transaction transaction) {
		Transaction createdTransaction = transactionService.transfer(transaction);
		return new ResponseEntity<>(createdTransaction, HttpStatus.CREATED);
	}
	
	@PostMapping("withdraw")
	@ApiOperation(
			value = "Withdraw funds from one account",
            notes = "This will withdraw funds from one bank account. The cost of this operation"
            		+ " is 1% of the amount to withdraw",
            response = Object.class
    )
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Withdraw completed.", response = String.class),
			@ApiResponse(code = 404, message = "Account not found.", response = String.class),
			@ApiResponse(code = 409, message = "Withdraw not allowed: not enough funds.", response = String.class)
	})
	private ResponseEntity<Object> withdraw(@RequestBody Transaction transaction) {
		Transaction createdTransaction = transactionService.withdraw(transaction);
		return new ResponseEntity<>(createdTransaction, HttpStatus.CREATED);
	}
	
	@PostMapping("deposit")
	@ApiOperation(
			value = "Deposit funds into account",
            notes = "This will deposit funds into one bank account. This operation"
            		+ " gives a bonus of 0,5% of the amount to deposit",
            response = Object.class
    )
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Deposit completed.", response = String.class),
			@ApiResponse(code = 404, message = "Account not found.", response = String.class)
	})
	private ResponseEntity<Object> deposit(@RequestBody Transaction transaction) {
		Transaction createdTransaction = transactionService.deposit(transaction);
		return new ResponseEntity<>(createdTransaction, HttpStatus.CREATED);
	}

}
