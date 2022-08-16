package com.example.project.controllers;

import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.project.models.User;
import com.example.project.models.LoginUser;
import com.example.project.services.UserService;

@Controller
public class AuthController {
	@Autowired
	private UserService userServ;

	@Autowired
	private UserService userService;

	/*----------------------------------------------------------------------------
		Display main page - GET
	----------------------------------------------------------------------------*/
	@GetMapping("/")
	public String index() {
		return "index.jsp";
	}

	/*----------------------------------------------------------------------------
		Display registration form - GET
	----------------------------------------------------------------------------*/
	@GetMapping("/reg")
	public String reg(Model model) {
		model.addAttribute("newUser", new User());
		return "register.jsp";
	}

	/*----------------------------------------------------------------------------
		Process registration request - POST
	----------------------------------------------------------------------------*/
	@PostMapping("/register")
	public String register(@Valid @ModelAttribute("newUser") User newUser, BindingResult result, Model model,
			RedirectAttributes redirectAttributes) {
		// send the instance and the result
		userServ.register(newUser, result);
		if (result.hasErrors()) {
			model.addAttribute("newLogin", new LoginUser());
			return "register.jsp";
		}
		return "redirect:/log";
	}

	/*----------------------------------------------------------------------------
	Display login form - GET
	----------------------------------------------------------------------------*/
	@GetMapping("/log")
	public String login(Model model) {
		if (!model.containsAttribute("newUser")) {
			model.addAttribute("newUser", new User());
		}
		if (!model.containsAttribute("newLogin")) {
			model.addAttribute("newLogin", new LoginUser());
		}

		return "login.jsp";
	}

	/*----------------------------------------------------------------------------
	Process login request - POST
	----------------------------------------------------------------------------*/

	@PostMapping("/login")
	public String loginUser(@Valid @ModelAttribute("newLogin") LoginUser newLogin, Model model, BindingResult result,
			RedirectAttributes redirectAttributes, HttpSession session) {
		if (result.hasErrors()) {
			System.out.println("Running into errors validation #1");
			redirectAttributes.addFlashAttribute("newLogin", newLogin);
			redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.newLogin", result);
			return "login.jsp";
		}

		User user = userService.login(newLogin, result);
		if (user == null || user.getId() == null) {
			System.out.println("Running into errors validation #2");
			redirectAttributes.addFlashAttribute("newLogin", newLogin);
			redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.newLogin", result);
			return "login.jsp";
		} else {
			if (user.getUser_role().equals("HR")) {
				session.setAttribute("userId", user.getId());
				session.setAttribute("loggedFname", user.getFirstName());
				session.setAttribute("loggedLname", user.getLastName());
				return "redirect:/hrindex";
			}
			session.setAttribute("userId", user.getId());
			session.setAttribute("loggedFname", user.getFirstName());
			session.setAttribute("loggedLname", user.getLastName());
			return "redirect:/userdashboard";
		}
	}

	/*----------------------------------------------------------------------------
	Process logout request - DELETE
	----------------------------------------------------------------------------*/
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/";
	}
}