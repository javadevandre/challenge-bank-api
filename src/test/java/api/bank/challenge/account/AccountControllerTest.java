package api.bank.challenge.account;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(AccountController.class)
public class AccountControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@MockBean
	private AccountService accountService;
	
	@Test
	public void createAccount_whenAccountDoesNotExists() throws Exception {
		Account account = new Account("01234567890", "Andre Luiz Cirino dos Santos");
		when(accountService.createAccount(account)).thenReturn(account);
		
		mockMvc.perform(post("/account")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(account)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("cpf").value("01234567890"))
				.andExpect(jsonPath("fullName").value("Andre Luiz Cirino dos Santos"));
	}
	
	@Test
	public void createAccount_whenAccountAlreadyExists() throws Exception {
		Account account = new Account("01234567890", "Andre Luiz Cirino dos Santos");
		when(accountService.createAccount(account)).thenThrow(new AccountAlreadyExistsException());
		
		mockMvc.perform(post("/account")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(account)))
				.andExpect(status().isConflict());
	}
	
}
