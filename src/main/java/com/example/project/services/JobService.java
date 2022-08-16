package com.example.project.services;

import java.util.ArrayList;
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
	private ReqJobRepository reqJobRepo;

	@Autowired
	private JobRepository jobRepository;
	@Autowired
	private AvailableSkillsRepository availableSkillsRepository;
	@Autowired
	private UserRepository userRepo;

	/*----------------------------------------------------------------------------
	CREATES A NEW JOB
	----------------------------------------------------------------------------*/
	public Job createJob(Job job) {
		return jobRepository.save(job);
	}

	/*----------------------------------------------------------------------------
	GET ALL SKILLS
	----------------------------------------------------------------------------*/
	public List<AvailableSkills> allSkills() {
		return availableSkillsRepository.findAll();
	}

	/*----------------------------------------------------------------------------
	FIND A JOB BY ID
	----------------------------------------------------------------------------*/
	public Job findjob(Long id) {
		Optional<Job> optionaljob = jobRepository.findById(id);
		if (optionaljob.isPresent()) {
			return optionaljob.get();
		} else {
			return null;
		}
	}

	/*----------------------------------------------------------------------------
	GET ALL JOBS
	----------------------------------------------------------------------------*/
	public List<Job> getAlljobs() {
		return jobRepository.findAll(Sort.by("createdAt").descending());

	}

	/*----------------------------------------------------------------------------
	GETS ALL AVAILABLE JOBS (NOT APPLIED FOR)
	----------------------------------------------------------------------------*/
	public List<Job> getNotAppliedJobs(Long userId) {
		User user = userRepo.findById(userId).get();
		List<Job> notAppliedJobs = new ArrayList<Job>();
		for (Job job : getAlljobs()) {
			Boolean jobInAppliedJobs = false;
			for (Requested_Jobs jobsApplied : user.getUser_requests()) {
				if (job.getId() == jobsApplied.getjobOffer().getId()) {
					jobInAppliedJobs = true;
					break;
				}
			}
			if (!jobInAppliedJobs) {
				notAppliedJobs.add(job);
			}
		}

		return notAppliedJobs;
	}

	/*----------------------------------------------------------------------------
	SEARCH A JOB BY CITY
	----------------------------------------------------------------------------*/
	public List<Job> findByLocation(String city) {
		return jobRepository.findByLocation(city);
	}

	/*----------------------------------------------------------------------------
	GET CURRENT JOB REQUEST
	----------------------------------------------------------------------------*/
	// get current request by Id:
	public Requested_Jobs get_currentReq(Long req_id) {
		Optional<Requested_Jobs> current_req = reqJobRepo.findById(req_id);
		return current_req.get();
	}

	/*----------------------------------------------------------------------------
	GET USER FROM JOB REQUEST
	----------------------------------------------------------------------------*/
	public User get_user(Long req_id) {
		Requested_Jobs current_req = get_currentReq(req_id);
		User theuser = current_req.getUser_applied();
		return theuser;
	}

	/*----------------------------------------------------------------------------
	CHANGE STATUS
	----------------------------------------------------------------------------*/
	public Requested_Jobs change_ReqStatus(Long req_id, String state) {
		Requested_Jobs req_toUpdate = get_currentReq(req_id);
		req_toUpdate.setStatus(state);
		return reqJobRepo.save(req_toUpdate);
	}

	/*----------------------------------------------------------------------------
	APPLY FOR A SPECEFIC JOB
	----------------------------------------------------------------------------*/
	public void apply(Long user_id, Long job_id) {
		Optional<User> currentUser = userRepo.findById(user_id);
		Optional<Job> currentJob = jobRepository.findById(job_id);
		Requested_Jobs add_requestJob = new Requested_Jobs(currentUser.get(), currentJob.get(), "pending");
		reqJobReop.save(add_requestJob);
	}
}
