package com.example.demo.awesome.model;

import javax.persistence.Entity;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;


@Entity
public class Student extends AbstractEntity{
	
	@NotEmpty(message = "The field name may not be empty")
	private String name;
	
	@NotEmpty(message = "The field email may not be empty")
	@Email
	private String email;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
}
