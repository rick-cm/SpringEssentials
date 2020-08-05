package com.example.demo.awesome.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.awesome.model.Student;

public interface StudentRepository extends JpaRepository<Student, Long>{

	List<Student> findByNameIgnoreCaseContaining(String name);
	
	
}
