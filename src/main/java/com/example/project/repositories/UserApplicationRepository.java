package com.example.project.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.project.models.User;
import com.example.project.models.UserApplication;

@Repository
public interface UserApplicationRepository extends CrudRepository<UserApplication, Long> {

	Optional<UserApplication> findById(String appId);

	Optional<UserApplication> findByUser(User user);

}
