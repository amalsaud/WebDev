package com.example.project.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.project.models.AvailableSkills;

@Repository
public interface AvailableSkillsRepository extends CrudRepository<AvailableSkills, Long> {
	List<AvailableSkills> findAll();
}
