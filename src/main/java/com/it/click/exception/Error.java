package com.it.click.exception;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Error {

	@GetMapping("/error")
	public NoValueException name() {
		return new NoValueException("/login","Bad Request","we are redirecting it");
	}
}
