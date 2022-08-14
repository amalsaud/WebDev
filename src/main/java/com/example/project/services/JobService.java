package com.example.project.services;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Sort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.project.models.AvailableSkills;
import com.example.project.models.Job;
import com.example.project.models.Requested_Jobs;
import com.example.project.models.User;
import com.example.project.repositories.AvailableSkillsRepository;
import com.example.project.repositories.JobRepository;
import com.example.project.repositories.ReqJobRepository;
import com.example.project.repositories.UserRepository;

@Service
public class JobService {
	@Autowired
	private ReqJobRepository reqJobReop;
	@Autowired
	private UserService userService;
	
	@Autowired
	private JobRepository jobRepository;
	@Autowired
	private AvailableSkillsRepository availableSkillsRepository;
	@Autowired
	private UserRepository userRepo;
	// ================ Create New Job =================================
	public Job createJob(Job job) {

		return jobRepository.save(job);

	}

	// ================== get all available skills ========================
	public List<AvailableSkills> allSkills() {
		return availableSkillsRepository.findAll();
	}

	// ====================== find a job =====================================
	// find an event
	public Job findjob(Long id) {
		Optional<Job> optionaljob = jobRepository.findById(id);
		if (optionaljob.isPresent()) {
			return optionaljob.get();
		} else {
			return null;
		}
	}

	// ============================ get a list of all jobs ==================
	public List<Job> getAlljobs() {
		return jobRepository.findAll(Sort.by("createdAt").descending());

	}
	// ============================ search ==================

	public List<Job> findByLocation(String city){
		return jobRepository.findByLocation(city);
	}
	// ========================= edit a job ==================================

	public Job updateJob(Job job, Long id) {

		Optional<Job> optionalJob = jobRepository.findById(id);
		if (optionalJob.isPresent()) {

			Job updatejob = optionalJob.get();
			updatejob.setTitle(job.getTitle());
			updatejob.setDescription(job.getDescription());
			updatejob.setExperience_Required(job.getExperience_Required());
			updatejob.setGpa_wanted(job.getGpa_wanted());
			updatejob.setSkills_for_Job(job.getSkills_for_Job());
			updatejob.setLocation(job.getLocation());

			return jobRepository.save(updatejob);
		} else {
			return null;
		}
	}

	// ======================= delete job ===============================
	public void deletejob(Long id) {
		Job job = findjob(id);
		jobRepository.delete(job);
	}
	// ===============================================
	@Autowired
	private ReqJobRepository reqJobRepo;
	
	
	// get current request by Id:
	public Requested_Jobs get_currentReq(Long req_id) {
		Optional<Requested_Jobs> current_req = reqJobRepo.findById(req_id);
		return current_req.get();
	}
	
	// get User from the Request:
	public User get_user(Long req_id) {
		Requested_Jobs current_req = get_currentReq(req_id);
		User theuser = current_req.getUser_applied();
		return theuser;
	}
	
	
	// change status:
	public Requested_Jobs change_ReqStatus(Long req_id, String state) {
		Requested_Jobs req_toUpdate = get_currentReq(req_id);
		req_toUpdate.setStatus(state);
		return reqJobRepo.save(req_toUpdate);
	}
	// ==================== apply to job ===========================
		public void apply(Long user_id, Long job_id) {
			Optional<User> currentUser = userRepo.findById(user_id);
			Optional<Job> currentJob = jobRepository.findById(job_id);
			Requested_Jobs add_requestJob = new Requested_Jobs(currentUser.get(), currentJob.get(), "pending");
			reqJobReop.save(add_requestJob);
		}
}
