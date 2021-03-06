package com.jwtToken.controller;

import java.util.List;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin()
public class HelloWorldController {

	//@PreAuthorize("hasRole('INSTRUCTOR')")
	@RequestMapping({ "/hello" })
	public String hello() {
		return "Hello World";
	}

}
