package com.example.demo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class Employee {
	@Id
	@GeneratedValue
	private long id;
	//private String name;
	private String firstName;
	private String lastName;
	private String role;
	
	public Employee() {	
	}
	
	public Employee(String firstName, String lastName,String role) {
		this.firstName=firstName;
		this.lastName=lastName;
		this.role=role;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return firstName + " " + lastName;
	}

	public void setName(String name) {
		String[] parts = name.split(" ");
		this.firstName=parts[0];
		this.lastName=parts[1];
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
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
	
	
	
	
	
	
	}
