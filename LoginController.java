package com.yamudipak.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.yamudipak.model.User;
import com.yamudipak.serviceimpl.UserServiceImpl;
import com.yamudipak.utils.VerifyRecaptcha;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Controller
public class LoginController {
	@Autowired
	private UserServiceImpl usi;
	
@GetMapping("/login")
public String setLogin() {
	return"Login";
}
@PostMapping("/login")
public String getLogin(@ModelAttribute User us,Model model,HttpSession session,
		@RequestParam("g-recaptcha-response") String grCode) throws IOException {
	User u=usi.getUserByUsernameAndPassword(us.getUsername(), us.getPassword());
	
	if(VerifyRecaptcha.verify(grCode)) {
		
		if(u!=null) {
		log.info("-----login success---");
		 session.setAttribute("activeuser",u);
		 session.setMaxInactiveInterval(300);
		 return"Home"; 
	}else {
		log.info("-------   faild to login -------");
		model.addAttribute("name",us.getPassword());
		model.addAttribute("msg","user  not found");
		return"Login";
	}

	}else {
	
	log.info("-------   faild to login -------");
	model.addAttribute("name",us.getPassword());
	model.addAttribute("msg","you are robot");
	return"Login";
	}
}
}
