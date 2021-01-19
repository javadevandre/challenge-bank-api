package api.bank.challenge.transaction;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import api.bank.challenge.account.Account;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class TransactionIntegrationTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Test
	public void transfer() throws Exception {
		Account fromAccount = new Account("11234567890", "Andre Luiz Cirino dos Santos");
		fromAccount.setBalance(new BigDecimal(1000));
		Account toAccount = new Account("09876543210", "Outro Andre");
		Transaction transaction = new Transaction(
				fromAccount,
				toAccount, 
				new BigDecimal(1000)
		);
		mockMvc.perform(post("/account")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(fromAccount)))
				.andExpect(status().isCreated());
		mockMvc.perform(post("/account")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(toAccount)))
				.andExpect(status().isCreated());
		mockMvc.perform(post("/transaction/transfer")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(transaction)))
				.andExpect(status().isCreated());
	}
	
	@Test
	public void withdraw() throws Exception {
		Account fromAccount = new Account("12345678901", "Andre Luiz Cirino dos Santos");
		fromAccount.setBalance(new BigDecimal(2000));
		Transaction transaction = new Transaction(
				fromAccount,
				new BigDecimal(1000)
		);
		mockMvc.perform(post("/account")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(fromAccount)))
				.andExpect(status().isCreated());
		mockMvc.perform(post("/transaction/withdraw")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(transaction)))
				.andExpect(status().isCreated());
	}
	
	@Test
	public void deposit() throws Exception {
		Account toAccount = new Account("98765432100", "Andre Luiz Cirino dos Santos");
		Transaction transaction = new Transaction(new BigDecimal(1000));
		transaction.setToAccount(toAccount);
		mockMvc.perform(post("/account")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(toAccount)))
				.andExpect(status().isCreated());
		mockMvc.perform(post("/transaction/deposit")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(transaction)))
				.andExpect(status().isCreated());
	}
}
