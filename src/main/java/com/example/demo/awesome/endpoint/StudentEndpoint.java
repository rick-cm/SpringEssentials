package com.example.demo.awesome.endpoint;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import static java.util.Arrays.asList;

import java.time.LocalDateTime;

import com.example.demo.awesome.error.CustomErrorType;
import com.example.demo.awesome.error.ResourceNotFoundException;
import com.example.demo.awesome.model.Student;
import com.example.demo.awesome.repository.StudentRepository;
import com.example.demo.awesome.util.DateUtil;

@RestController
@RequestMapping("/student")
public class StudentEndpoint {
	
	private final StudentRepository dao;
	
	@Autowired
	public StudentEndpoint(StudentRepository dao) {
		this.dao = dao;
	}

	@GetMapping
	public ResponseEntity<?> listAll(Pageable pageable){
		
		return new ResponseEntity<>(dao.findAll(pageable), HttpStatus.OK);
	}
	
	@GetMapping( path = "/{id}")
	public ResponseEntity<?> getStudentById(@PathVariable("id") Long id, @AuthenticationPrincipal UserDetails userDetails){
		System.out.println(userDetails);
		verifyIfStudentExists(id);
		return new ResponseEntity<>(dao.findById(id), HttpStatus.OK);
	}
	
	@GetMapping(path = "/findByName/{name}")
	public ResponseEntity<?> getStudentByName(@PathVariable("name") String name){
		List<Student> students = dao.findByNameIgnoreCaseContaining(name);
		if(students.isEmpty())
			return new ResponseEntity<>(new CustomErrorType("Student Not Found"), HttpStatus.NOT_FOUND);
		return new ResponseEntity<>(students, HttpStatus.OK);
	}
	
	@PostMapping
	@Transactional(rollbackFor = Exception.class)
	public ResponseEntity<?> save(@Valid @RequestBody Student student){
		return new ResponseEntity<>(dao.save(student), HttpStatus.OK);
	}

	@DeleteMapping(path = "/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> delete(@PathVariable Long id){
		verifyIfStudentExists(id);
		dao.deleteById(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@PutMapping
	public ResponseEntity<?> update(@RequestBody Student student){
		verifyIfStudentExists(student.getId());
		dao.save(student);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	private void verifyIfStudentExists(Long id) {
		if(dao.findById(id).orElse(null) == null)
			throw new ResourceNotFoundException("Student not found for ID: "+id);
	}
}
