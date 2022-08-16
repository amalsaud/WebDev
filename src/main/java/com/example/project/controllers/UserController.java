package com.example.project.controllers;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.project.models.User;
import com.example.project.models.UserApplication;
import com.example.project.models.AvailableSkills;
import com.example.project.models.Job;
import com.example.project.models.Requested_Jobs;
import com.example.project.services.AvailableSkillService;
import com.example.project.services.JobService;
import com.example.project.services.UserApplicationService;
import com.example.project.services.UserService;

@Controller
public class UserController {
	@Autowired
	private UserService userServ;

	@Autowired
	private UserApplicationService userApplicationService;

	@Autowired
	private AvailableSkillService availableSkillService;

	@Autowired
	private JobService jobService;

	/*----------------------------------------------------------------------------
	Display user main page and all available jobs - Get
	----------------------------------------------------------------------------*/
	@GetMapping("/userdashboard")
	public String displayUserDashboard(Model model, HttpSession session) {
		String[] citiesList = { "Abha", "Dammam", "Riyadh", "Jeddah", "Makkah", "Madinah", "Dammam", "Taif", "Khobar",
				"Tabuk", "Dhahran", "Najran" };
		model.addAttribute("citiesList", citiesList);

		Long idLong = (Long) session.getAttribute("userId");
		User user = userServ.findById(idLong);
		model.addAttribute("user", user);
		model.addAttribute("newApplication", new UserApplication());

		List<AvailableSkills> allSkills = availableSkillService.allSkills();
		model.addAttribute("allSkills", allSkills);

		List<Requested_Jobs> requestedJobs = user.getUser_requests();
		List<Job> jobs = jobService.getNotAppliedJobs(idLong);
		model.addAttribute("jobs", jobs);
		model.addAttribute("requestedJobs", requestedJobs);

		return "userdashboard.jsp";
	}

	/*----------------------------------------------------------------------------
	Apply for a specific job - POST
	----------------------------------------------------------------------------*/
	@PostMapping("/jobs/apply/{job_id}")
	public String apply(@PathVariable("job_id") Long job_id, @Valid @ModelAttribute("apply") Requested_Jobs apply,
			BindingResult result, RedirectAttributes redirectAttributes, HttpSession session, Model model) {
		Long idLong = (Long) session.getAttribute("userId");
		User user = userServ.findById(idLong);
		model.addAttribute("user", user);
		if (session.getAttribute("userId") == null) {
			return "redirect:/";
		} else {
			if (result.hasErrors()) {
				redirectAttributes.addFlashAttribute("apply", apply);
				redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.apply", result);

				return "redirect:/userdashboard";
			} else if (user.getUserApplication() == null) {
				model.addAttribute("newApplication", new UserApplication());
				List<AvailableSkills> allSkills = availableSkillService.allSkills();
				model.addAttribute("allSkills", allSkills);
				redirectAttributes.addFlashAttribute("error", "Fill an application to apply");
				return "redirect:/userdashboard";
			} else {
				// get user_id + job_id + "pending"
				Long user_id = (Long) session.getAttribute("userId");
				jobService.apply(user_id, job_id);
				redirectAttributes.addFlashAttribute("success", "You applied for the job successfully");
				return "redirect:/userdashboard";
			}
		}
	}

	/*----------------------------------------------------------------------------
	Display user account - Get
	----------------------------------------------------------------------------*/
	@GetMapping("/account")
	public String displayUserAccount(Model model, HttpSession session) {
		String[] citiesList = { "Abha", "Dammam", "Riyadh", "Jeddah", "Makkah", "Madinah", "Dammam", "Taif", "Khobar",
				"Tabuk", "Dhahran", "Najran" };
		model.addAttribute("citiesList", citiesList);

		Long idLong = (Long) session.getAttribute("userId");
		User user = userServ.findById(idLong);
		model.addAttribute("user", user);
		if (user.getUserApplication() == null) {
			model.addAttribute("newApplication", new UserApplication());
			List<AvailableSkills> allSkills = availableSkillService.allSkills();
			model.addAttribute("allSkills", allSkills);

			return "account.jsp";
		} else {
			UserApplication current_userApp = user.getUserApplication();
			model.addAttribute("current_userApp", current_userApp);
			model.addAttribute("user_skills", current_userApp.getSkills_for_appl());

			return "alreadyHaveApplication.jsp";
		}

	}

