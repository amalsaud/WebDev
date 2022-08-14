package com.example.project.models;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
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
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name="user")
public class User {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	
    @NotEmpty
    @Size(min = 3, max = 200, message = "Must be 3 - 200 char")
    private String firstName;
    
    @NotEmpty
    @Size(min = 3, max = 200, message = "Must be 3 - 200 char")
    private String lastName;
    
    @Email
    private String email;
    
    
    @NotEmpty
    private String password;
    @Transient
    private String confirmPassword;
    
    private String user_role;
    
    
    //////////////////////////////////////RelatioShip /////////////////////////////////////////
    
    // (connection with UserApplocations table)
    // ------ OneToOne relation: one user has one UserApplication --------
    @OneToOne(mappedBy="user", cascade=CascadeType.ALL, fetch=FetchType.LAZY)
    private UserApplication userApplication;
    
    
    // (connection with job table)
    // ----- ManyToMany relation: many users applied for  many jobs
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "user_job", 
        joinColumns = @JoinColumn(name = "user_id"), 
        inverseJoinColumns = @JoinColumn(name = "job_id")
    )
    private List<Job> jobsAppliedFor;
    
    
    // (connection with Requested_Jobs table)
    // ------ OneToMany relationShip: one user has many requests
  	@JsonManagedReference
  	@OneToMany(mappedBy = "user_applied")
  	private List<Requested_Jobs> user_requests;
    
    
    
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
	public User() {
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getConfirmPassword() {
		return confirmPassword;
	}
	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
	public String getUser_role() {
		return user_role;
	}
	public void setUser_role(String user_role) {
		this.user_role = user_role;
	}
	public UserApplication getUserApplication() {
		return userApplication;
	}
	public void setUserApplication(UserApplication userApplication) {
		this.userApplication = userApplication;
	}
	public List<Job> getJobsAppliedFor() {
		return jobsAppliedFor;
	}
	public void setJobsAppliedFor(List<Job> jobsAppliedFor) {
		this.jobsAppliedFor = jobsAppliedFor;
	}
	public List<Requested_Jobs> getUser_requests() {
		return user_requests;
	}
	public void setUser_requests(List<Requested_Jobs> user_requests) {
		this.user_requests = user_requests;
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
