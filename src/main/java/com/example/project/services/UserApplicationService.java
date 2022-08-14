package com.example.project.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.project.models.User;
import com.example.project.models.UserApplication;
import com.example.project.repositories.UserApplicationRepository;
import com.example.project.repositories.UserRepository;

@Service
public class UserApplicationService {

	@Autowired
	private UserApplicationRepository userApplicationRepository;
	
	@Autowired
	private UserRepository userRepository;;
	
	public UserApplication create(UserApplication app) {
		return userApplicationRepository.save(app);
	}

	public UserApplication findById(String appId) {
		Optional<UserApplication> optionalApp = userApplicationRepository.findById(appId);
		if (optionalApp.isPresent()) {
			return optionalApp.get();
		} else {
			return null;
		}
		
	}
	
	public User findById(Long id) {
		Optional<User> optionalUser = userRepository.findById(id);
		if (optionalUser.isPresent()) {
			return optionalUser.get();
		} else {
			return null;
		}
	}
	
	public UserApplication create_userApp(Long user_id, UserApplication userApplication) {
		Optional<User> current_user = userRepository.findById(user_id);
		userApplication.setUser(current_user.get());
		// --- here add file info
		return userApplicationRepository.save(userApplication);
	}
	
	
	//update UserApplication:
	public UserApplication update_current(String userApp_id, UserApplication userApplication) {
		Optional<UserApplication> retrieveUserApp = userApplicationRepository.findById(userApp_id);
		UserApplication newUserApp = retrieveUserApp.get();
		newUserApp.setCity(userApplication.getCity());
		newUserApp.setUser_gpa(userApplication.getUser_gpa());
		newUserApp.setYears_experience(userApplication.getYears_experience());
		newUserApp.setSkills_for_appl(userApplication.getSkills_for_appl());
		// ---  here add file info
		return userApplicationRepository.save(newUserApp);
	}
	
	//get current UserApplication by user Id:
	public UserApplication getCurrentUserApp(Long user_id) {
		User currentUser = findById(user_id);
		UserApplication myApp1 = currentUser.getUserApplication();
		return myApp1;
		
	}
}
