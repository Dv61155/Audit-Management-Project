package com.mfpe.exception;

import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@Data
@NoArgsConstructor
public class EmptyInputException  extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	private String message;

	public EmptyInputException(String message) {
		super();
		this.message = message;
	}
	
	

}