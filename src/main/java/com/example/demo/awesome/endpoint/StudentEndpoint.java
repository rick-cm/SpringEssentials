package com.example.demo.awesome.endpoint;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import static java.util.Arrays.asList;

import java.time.LocalDateTime;

import com.example.demo.awesome.error.CustomErrorType;
import com.example.demo.awesome.model.Student;
import com.example.demo.awesome.util.DateUtil;

@RestController
@RequestMapping("/student")
public class StudentEndpoint {
	
	private final DateUtil dateUtil;
	
	@Autowired
	public StudentEndpoint(DateUtil dateUtil) {
		this.dateUtil = dateUtil;
	}

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> listAll(){
		
		//System.out.println(dateUtil.formatLocalDateTimeToDatabaseStyle(LocalDateTime.now()));
		return new ResponseEntity<>(Student.studentList, HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.GET, path = "/{id}")
	public ResponseEntity<?> getStudentById(@PathVariable("id") int id){
		Student student = new Student();
		student.setId(id);
		int index = Student.studentList.indexOf(student);
		if(index == -1) 
			return new ResponseEntity<>(new CustomErrorType("Student Not Found"), HttpStatus.NOT_FOUND);
		return new ResponseEntity<>(Student.studentList.get(index), HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> save(@RequestBody Student student){
		Student.studentList.add(student);
		return new ResponseEntity<>(student, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.DELETE)
	public ResponseEntity<?> delete(@RequestBody Student student){
		Student.studentList.remove(student);
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
