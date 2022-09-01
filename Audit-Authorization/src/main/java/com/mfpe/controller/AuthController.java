package com.mfpe.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mfpe.model.AuthenticationRequest;
import com.mfpe.model.AuthenticationResponse;
import com.mfpe.service.JwtService;
import com.mfpe.service.ProjectManagerService;

@RestController
@RequestMapping("/auth")	
@CrossOrigin(origins = "*")
public class AuthController {
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private ProjectManagerService projectManagerDetailsService;
	
	@Autowired
	private JwtService jwtService;
	
	Logger logger = LoggerFactory.getLogger("Auth-Controller-Logger");
	
	@GetMapping("health-check")
	public String healthCheck() {
		return "Audit Benchmark Microservice is Active";
	}
	
	// authentication - for the very first login
	@PostMapping("/authenticate")
	public ResponseEntity<String> generateJwt(@Valid @RequestBody AuthenticationRequest request){
		
		ResponseEntity<String> response = null;
		
		// authenticating the User-Credentials
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
		
		// when it authenticates successfully
		UserDetails userDetails = projectManagerDetailsService.loadUserByUsername(request.getUsername());
		
		
		// returning the token as response
		String jwt = jwtService.generateToken(userDetails);	
		
	
		logger.info("Authenticated User :: " + userDetails);
		
		response = new ResponseEntity<String>(jwt, HttpStatus.OK);
		
		logger.info("Successfully Authenticated user!");
			
	
		logger.info("-------- Exiting /authenticate");
		return response;
	}
	
	// validate - for every request it validates the user-credentials from the provided Jwt token in Authorization req. header
	@PostMapping("/validate")
	public ResponseEntity<AuthenticationResponse> validateJwt(@RequestHeader("Authorization") String jwt){
		
		AuthenticationResponse authenticationResponse = new AuthenticationResponse("Invalid", false);
		ResponseEntity<AuthenticationResponse> response = null;

		//first remove Bearer from Header
		jwt = jwt.substring(7);
		
		//check token
		logger.info("--------JWT :: "+jwt);
		
		
		// check the jwt is proper or not
		UserDetails user = projectManagerDetailsService.loadUserByUsername(jwtService.extractUsername(jwt));

		// now validating the jwt
		try {
			if(jwtService.validateToken(jwt, user)) {
				authenticationResponse.setName(user.getUsername());
				authenticationResponse.setValid(true);
				response = new ResponseEntity<AuthenticationResponse>(authenticationResponse, HttpStatus.OK);
				logger.info("Successfully validated the jwt and sending response back!");
			}
			else {
				response = new ResponseEntity<AuthenticationResponse>(authenticationResponse, HttpStatus.FORBIDDEN);
				logger.error("JWT Token validation failed!");
			}
		}catch (Exception e) {
			response = new ResponseEntity<AuthenticationResponse>(authenticationResponse, HttpStatus.BAD_REQUEST);
			logger.error("Exception occured whil validating JWT : Exception info : " + e.getMessage());
		}
		logger.info("-------- Exiting /validate");
		return response;
	}
	
}
