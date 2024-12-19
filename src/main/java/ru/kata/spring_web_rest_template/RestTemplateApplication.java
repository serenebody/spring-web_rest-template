package ru.kata.spring_web_rest_template;


import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import ru.kata.spring_web_rest_template.model.User;

import java.util.Objects;

@SpringBootApplication
public class RestTemplateApplication {

	private static final String BASE_URL = "http://94.198.50.185:7081/api/users";
	private static final RestTemplate restTemplate = new RestTemplate();

	public static void main(String[] args) {
		String sessionId = getSessionId();
		HttpHeaders headers = createHeaders(sessionId);
		System.out.println(sessionId);
		String part1 = addUser(headers);
		System.out.println("Part 1 of the code: " + part1);

		String part2 = updateUser(headers);
		System.out.println("Part 2 of the code: " + part2);

		String part3 = deleteUser(headers);
		System.out.println("Part 3 of the code: " + part3);

		String finalCode = part1 + part2 + part3;
		System.out.println("Final code: " + finalCode);
	}

	private static String getSessionId() {
		ResponseEntity<String> response = restTemplate.exchange(
				BASE_URL, HttpMethod.GET, null, String.class);
		return Objects.requireNonNull(response.getHeaders().get("Set-Cookie")).get(0);
	}

	private static HttpHeaders createHeaders(String sessionId) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Cookie", sessionId);
		return headers;
	}

	private static String addUser(HttpHeaders headers) {
		User newUser = new User(3L, "James", "Brown", (byte) 30);
		HttpEntity<User> request = new HttpEntity<>(newUser, headers);
		ResponseEntity<String> response = restTemplate.exchange(
				BASE_URL, HttpMethod.POST, request, String.class);
		return response.getBody();
	}

	private static String updateUser(HttpHeaders headers) {
		User updatedUser = new User(3L, "Thomas", "Shelby", (byte) 30);
		HttpEntity<User> request = new HttpEntity<>(updatedUser, headers);
		ResponseEntity<String> response = restTemplate.exchange(
				BASE_URL, HttpMethod.PUT, request, String.class);
		return response.getBody();
	}

	private static String deleteUser(HttpHeaders headers) {
		ResponseEntity<String> response = restTemplate.exchange(
				BASE_URL + "/3", HttpMethod.DELETE, new HttpEntity<>(headers), String.class);
		return response.getBody();
	}

}
