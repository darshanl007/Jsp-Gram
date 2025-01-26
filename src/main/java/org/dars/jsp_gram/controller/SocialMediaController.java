package org.dars.jsp_gram.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SocialMediaController {
	
	@GetMapping("/")
	public String loadLogin() {
		return "login.html";
	}
	
}
