package com.example.project.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.project.models.Requested_Jobs;
import com.example.project.models.User;
import com.example.project.repositories.ReqJobRepository;

@Service
public class ReqJobService {
	@Autowired
	private ReqJobRepository reqJobRepo;

	/*----------------------------------------------------------------------------
	GET CURRENT JOB REQUEST BY REQUEST ID, LONG
	----------------------------------------------------------------------------*/
	public Requested_Jobs get_currentReq(Long req_id) {
		Optional<Requested_Jobs> current_req = reqJobRepo.findById(req_id);
		return current_req.get();
	}

	/*----------------------------------------------------------------------------
	GET CURRENT JOB REQUEST BY REQUEST ID, LIST
	----------------------------------------------------------------------------*/ public List<Requested_Jobs> getAppRequests(
			Long id) {
		List<Requested_Jobs> current_reqs = reqJobRepo.findByJobOffer(id);
		return current_reqs;
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
}
