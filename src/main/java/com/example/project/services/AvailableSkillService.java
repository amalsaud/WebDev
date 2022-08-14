package com.example.project.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.project.models.AvailableSkills;
import com.example.project.repositories.AvailableSkillsRepository;

@Service
public class AvailableSkillService {
	
	@Autowired
	private AvailableSkillsRepository availableSkillRepo;
	public AvailableSkillService(AvailableSkillsRepository availableSkillRepo) {
		this.availableSkillRepo = availableSkillRepo;
	}
	
	public List<AvailableSkills> allSkills(){
		List<AvailableSkills> allSkills = availableSkillRepo.findAll();
		return allSkills;
	}
	
}
