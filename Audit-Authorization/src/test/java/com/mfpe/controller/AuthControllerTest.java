package com.mfpe.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import com.mfpe.model.AuthenticationRequest;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

	@InjectMocks
	private AuthController authController; // from where we will assert

	@Test
	void loginTest() throws Exception {
		AuthenticationRequest user = new AuthenticationRequest("admin", "admin");
		ResponseEntity<?> response = authController.generateJwt(user);
		assertEquals(200, response.getStatusCodeValue());
	}

	@Test
	void loginTestFailed() {
		AuthenticationRequest user = new AuthenticationRequest("admin", "admin");
		Exception e = assertThrows(Exception.class, () -> {
			ResponseEntity<?> response = authController.generateJwt(user);
			assertEquals(403, response.getStatusCodeValue()); // 403 forbidden
		});
	}

	@Test
	void validateTestValidtoken() throws Exception {

		AuthenticationRequest user = new AuthenticationRequest("admin2", "admin2");
		ResponseEntity<String> token = authController.generateJwt(user);
		System.out.println(token);
		ResponseEntity<?> validity = authController.validateJwt("Bearer "+token);
		assertEquals(true, validity.getBody().toString().contains("true"));

	}

	@Test
	void validateTestInValidtoken() {
		ResponseEntity<?> validity = authController.validateJwt("bearer token");
		assertEquals(false, validity.getBody().toString().contains("false"));
	}
}
