package com.example.project.repositories;
import org.springframework.data.domain.Sort;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.project.models.Job;
@Repository
public interface JobRepository extends CrudRepository<Job, Long> {
List<Job> findAll();
List<Job> findByLocation(String city);
List<Job> findAll(Sort sort);
}

