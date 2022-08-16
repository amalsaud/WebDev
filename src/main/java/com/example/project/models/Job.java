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
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "job")
public class Job {

	/*----------------------------------------------------------------------------
	TABLE
	----------------------------------------------------------------------------*/
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotEmpty(message = "The title shouldn't be Empty")
	@Size(min = 3, max = 200, message = "The title shouldn't be less than 3 characters")
	private String title;

	@NotEmpty(message = "The description shouldn't be Empty")
	@Size(min = 3, max = 200, message = "The description shouldn't be less than 3 characters")
	private String description;

	@NotEmpty(message = "The location shouldn't be Empty")
	@Size(min = 3, max = 200, message = "The location shouldn't be less than 3 characters")
	private String location;

	@NotNull(message = "The experience field shouldn't be Empty")
	@Min(0)
	private int experience_Required;

	@NotNull(message = "The gpa field field shouldn't be Empty")
	@Min(1)
	@Max(5)
	private double gpa_wanted;

	@Column(updatable = false)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date createdAt;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date updatedAt;

	@PrePersist
	protected void onCreate() {
		this.createdAt = new Date();
	}

	@PreUpdate
	protected void onUpdate() {
		this.updatedAt = new Date();
	}

	/*----------------------------------------------------------------------------
	RELATIONS
	----------------------------------------------------------------------------*/
	// (connection with User table)
	// ManyToMany relation: many jobs have many users
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "user_job", joinColumns = @JoinColumn(name = "job_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
	private List<User> usersApplied;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "availableSkills_job", joinColumns = @JoinColumn(name = "job_id"), inverseJoinColumns = @JoinColumn(name = "availableSkills_id"))
	private List<AvailableSkills> skills_for_Job;

	// (connection with Requested_Jobs table)
	// OneToMany relationShip: one jobOffer has many requests
	@JsonManagedReference
	@OneToMany(mappedBy = "jobOffer")
	private List<Requested_Jobs> job_requests;

	/*----------------------------------------------------------------------------
	CONSTRUCTOR
	----------------------------------------------------------------------------*/
	public Job() {
	}

	/*----------------------------------------------------------------------------
	GETTERS AND SETTERS
	----------------------------------------------------------------------------*/
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public int getExperience_Required() {
		return experience_Required;
	}

	public void setExperience_Required(int experience_Required) {
		this.experience_Required = experience_Required;
	}

	public double getGpa_wanted() {
		return gpa_wanted;
	}

	public void setGpa_wanted(double gpa_wanted) {
		this.gpa_wanted = gpa_wanted;
	}

	public List<User> getUsersApplied() {
		return usersApplied;
	}

	public void setUsersApplied(List<User> usersApplied) {
		this.usersApplied = usersApplied;
	}

	public List<AvailableSkills> getSkills_for_Job() {
		return skills_for_Job;
	}

	public void setSkills_for_Job(List<AvailableSkills> skills_for_Job) {
		this.skills_for_Job = skills_for_Job;
	}

	public List<Requested_Jobs> getJob_requests() {
		return job_requests;
	}

	public void setJob_requests(List<Requested_Jobs> job_requests) {
		this.job_requests = job_requests;
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
