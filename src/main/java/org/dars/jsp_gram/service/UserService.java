package org.dars.jsp_gram.service;

import org.dars.jsp_gram.dto.User;
import org.dars.jsp_gram.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;

import jakarta.validation.Valid;

@Service
public class UserService {

	@Autowired
	UserRepository userRepository;

	public String loadRegister(ModelMap map, User user) {
		map.put("user", user);
		return "register.html";
	}

	public String register(@Valid User user, BindingResult result) {

		if (!user.getPassword().equals(user.getConfirmpassword()))
			result.rejectValue("confirmpassword", "error.confirmpassword", "Passwords not Matching");

		if (userRepository.existsByEmail(user.getEmail()))
			result.rejectValue("email", "error.email", "Email Already Exists");

		if (userRepository.existsByMobile(user.getMobile()))
			result.rejectValue("mobile", "error.mobile", "Mobile Number Already Exists");

		if (userRepository.existsByUsername(user.getUsername()))
			result.rejectValue("username", "error.username", "Username Already Taken");

		if (result.hasErrors()) {
			return "register.html";
		} else {
			user.setVerified(false);
			userRepository.save(user);
			return "redirect://www.instagram.com";
		}
	}

}
