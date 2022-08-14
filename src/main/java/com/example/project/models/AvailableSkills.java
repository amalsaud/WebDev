package com.example.project.models;

import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name="availableSkills")
public class AvailableSkills {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	
    @NotNull
    private String skill_name;

    
    //////////////////////////////////////RelatioShip /////////////////////////////////////////
    
    
 // (connection with Skill table)
    // ----- ManyToMany relation: many skills required by many jobs
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "availableSkills_job", 
        joinColumns = @JoinColumn(name = "availableSkills_id"), 
        inverseJoinColumns = @JoinColumn(name = "job_id")
    )
    private List<Job> jobsAskForSkill;
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "availableSkills_userApp", 
        joinColumns = @JoinColumn(name = "availableSkills_id"), 
        inverseJoinColumns = @JoinColumn(name = "userApplication_id")
    )
    private List<UserApplication> userAppHaveSkills;
    
    // This will not allow the createdAt column to be updated after creation
    @Column(updatable=false)
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date createdAt;
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date updatedAt;
    @PrePersist
	protected void onCreate(){
		this.createdAt = new Date();
	}
	@PreUpdate
	protected void onUpdate(){
		this.updatedAt = new Date();
	}
	
	
	//constructor + getters and setters:
	public AvailableSkills() {
		//
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public List<Job> getJobsAskForSkill() {
		return jobsAskForSkill;
	}
	public void setJobsAskForSkill(List<Job> jobsAskForSkill) {
		this.jobsAskForSkill = jobsAskForSkill;
	}
	public List<UserApplication> getUserAppHaveSkills() {
		return userAppHaveSkills;
	}
	public void setUserAppHaveSkills(List<UserApplication> userAppHaveSkills) {
		this.userAppHaveSkills = userAppHaveSkills;
	}
	public String getSkill_name() {
		return skill_name;
	}
	public void setSkill_name(String skill_name) {
		this.skill_name = skill_name;
	}
	
	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	public Date getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}
	
}
