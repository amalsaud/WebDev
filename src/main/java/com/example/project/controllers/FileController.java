package com.example.project.controllers;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.project.models.User;
import com.example.project.models.UserApplication;
import com.example.project.repositories.UserRepository;
import com.example.project.message.ResponseFile;
import com.example.project.models.AvailableSkills;
import com.example.project.models.Job;
import com.example.project.models.LoginUser;
import com.example.project.models.Requested_Jobs;
import com.example.project.services.AvailableSkillService;
import com.example.project.services.FileStorageService;
import com.example.project.services.JobService;
import com.example.project.services.ReqJobService;
import com.example.project.services.UserApplicationService;
import com.example.project.services.UserService;

@Controller
@CrossOrigin("http://localhost:8080")
public class FileController {
	@Autowired
	private FileStorageService storageService;
	@Autowired
	private ReqJobService reqJobService;
	@Autowired
	private UserService userServ;

	@Autowired
	private UserApplicationService userApplicationService;

	@Autowired
	private AvailableSkillService availableSkillService;

	@Autowired
	private JobService jobService;
	@Autowired
	private JavaMailSender mailSender;
	@Autowired

	private UserRepository userRepo;
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
		if (user == null || user.getId()==null) {
			System.out.println("Running into errors validation #2");
			redirectAttributes.addFlashAttribute("newLogin", newLogin);
			redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.newLogin", result);
			return "login.jsp";
		} else {
			if(user.getUser_role().equals("HR")) {
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
	Display user dashboard and all jobs - Get
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

		List<Job> jobs = jobService.getAlljobs();
		model.addAttribute("jobs", jobs);
		return "userdashboard.jsp";
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
	Display edit page - Get
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

	@PostMapping("/upload")
	public String uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("file2") MultipartFile file2,
			HttpSession session, RedirectAttributes redirectAttributes) {
		try {
			String idLong = (String) session.getAttribute("appId");
			storageService.store(file, file2, idLong);
			redirectAttributes.addFlashAttribute("success", "File uploaded successfully");
			return "redirect:/account";
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", "File size can not be > 2 MB");
			return "upload.jsp";
		}
	}

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

	@GetMapping("/files")
	public ResponseEntity<List<ResponseFile>> getListFiles() {
		List<ResponseFile> files = storageService.getAllFiles().map(dbFile -> {
			String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/files/")
					.path(dbFile.getId()).toUriString();
			return new ResponseFile(dbFile.getCvFileName(), fileDownloadUri, dbFile.getCvFileType(),
					dbFile.getCvFileData().length);
		}).collect(Collectors.toList());
		return ResponseEntity.status(HttpStatus.OK).body(files);
	}

	@GetMapping("/files/{id}")
	public ResponseEntity<byte[]> getFile(@PathVariable String id) {
		UserApplication fileDB = storageService.getFile(id);
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDB.getCvFileName() + "\"")
				.body(fileDB.getCvFileData());
	}
	@GetMapping("/files/cer/{id}")
	public ResponseEntity<byte[]> getCerFile(@PathVariable String id) {
		UserApplication fileDB = storageService.getFile(id);
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDB.getCertFileName() + "\"")
				.body(fileDB.getCertFileData());
	}

	// --- update UserApplication:
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

	/*------------------------------------------------------------------------------------------------------------------------
	------------------------------------------------------------ADMIN---------------------------------------------------------
	------------------------------------------------------------------------------------------------------------------------*/
	@GetMapping("/hrindex")
	public String displayHRIndex(Model model) {
		List<Job> jobs = jobService.getAlljobs();
		model.addAttribute("jobs", jobs);
		return "hrindex.jsp";
	}

	// ======================== job details =========================
	@GetMapping("jobs/{id}")
	public String jodinfo(@PathVariable("id") Long id, Model model) {
		Job job = jobService.findjob(id);
		model.addAttribute("job", job);
		return "jobdetails.jsp";
	}

	// ================= create new job - get - =======================
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

	// ================= create new job - post - ====================
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

	// ====================== edit a job - get - ===========================
	@GetMapping(value = "/jobs/edit/{id}")
	public String edit(@PathVariable("id") Long id, Model model) {

		Job job = jobService.findjob(id);
		List<AvailableSkills> skills = jobService.allSkills();

		if (!model.containsAttribute("job")) {
			model.addAttribute("job", job);
		}
		if (!model.containsAttribute("skills")) {
			model.addAttribute("skills", skills);
		}

		return "editjob.jsp";
	}

	// ====================== edit a job - post - ===========================
	@RequestMapping(value = "/jobs/edit/{id}", method = RequestMethod.PUT)
	public String update(@PathVariable("id") Long id, @Valid @ModelAttribute("job") Job job, BindingResult result,
			RedirectAttributes redirectAttributes, Model model) {

		if (result.hasErrors()) {
			redirectAttributes.addFlashAttribute("job", job);
			redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.job", result);
			return "redirect:/jobs/edit/{id}";
		} else {
			jobService.updateJob(job, id);
			return "redirect:/jobs";
		}
	}

	// ================= delete a job ==================================
	@DeleteMapping(value = "/delete/{id}")
	public String destroy(@PathVariable("id") Long id) {
		jobService.deletejob(id);
		return "redirect:/hrindex";
	}

	/*----------------------------------------------------------------------------
	Delete User Account
	----------------------------------------------------------------------------*/
	@RequestMapping(value = "/delete", method = RequestMethod.DELETE)
	public String deleteAccount(RedirectAttributes redirectAttributes, HttpSession session) {
		Long currentUser_id = (Long) session.getAttribute("userId");
		userServ.deleteAccount(currentUser_id);
		redirectAttributes.addFlashAttribute("success", "your Account has beed deleted");
		return "redirect:/";
	}

	/*----------------------------------------------------------------------------
	Warning before deleting account
	----------------------------------------------------------------------------*/
	@GetMapping("/warning")
	public String warning() {
		return "warning.jsp";
	}

	/*----------------------------------------------------------------------------
	Logout and delete the session
	----------------------------------------------------------------------------*/
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/";
	}

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
				String idLong = (String) session.getAttribute("appId");
				UserApplication current_userApp = userApplicationService.findById(idLong);
				model.addAttribute("current_userApp", current_userApp);
				model.addAttribute("user_skills", current_userApp.getSkills_for_appl());

			}

			return "diplaysUserApplication.jsp";
		}
	}

	@PostMapping("/editfiles")
	public String editfiles(@RequestParam("file") MultipartFile file, @RequestParam("file2") MultipartFile file2,
			HttpSession session, RedirectAttributes redirectAttributes) {
		try {
			String idLong = (String) session.getAttribute("appId");
			storageService.store(file, file2, idLong);
			redirectAttributes.addFlashAttribute("success", "File uploaded successfully");
			return "redirect:/account";
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", "File size can not be > 2 MB");
			return "editUpload.jsp";
		}
	}

	// aplly for a job
	@PostMapping("/jobs/apply/{job_id}")
	public String apply(@PathVariable("job_id") Long job_id, @Valid @ModelAttribute("apply") Requested_Jobs apply,
			BindingResult result, RedirectAttributes redirectAttributes, HttpSession session,Model model) {
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
			} 		
			else if (user.getUserApplication() == null) {
				model.addAttribute("newApplication", new UserApplication());
				List<AvailableSkills> allSkills = availableSkillService.allSkills();
				model.addAttribute("allSkills", allSkills);
				redirectAttributes.addFlashAttribute("error", "Fill an application to apply");
				return "redirect:/userdashboard";}
				else {
					// get user_id + job_id + "pending"
					Long user_id = (Long) session.getAttribute("userId");
					jobService.apply(user_id, job_id);
					redirectAttributes.addFlashAttribute("success", "You applied for the job successfully");
					return "redirect:/userdashboard";
				}
			}
		}

		// request on jobs
		@GetMapping(value = "/displayrequests")
		public String displayRequestedJobs(HttpSession session, Model model) {
			List<Job> all_jobs = jobService.getAlljobs();
			model.addAttribute("jobs", all_jobs);
			return "allApplications.jsp";
		}

		// accept user:
		@RequestMapping(value = "/acceptapp/{req_id}", method = RequestMethod.PUT)
		public String acceptUser(@PathVariable("req_id") Long req_id, RedirectAttributes redirectAttributes) {
			Requested_Jobs updated_req = reqJobService.change_ReqStatus(req_id, "Accept");
			User theuser = reqJobService.get_user(req_id);
			String from = "fatimah.se521@gmail.com";
			String to = (String) theuser.getEmail();
			// process of sending:
			SimpleMailMessage message = new SimpleMailMessage();
			message.setFrom(from);
			message.setTo(to);
			message.setSubject("Acceptance Message");
			message.setText("Congratulations! You are accepted for this position");
			mailSender.send(message);
			redirectAttributes.addFlashAttribute("success", "The Email has sent to user with updated status");
			return "redirect:/displayrequests";
		}

		// reject user:
		@RequestMapping(value = "/rejectapp/{req_id}", method = RequestMethod.PUT)
		public String rejectUser(@PathVariable("req_id") Long req_id, RedirectAttributes redirectAttributes) {
			Requested_Jobs updated_req = reqJobService.change_ReqStatus(req_id, "Reject");
			User theuser = reqJobService.get_user(req_id);
			String from = "fatimah.se521@gmail.com";
			String to = (String) theuser.getEmail();
			// process of sending:
			SimpleMailMessage message = new SimpleMailMessage();
			message.setFrom(from);
			message.setTo(to);
			message.setSubject("Reject Message");
			message.setText("Unfortunately! You are not accepted for this position");
			mailSender.send(message);
			redirectAttributes.addFlashAttribute("success", "The Email has sent to user with updated status");
			return "redirect:/displayrequests";
		}
		@PostMapping("/findbycity")
		public String findbycity(Model model, @RequestParam(value = "city") String city) {
			List<Job> jobsByCity = jobService.findByLocation(city);
			model.addAttribute("jobs",jobsByCity);
			return "userdashboard.jsp";
		}
		@GetMapping("/userprofile/{userId}")
		public String userProfile(@PathVariable("userId") Long userId,
				Model model) {
			User user = userServ.findById(userId);
			UserApplication current_userApp = user.getUserApplication();
			model.addAttribute("current_userApp", current_userApp);
			model.addAttribute("user_skills", current_userApp.getSkills_for_appl());

			return "viewtest.jsp";

		}

	}