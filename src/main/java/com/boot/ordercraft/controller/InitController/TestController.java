package com.boot.ordercraft.controller.InitController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

import com.boot.ordercraft.service.MailService.MailService;

public class TestController {

	
	@Autowired
	MailService mailService;

	@GetMapping("/testmail")
	public String testMail() {
	    mailService.sendRegistrationEmail("dummy@example.com", "TestUser", "password123");
	    return "Mail sent!";
	}

}
