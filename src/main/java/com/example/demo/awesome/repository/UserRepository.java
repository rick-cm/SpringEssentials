package com.example.demo.awesome.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.awesome.model.User;

public interface UserRepository extends JpaRepository<User, Long>{

	User findByUsername(String username);
}