	/*----------------------------------------------------------------------------
	Create user information - POST
	----------------------------------------------------------------------------*/
	@PostMapping("/apply")
	public String create_userApp(@Valid @ModelAttribute("newUserApp") UserApplication newUserApp,
			BindingResult bindingResult, RedirectAttributes redirectAttributes, HttpSession session) {
		if (bindingResult.hasErrors()) {
			redirectAttributes.addFlashAttribute("newUserApp", newUserApp);
			redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.newUserApp",
					bindingResult);
			return "redirect:/account";
		}
		// no errors --> save and redirect to main page:
		if (newUserApp.getSkills_for_appl().isEmpty()) {
			redirectAttributes.addFlashAttribute("error", "Please Enter valid data");

			return "redirect:/account";
		}
		Long creator_id = (Long) session.getAttribute("userId");
		UserApplication mynewApp = userApplicationService.create_userApp(creator_id, newUserApp);
		session.setAttribute("appId", mynewApp.getId());
		redirectAttributes.addFlashAttribute("success", "The User Application has been created successfully");
		return "upload.jsp";
	}

	/*----------------------------------------------------------------------------
	Display edit user application page - Get
	----------------------------------------------------------------------------*/
	@GetMapping("/edit")
	public String editView_userApp(Model model, HttpSession session) {
		String[] citiesList = { "Abha", "Dammam", "Riyadh", "Jeddah", "Makkah", "Madinah", "Dammam", "Taif", "Khobar",
				"Tabuk", "Dhahran", "Najran" };
		model.addAttribute("citiesList", citiesList);
		Long user_id = (Long) session.getAttribute("userId");
		UserApplication current_userApp = userApplicationService.getCurrentUserApp(user_id);
		if (session.getAttribute("userId") == null) {
			return "redirect:/";
		} else if (current_userApp == null) {
			model.addAttribute("newApplication", new UserApplication());
			List<AvailableSkills> allSkills = availableSkillService.allSkills();
			model.addAttribute("allSkills", allSkills);
			return "account.jsp";
		} else {
			if (!model.containsAttribute("current_userApp")) {

				// save userApp_id in session
				String userAppId = current_userApp.getId();

				session.setAttribute("userAppId", userAppId);
				model.addAttribute("current_userApp", current_userApp);
				List<AvailableSkills> allSkills = availableSkillService.allSkills();
				model.addAttribute("allSkills", allSkills);

			}
			return "redirect:/viewapp";
		}
	}

	/*----------------------------------------------------------------------------
	Edit user information - PUT
	----------------------------------------------------------------------------*/
	@PutMapping(value = "/update")
	public String saveEdit_userApp(@Valid @ModelAttribute("current_userApp") UserApplication current_userApp,
			BindingResult bindingResult, RedirectAttributes redirectAttributes, HttpSession session) {
		// check if there's an error --> redirect to display edit page
		if (bindingResult.hasErrors()) {
			redirectAttributes.addFlashAttribute("current_userApp", current_userApp);
			redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.current_userApp",
					bindingResult);
			return "redirect:/edit";
		}
		// no errors --> update the item then redirect to main page
		String idLong = (String) session.getAttribute("appId");
		userApplicationService.update_current(idLong, current_userApp);
		redirectAttributes.addFlashAttribute("success", "The User Application has been Updated successfully");
		return "editUpload.jsp";
	}

	/*----------------------------------------------------------------------------
	Display User application info if it is exist - Get
	----------------------------------------------------------------------------*/
	@GetMapping("/viewapp")
	public String view_userApp(Model model, HttpSession session) {
		String[] citiesList = { "Abha", "Dammam", "Riyadh", "Jeddah", "Makkah", "Madinah", "Dammam", "Taif", "Khobar",
				"Tabuk", "Dhahran", "Najran" };
		model.addAttribute("citiesList", citiesList);
		List<AvailableSkills> allSkills = availableSkillService.allSkills();
		model.addAttribute("allSkills", allSkills);
		if (session.getAttribute("userId").equals(null)) {
			return "redirect:/";
		} else {
			if (!model.containsAttribute("current_userApp")) {
				// get user_id and fetch the UserApplication
				Long currentUser_id = (Long) session.getAttribute("userId");
				UserApplication current_userApp = userApplicationService.getCurrentUserApp(currentUser_id);
				model.addAttribute("current_userApp", current_userApp);
				model.addAttribute("user_skills", current_userApp.getSkills_for_appl());

			}

			return "diplaysUserApplication.jsp";
		}
	}

	/*----------------------------------------------------------------------------
	Filter jobs by city - POST
	----------------------------------------------------------------------------*/
	@PostMapping("/findbycity")
	public String findbycity(Model model, @RequestParam(value = "city") String city) {
		List<Job> jobsByCity = jobService.findByLocation(city);
		model.addAttribute("jobs", jobsByCity);
		return "userdashboard.jsp";
	}

	/*----------------------------------------------------------------------------
	Display a warning page before deleting user account - GET
	----------------------------------------------------------------------------*/
	@GetMapping("/warning")
	public String warning() {
		return "warning.jsp";
	}

	/*----------------------------------------------------------------------------
	Delete User Account - DELETE
	----------------------------------------------------------------------------*/
	@RequestMapping(value = "/delete", method = RequestMethod.DELETE)
	public String deleteAccount(RedirectAttributes redirectAttributes, HttpSession session) {
		Long currentUser_id = (Long) session.getAttribute("userId");
		userServ.deleteAccount(currentUser_id);
		redirectAttributes.addFlashAttribute("success", "your Account has beed deleted");
		return "redirect:/";
	}

}
