package com.it.click.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class NoValueException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	private String path ;
	private String status;
	private String message;
}
