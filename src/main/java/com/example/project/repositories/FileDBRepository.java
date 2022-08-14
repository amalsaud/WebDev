package com.example.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.project.models.UserApplication;

@Repository
public interface FileDBRepository extends JpaRepository<UserApplication, String> {
}
