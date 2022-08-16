package com.example.project.services;

import java.util.Optional;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.example.project.models.LoginUser;
import com.example.project.models.User;
import com.example.project.repositories.UserRepository;

@Service
public class UserService {
	@Autowired
	UserRepository userRepo;

	public User register(User newUser, BindingResult result) {
		if (userRepo.findByEmail(newUser.getEmail()).isPresent()) {
			result.rejectValue("email", "Unique", "This email is already in use!");
		}
		if (!newUser.getPassword().equals(newUser.getConfirmPassword())) {
			result.rejectValue("confirm", "Matches", "The Confirm Password must match Password!");
		}
		if (result.hasErrors()) {
			return null;
		} else {
			String hashed = BCrypt.hashpw(newUser.getPassword(), BCrypt.gensalt());
			newUser.setPassword(hashed);
			User user = userRepo.save(newUser);
			user.setUser_role("NORMAL");
			userRepo.save(user);
			return user;
		}
	}

	public User login(LoginUser newLogin, BindingResult result) {
		if (result.hasErrors()) {
			return null;
		}
		// fetch Optional user list by its key => email
		Optional<User> potentialUser = userRepo.findByEmail(newLogin.getEmail());

		if (!potentialUser.isPresent()) {
			result.rejectValue("email", "Unique", "Unknown email!");
			return null;
		}
		// if email is correct ,fetch user instance then check pw
		User user = potentialUser.get();
		if (!BCrypt.checkpw(newLogin.getPassword(), user.getPassword())) {
			result.rejectValue("password", "Matches", "Invalid Password!");
		}
		if (result.hasErrors()) {
			return null;
		} else {
			return user;
		}
	}

	/*----------------------------------------------------------------------------
	find logged user by id
	----------------------------------------------------------------------------*/
	public User findById(Long id) {
		Optional<User> optionalUser = userRepo.findById(id);
		if (optionalUser.isPresent()) {
			return optionalUser.get();
		} else {
			return null;
		}
	}

	/*----------------------------------------------------------------------------
	delete user account
	----------------------------------------------------------------------------*/
	public void deleteAccount(Long id) {
		User currentUser = findById(id);
		userRepo.delete(currentUser);
	}

}
