package com.example.project.models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "requested_jobs")
public class Requested_Jobs {

	/*----------------------------------------------------------------------------
	TABEL
	----------------------------------------------------------------------------*/
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String status;

	/*----------------------------------------------------------------------------
	RELATIONS
	----------------------------------------------------------------------------*/
	// (connection with Job table)
	// RelationShip is ManyToOne: Many requestedJob related to One job:
	@JsonBackReference
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "job_id")
	private Job jobOffer;

	// (connection with User table)
	// RelationShip is ManyToOne: Many requestedJob related to One user:
	@JsonBackReference
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user_applied;

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
	CONSTRUCTORS
	----------------------------------------------------------------------------*/
	public Requested_Jobs(User user_applied, Job jobOffer, String status) {
		this.user_applied = user_applied;
		this.jobOffer = jobOffer;
		this.status = status;
	}

	public Requested_Jobs() {
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Job getjobOffer() {
		return jobOffer;
	}

	public void setjobOffer(Job jobOffer) {
		this.jobOffer = jobOffer;
	}

	public User getUser_applied() {
		return user_applied;
	}

	public void setUser_applied(User user_applied) {
		this.user_applied = user_applied;
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
