package org.dars.jsp_gram.service;

import java.util.Random;

import org.dars.jsp_gram.dto.User;
import org.dars.jsp_gram.helper.AES;
import org.dars.jsp_gram.helper.EmailSender;
import org.dars.jsp_gram.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Service
public class UserService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	EmailSender emailSender;

	public String loadRegister(ModelMap map, User user) {
		map.put("user", user);
		return "register.html";
	}

	public String register(@Valid User user, BindingResult result, HttpSession session) {

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
			user.setPassword(AES.encrypt(user.getPassword()));
			int otp = new Random().nextInt(100000, 1000000);
			user.setOtp(otp);
			System.err.println(otp);
			emailSender.sendOtp(user.getEmail(), otp, user.getFirstname());
			userRepository.save(user);
			session.setAttribute("success", "Otp Sent Success");
			return "redirect:/otp/" + user.getId();
		}
	}

	public String loadOtp(int id, ModelMap map) {
		map.put("id", id);
		return "user-otp.html";
	}

	public String verifyOtp(int otp, int id, HttpSession session) {
		User user = userRepository.findById(id).get();
		if (user.getOtp() == otp) {
			user.setVerified(true);
			user.setOtp(0);
			userRepository.save(user);
			session.setAttribute("success", "Account Created Success");
			return "login.html";
		} else {
			session.setAttribute("failure", "Invalid Otp, Try Again!!!");
			return "redirect:/otp/" + id;
		}

	}

	public String resendOtp(int id, HttpSession session) {
		User user = userRepository.findById(id).get();
		int otp = new Random().nextInt(100000, 1000000);
		user.setOtp(otp);
		System.err.println(otp);
		emailSender.sendOtp(user.getEmail(), otp, user.getFirstname());
		userRepository.save(user);
		session.setAttribute("success", "Otp Re-Sent Success");
		return "redirect:/otp/" + user.getId();
	}

}
