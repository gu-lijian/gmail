package com.poc.gmail.controller;

import java.io.IOException;
import java.security.GeneralSecurityException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.poc.gmail.action.GmailRegistrationAction;

@Controller
public class GmailController {
	
	@Autowired
	GmailRegistrationAction gmailRegistrationAction;
	
	@RequestMapping(value="/register")
	public String register(){
		return "register";
	}

	@RequestMapping(value="/register/submit")
	@ResponseBody
	public ModelAndView registerGmail(@RequestParam("gmail") String gmail){
		ModelAndView mode = new ModelAndView();
		String message = "";
		try {
			gmailRegistrationAction.getGmailAccess(gmail);
			message = "Thank you for your enrollment: " + gmail;
		} catch (IOException | GeneralSecurityException e) {
			e.printStackTrace();
			message = "We expect your enrollment in near future: " + gmail;
		}	
		mode.addObject("message", message);
		mode.setViewName("result");
		return mode;
	}

}
