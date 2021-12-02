package mx.tec.project;

import org.junit.jupiter.api.Test;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import mx.tec.project.vo.Credentials;

import static org.hamcrest.CoreMatchers.equalTo;

@WebMvcTest
public class SitatyrChatAppTestsApplicationTests {
	@Autowired
	private MockMvc mockMvc;
	
	@Test
	public void givenARequestLogin() throws Exception {
		Credentials creden = new Credentials();
		creden.setPassword("sasa123");
		creden.setUsername("sasa123");
		
		
		this.mockMvc.perform(post("https://sitatyr-chat-app-be.herokuapp.com/api/user/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(creden)))
		.andExpect(status().isOk())
		.andExpect(jsonPath("content", equalTo("Hello, Mario Márquez")));
	}
	
	
	public static String asJsonString(final Object obj ) {
		try {
			final ObjectMapper mapper = new ObjectMapper();
			final String jsonContent = mapper.writeValueAsString(obj);
			return jsonContent;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
}