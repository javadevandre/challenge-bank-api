package api.bank.challenge.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("account")
@Api(value = "Controller responsible for account creation")
public class AccountController {
	
	@Autowired
	private AccountService accountService;

	@PostMapping
	@ApiOperation(
			value = "Create an account",
            notes = "This will create a bank account",
            response = Object.class
    )
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Account Created.", response = String.class),
			@ApiResponse(code = 409, message = "Account already exists.", response = String.class)
	})
	public ResponseEntity<Object> createAccount(@RequestBody Account account) {
		Account createdAccount = accountService.createAccount(account);
		return new ResponseEntity<>(createdAccount, HttpStatus.CREATED);
	}
}
