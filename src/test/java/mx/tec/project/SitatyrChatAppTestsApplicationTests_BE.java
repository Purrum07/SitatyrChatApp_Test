package mx.tec.project;

import org.apache.tomcat.util.json.JSONParser;
import org.assertj.core.util.Arrays;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import mx.tec.project.util.JsonString;
import mx.tec.project.vo.Credentials;

import static org.assertj.core.api.Assertions.assertThat;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class SitatyrChatAppTestsApplicationTests_BE {

	HttpClient client = HttpClient.newHttpClient();
	ObjectMapper mapper = new ObjectMapper();

	@Resource
	private JsonString jsonString;

	@Test
	public void givenARequestLogin_WhenValidCredentials_ThenRecieveTokenAndUserId() throws Exception {

		// Given
		Credentials creden = new Credentials("sasa123", "sasa123");

		String uri = "https://sitatyr-chat-app-be.herokuapp.com/api/user/login";

		// When
		HttpRequest req = HttpRequest.newBuilder().uri(URI.create(uri)).header("Content-Type", "application/json")
				.POST(BodyPublishers.ofString(jsonString.asJsonString(creden))).build();

		HttpResponse<String> res = client.send(req, BodyHandlers.ofString());
		Map<String, String> bodyResponse = mapper.readValue(res.body(), HashMap.class);

		String token = bodyResponse.toString().substring(21, bodyResponse.toString().length() - 2);

		// Then
		assertEquals(200, res.statusCode());
		assertThat(token != null);
		assertEquals(13, bodyResponse.get("id"));

	}

	@Test
	public void givenARequestLogin_WhenInvalidUsername_ThenRecieveStatus401() throws Exception {

		// Given
		Credentials creden = new Credentials("sasa1234", "sasa123");

		String uri = "https://sitatyr-chat-app-be.herokuapp.com/api/user/login";

		// When
		HttpRequest req = HttpRequest.newBuilder().uri(URI.create(uri)).header("Content-Type", "application/json")
				.POST(BodyPublishers.ofString(jsonString.asJsonString(creden))).build();

		HttpResponse<String> res = client.send(req, BodyHandlers.ofString());

		// Then
		assertEquals(401, res.statusCode());
	}

	@Test
	public void givenARequestLogin_WhenInvalidPassword_ThenRecieveStatus401() throws Exception {

		// Given
		Credentials creden = new Credentials("sasa123", "sasa124");

		String uri = "https://sitatyr-chat-app-be.herokuapp.com/api/user/login";

		// When
		HttpRequest req = HttpRequest.newBuilder().uri(URI.create(uri)).header("Content-Type", "application/json")
				.POST(BodyPublishers.ofString(jsonString.asJsonString(creden))).build();

		HttpResponse<String> res = client.send(req, BodyHandlers.ofString());

		// Then
		assertEquals(401, res.statusCode());
	}

	@Test
	public void givenARequestLogin_WhenInvalidCredentials_ThenRecieveStatus401() throws Exception {

		// Given
		Credentials creden = new Credentials("sasa1234", "sasa1234");

		String uri = "https://sitatyr-chat-app-be.herokuapp.com/api/user/login";

		// When
		HttpRequest req = HttpRequest.newBuilder().uri(URI.create(uri)).header("Content-Type", "application/json")
				.POST(BodyPublishers.ofString(jsonString.asJsonString(creden))).build();

		HttpResponse<String> res = client.send(req, BodyHandlers.ofString());

		// Then
		assertEquals(401, res.statusCode());
	}

	@Test
	public void givenAnAhutenticatedUser_WhenObtainAllContacts_ThenRecieveMessageOfConfirmation() throws Exception {

		// Given
		Credentials creden = new Credentials("sasa123", "sasa123");

		String loginUri = "https://sitatyr-chat-app-be.herokuapp.com/api/user/login";

		// When
		HttpRequest reqLogin = HttpRequest.newBuilder().uri(URI.create(loginUri)).header("Content-Type", "application/json")
				.POST(BodyPublishers.ofString(jsonString.asJsonString(creden))).build();

		HttpResponse<String> resLogin = client.send(reqLogin, BodyHandlers.ofString());
		Map<String, String> bodyResponseLogin = mapper.readValue(resLogin.body(), HashMap.class);

		String token = bodyResponseLogin.toString().substring(21, bodyResponseLogin.toString().length() - 2);

		String contactsUri = "https://sitatyr-chat-app-be.herokuapp.com/api/contacts";
		
		String beaerToken = "Bearer " + token;

		// When
		HttpRequest reqContacts = HttpRequest.newBuilder().uri(URI.create(contactsUri)).header("Authorization", beaerToken)
				.GET().build();

		HttpResponse<String> resContacts = client.send(reqContacts, BodyHandlers.ofString());
		String bodyResponseContacts = resContacts.body();

		//Then
		assertEquals("Test GET to /api/contacts", bodyResponseContacts);
	}
	
	@Test
	public void givenAnUnahutenticatedUser_WhenObtainAllContacts_ThenRecieveStatus401() throws Exception {

		// Given
		Credentials creden = new Credentials("sasa1234", "sasa1234");

		String loginUri = "https://sitatyr-chat-app-be.herokuapp.com/api/user/login";

		// When
		HttpRequest reqLogin = HttpRequest.newBuilder().uri(URI.create(loginUri)).header("Content-Type", "application/json")
				.POST(BodyPublishers.ofString(jsonString.asJsonString(creden))).build();

		HttpResponse<String> resLogin = client.send(reqLogin, BodyHandlers.ofString());

		String contactsUri = "https://sitatyr-chat-app-be.herokuapp.com/api/contacts";
		
		String beaerToken = "Bearer ";

		// When
		HttpRequest reqContacts = HttpRequest.newBuilder().uri(URI.create(contactsUri)).header("Authorization", beaerToken)
				.GET().build();

		HttpResponse<String> resContacts = client.send(reqContacts, BodyHandlers.ofString());

		//Then
		assertEquals(401, resContacts.statusCode());
	}
	
	@Test
	public void givenAnAhutenticatedUser_WhenObtainSpecificContactOfUser_ThenRecieveListOfContacts() throws Exception {

		// Given
		Credentials creden = new Credentials("Tostador", "test123");

		String loginUri = "https://sitatyr-chat-app-be.herokuapp.com/api/user/login";

		// When
		HttpRequest reqLogin = HttpRequest.newBuilder().uri(URI.create(loginUri)).header("Content-Type", "application/json")
				.POST(BodyPublishers.ofString(jsonString.asJsonString(creden))).build();

		HttpResponse<String> resLogin = client.send(reqLogin, BodyHandlers.ofString());
		Map<String, String> bodyResponseLogin = mapper.readValue(resLogin.body(), HashMap.class);
		
		String id = String.valueOf(bodyResponseLogin.get("id"));
		
		String token = bodyResponseLogin.toString().substring(20, bodyResponseLogin.toString().length() - 2);

		String contactsUri = "https://sitatyr-chat-app-be.herokuapp.com/api/contacts/" + id;
		
		String bearerToken = "Bearer " + token;

		// When
		HttpRequest reqContacts = HttpRequest.newBuilder().uri(URI.create(contactsUri)).header("Authorization", bearerToken).header("Content-Type", "application/json")
				.GET().build();

		HttpResponse<String> resContacts = client.send(reqContacts, BodyHandlers.ofString());
		
		String bodyResponseContacts = resContacts.body();
		String contactsString = bodyResponseContacts.substring(1,bodyResponseContacts.length()-1);
		String[] contacts = contactsString.split("}");
		
		//Then
		assertEquals(200, resContacts.statusCode());
		assertEquals(3, contacts.length);
	}
	
	@Test
	public void givenAnAhutenticatedUser_WhenObtainSpecificContactOfUserWithoutContacts_ThenRecieveEmptyList() throws Exception {

		// Given
		Credentials creden = new Credentials("sasa123", "sasa123");

		String loginUri = "https://sitatyr-chat-app-be.herokuapp.com/api/user/login";

		// When
		HttpRequest reqLogin = HttpRequest.newBuilder().uri(URI.create(loginUri)).header("Content-Type", "application/json")
				.POST(BodyPublishers.ofString(jsonString.asJsonString(creden))).build();

		HttpResponse<String> resLogin = client.send(reqLogin, BodyHandlers.ofString());
		Map<String, String> bodyResponseLogin = mapper.readValue(resLogin.body(), HashMap.class);
		
		String id = String.valueOf(bodyResponseLogin.get("id"));
		
		String token = bodyResponseLogin.toString().substring(21, bodyResponseLogin.toString().length() - 2);

		String contactsUri = "https://sitatyr-chat-app-be.herokuapp.com/api/contacts/" + id;
		
		String bearerToken = "Bearer " + token;

		// When
		HttpRequest reqContacts = HttpRequest.newBuilder().uri(URI.create(contactsUri)).header("Authorization", bearerToken).header("Content-Type", "application/json")
				.GET().build();

		HttpResponse<String> resContacts = client.send(reqContacts, BodyHandlers.ofString());
		
		String bodyResponseContacts = resContacts.body();
		
		//Then
		assertEquals(200, resContacts.statusCode());
		assertEquals("[]", bodyResponseContacts);
	}

}