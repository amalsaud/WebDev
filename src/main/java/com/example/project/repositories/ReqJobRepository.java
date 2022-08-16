package com.example.project.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.project.models.Requested_Jobs;

@Repository
public interface ReqJobRepository extends CrudRepository<Requested_Jobs, Long> {
	List<Requested_Jobs> findAll();

	List<Requested_Jobs> findByJobOffer(Long job_id);
}
