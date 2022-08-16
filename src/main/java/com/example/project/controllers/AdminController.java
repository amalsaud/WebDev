package com.example.project.controllers;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.project.models.User;
import com.example.project.models.UserApplication;
import com.example.project.models.AvailableSkills;
import com.example.project.models.Job;
import com.example.project.services.JobService;
import com.example.project.services.ReqJobService;
import com.example.project.services.UserService;

@Controller
public class AdminController {
	@Autowired
	private ReqJobService reqJobService;

	@Autowired
	private JobService jobService;

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private UserService userServ;

	/*----------------------------------------------------------------------------
	Display HR main page - GET
	----------------------------------------------------------------------------*/
	@GetMapping("/hrindex")
	public String displayHRIndex(Model model) {
		List<Job> jobs = jobService.getAlljobs();
		model.addAttribute("jobs", jobs);
		return "hrindex.jsp";
	}

	/*----------------------------------------------------------------------------
	Display create a job page - GET
	----------------------------------------------------------------------------*/
	@GetMapping("/jobs/new")
	public String createjob(Model model, HttpSession session) {
		String[] citiesList = { "Abha", "Dammam", "Riyadh", "Jeddah", "Makkah", "Madinah", "Dammam", "Taif", "Khobar",
				"Tabuk", "Dhahran", "Najran" };
		model.addAttribute("citiesList", citiesList);

		List<AvailableSkills> skills = jobService.allSkills();

		if (!model.containsAttribute("newJob")) {
			model.addAttribute("newJob", new Job());
		}
		if (!model.containsAttribute("skills")) {
			model.addAttribute("skills", skills);
		}
		return "newjob.jsp";
	}

	/*----------------------------------------------------------------------------
	Process create a job - POST
	----------------------------------------------------------------------------*/
	@PostMapping("/jobs/new")
	public String createjob(@Valid @ModelAttribute("newJob") Job newJob, BindingResult result,
			RedirectAttributes redirectAttributes, HttpSession session) {
		if (result.hasErrors()) {
			redirectAttributes.addFlashAttribute("newJob", newJob);
			redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.newJob", result);
			return "redirect:/jobs/new";
		} else if (newJob.getSkills_for_Job().isEmpty()) {
			redirectAttributes.addFlashAttribute("error", "Please Enter valid data");

			return "redirect:/jobs/new";
		} else {
			jobService.createJob(newJob);
			return "redirect:/hrindex";
		}
	}

	/*----------------------------------------------------------------------------
	Display all users applications - GET
	----------------------------------------------------------------------------*/
	@GetMapping(value = "/displayrequests")
	public String displayRequestedJobs(HttpSession session, Model model) {
		List<Job> all_jobs = jobService.getAlljobs();
		model.addAttribute("jobs", all_jobs);
		return "allApplications.jsp";
	}

	/*----------------------------------------------------------------------------
	Display applicant profile - GET
	----------------------------------------------------------------------------*/
	@GetMapping("/userprofile/{jobId}/{reqId}/{userId}")
	public String userProfile(@PathVariable("userId") Long userId, @PathVariable("jobId") Long jobId,
			@PathVariable("reqId") Long reqId, Model model, HttpSession session) {
		User user = userServ.findById(userId);
		UserApplication current_userApp = user.getUserApplication();

		model.addAttribute("current_userApp", current_userApp);
		model.addAttribute("user_skills", current_userApp.getSkills_for_appl());
		model.addAttribute("JobReqId", jobId);
		model.addAttribute("reqId", reqId);
		session.setAttribute("jobId", jobId);
		return "viewtest.jsp";
	}

	/*----------------------------------------------------------------------------
	Process accepting a user application to a specific job - PUT
	----------------------------------------------------------------------------*/
	@RequestMapping(value = "/acceptapp/{req_id}", method = RequestMethod.PUT)
	public String acceptUser(@PathVariable("req_id") Long req_id, RedirectAttributes redirectAttributes, Model model,
			HttpSession session) {
		reqJobService.change_ReqStatus(req_id, "Accept");
		User theuser = reqJobService.get_user(req_id);
		String from = "fatimah.se521@gmail.com";
		String to = (String) theuser.getEmail();
		Long jobId = (long) session.getAttribute("jobId");
		Job job = jobService.findjob(jobId);
		// process of sending:
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(from);
		message.setTo(to);
		message.setSubject("WebDev");
		message.setText("Dear " + theuser.getFirstName() + "\n\nCongratulations! You are accepted for " + job.getTitle()
				+ " position in WebDev");
		mailSender.send(message);
		redirectAttributes.addFlashAttribute("success", "The Email has sent to user with updated status");
		return "redirect:/displayrequests";
	}

	/*----------------------------------------------------------------------------
	Process rejecting a user application to a specific job - PUT
	----------------------------------------------------------------------------*/
	@RequestMapping(value = "/rejectapp/{req_id}", method = RequestMethod.PUT)
	public String rejectUser(@PathVariable("req_id") Long req_id, RedirectAttributes redirectAttributes, Model model,
			HttpSession session) {
		reqJobService.change_ReqStatus(req_id, "Reject");
		Long jobId = (long) session.getAttribute("jobId");
		Job job = jobService.findjob(jobId);
		User theuser = reqJobService.get_user(req_id);
		String from = "fatimah.se521@gmail.com";
		String to = (String) theuser.getEmail();
		// process of sending:
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(from);
		message.setTo(to);
		message.setSubject("WebDev");
		message.setText("Dear " + theuser.getFirstName() + "\n\nUnfortunately! You are not accepted for "
				+ job.getTitle() + " position in WebDev");
		mailSender.send(message);
		redirectAttributes.addFlashAttribute("success", "The Email has sent to user with updated status");
		return "redirect:/displayrequests";
	}

}
