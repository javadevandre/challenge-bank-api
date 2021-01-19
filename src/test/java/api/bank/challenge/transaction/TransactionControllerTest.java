package api.bank.challenge.transaction;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import api.bank.challenge.account.Account;
import api.bank.challenge.account.AccountNotFoundException;

@WebMvcTest(TransactionController.class)
public class TransactionControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@MockBean
	private TransactionService transactionService;
	
	@Test
	public void transfer_whenAccountsExistsAndBalanceIsValid() throws Exception {
		Account fromAccount = new Account("01234567890", "Andre Luiz Cirino dos Santos");
		fromAccount.setBalance(new BigDecimal(1000));
		Account toAccount = new Account("09876543210", "Outro Andre");
		Transaction transaction = new Transaction(
				fromAccount,
				toAccount, 
				new BigDecimal(1000)
		);
		Transaction returnTransaction = new Transaction(
				fromAccount,
				toAccount,
				TransactionType.TRANSFER,
				new BigDecimal(1000)
		);
		when(transactionService.transfer(transaction)).thenReturn(returnTransaction);
		
		mockMvc.perform(post("/transaction/transfer")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(transaction)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("type").value("TRANSFER"))
				.andExpect(jsonPath("value").value(1000));
	}
	
	@Test
	public void transfer_whenAccountsExistsAndBalanceIsNotValid() throws Exception {
		Account fromAccount = new Account("01234567890", "Andre Luiz Cirino dos Santos");
		fromAccount.setBalance(new BigDecimal(1000));
		Account toAccount = new Account("09876543210", "Outro Andre");
		Transaction transaction = new Transaction(
				fromAccount,
				toAccount, 
				new BigDecimal(2000)
		);
		when(transactionService.transfer(transaction)).thenThrow(new TransactionNotAllowedException());
		
		mockMvc.perform(post("/transaction/transfer")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(transaction)))
				.andExpect(status().isConflict());
	}
	
	@Test
	public void transfer_whenAnyAccountDoesntExists() throws Exception {
		Account fromAccount = new Account("01234567890", "Andre Luiz Cirino dos Santos");
		fromAccount.setBalance(new BigDecimal(1000));
		Account toAccount = new Account("09876543210", "Outro Andre");
		Transaction transaction = new Transaction(
				fromAccount,
				toAccount, 
				new BigDecimal(1000)
		);
		when(transactionService.transfer(transaction)).thenThrow(new AccountNotFoundException());
		
		mockMvc.perform(post("/transaction/transfer")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(transaction)))
				.andExpect(status().isNotFound());
	}
	
	@Test
	public void withdraw_whenAccountExistsAndBalanceIsValid() throws Exception {
		Account fromAccount = new Account("01234567890", "Andre Luiz Cirino dos Santos");
		fromAccount.setBalance(new BigDecimal(1000));
		Transaction transaction = new Transaction(
				fromAccount,
				new BigDecimal(1000)
		);
		Transaction returnTransaction = new Transaction(
				fromAccount,
				TransactionType.WITHDRAW,
				new BigDecimal(1000)
		);
		when(transactionService.withdraw(transaction)).thenReturn(returnTransaction);
		
		mockMvc.perform(post("/transaction/withdraw")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(transaction)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("type").value("WITHDRAW"))
				.andExpect(jsonPath("value").value(1000));
	}
	
	@Test
	public void withdraw_whenAccountExistsAndBalanceIsNotValid() throws Exception {
		Account fromAccount = new Account("01234567890", "Andre Luiz Cirino dos Santos");
		fromAccount.setBalance(new BigDecimal(1000));
		Transaction transaction = new Transaction(
				fromAccount,
				new BigDecimal(2000)
		);
		when(transactionService.withdraw(transaction)).thenThrow(new TransactionNotAllowedException());
		
		mockMvc.perform(post("/transaction/withdraw")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(transaction)))
				.andExpect(status().isConflict());
	}
	
	@Test
	public void withdraw_whenAccountDoesntExists() throws Exception {
		Account fromAccount = new Account("01234567890", "Andre Luiz Cirino dos Santos");
		fromAccount.setBalance(new BigDecimal(1000));
		Transaction transaction = new Transaction(
				fromAccount,
				new BigDecimal(1000)
		);
		when(transactionService.withdraw(transaction)).thenThrow(new AccountNotFoundException());
		
		mockMvc.perform(post("/transaction/withdraw")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(transaction)))
				.andExpect(status().isNotFound());
	}
	
	@Test
	public void deposit_whenAccountExists() throws Exception {
		Account toAccount = new Account("01234567890", "Andre Luiz Cirino dos Santos");
		Transaction transaction = new Transaction(new BigDecimal(1000));
		transaction.setToAccount(toAccount);
		Transaction returnTransaction = new Transaction(
				TransactionType.DEPOSIT,
				new BigDecimal(1000)
		);
		returnTransaction.setToAccount(toAccount);
		when(transactionService.deposit(transaction)).thenReturn(returnTransaction);
		
		mockMvc.perform(post("/transaction/deposit")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(transaction)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("type").value("DEPOSIT"))
				.andExpect(jsonPath("value").value(1000));
	}
	
	@Test
	public void deposit_whenAccountDoesntExists() throws Exception {
		Account toAccount = new Account("01234567890", "Andre Luiz Cirino dos Santos");
		Transaction transaction = new Transaction(new BigDecimal(1000));
		transaction.setToAccount(toAccount);
		when(transactionService.deposit(transaction)).thenThrow(new AccountNotFoundException());
		
		mockMvc.perform(post("/transaction/deposit")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(transaction)))
				.andExpect(status().isNotFound());
	}

}