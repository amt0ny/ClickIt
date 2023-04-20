package com.it.click.exception;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
public class LogResponse {

	private String message;
	private int status;
	private HttpStatus logType;
	private Object data;
	
}
