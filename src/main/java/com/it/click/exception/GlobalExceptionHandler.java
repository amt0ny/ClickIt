package com.it.click.exception;

import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

	@Autowired
	Gson gson;

	@ExceptionHandler(NoValueException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public Response noValueExceptionHandler(NoValueException nx) {

		LogResponse logResponse = LogResponse.builder()
				.logType(HttpStatus.BAD_REQUEST)
				.message(nx.getLocalizedMessage())
				.status(HttpStatus.BAD_REQUEST.value())
				.build();

		log.error(gson.toJson(logResponse));

		return new Response(nx.getPath(), new Date(), nx.getMessage());
	}

}
