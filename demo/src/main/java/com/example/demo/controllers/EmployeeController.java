package com.example.demo.controllers;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Employee;
import com.example.demo.EmployeeResourceAssembler;
import com.example.demo.NotFoundException.EmployeeNotFoundException;
import com.example.demo.repositorys.EmployeeRepository;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
public class EmployeeController {

	private final EmployeeRepository repository;
	private final EmployeeResourceAssembler assembler;
	
	EmployeeController(EmployeeRepository repository,EmployeeResourceAssembler assembler){
		this.repository=repository;
		this.assembler=assembler;
	}
	@GetMapping("/employees")
	public Resources<Resource<Employee>>all(){
		//return repository.findAll();
		/*
		List<Resource<Employee>>employees = repository.findAll().stream()
				.map(employee -> new Resource<>(employee,
						linkTo(methodOn(EmployeeController.class).one(employee.getId())).withSelfRel(),
						linkTo(methodOn(EmployeeController.class).all()).withRel("empleyees")))
				.collect(Collectors.toList());*/
		
		List<Resource<Employee>>employees = repository.findAll().stream()
				.map(assembler::toResource)
				.collect(Collectors.toList());
		return new Resources<>(employees, 
				linkTo(methodOn(EmployeeController.class).all()).withSelfRel());		
	}
	@PostMapping("/employees")
	ResponseEntity<?> newEmployee(@RequestBody Employee newEmployee) throws URISyntaxException {
		//return repository.save(newEmployee);
		Resource<Employee>resource = assembler.toResource(repository.save(newEmployee));
		return ResponseEntity
				.created(new URI(resource.getId().expand().getHref()))
				.body(resource);
	}	
	@GetMapping("/employees/{id}")
	public Resource<Employee> one(@PathVariable Long id) {
		Employee employee = repository.findById(id)
				.orElseThrow(()->new EmployeeNotFoundException(id));
		
		return assembler.toResource(employee);
		/*
		Employee employee =  repository.findById(id)
				.orElseThrow(()-> new EmployeeNotFoundException(id));
		return new Resource<>(employee,
				linkTo(methodOn(EmployeeController.class).one(id)).withSelfRel(),
				linkTo(methodOn(EmployeeController.class).all()).withRel("employees"));
				*/
	}
	@PutMapping("/employees/{id}")
	ResponseEntity<?> replaceEmployee(@RequestBody Employee newEmployee, @PathVariable Long id) throws URISyntaxException {
		Employee updatedEmployee = repository.findById(id)
				.map(employee->{
					employee.setName(newEmployee.getName());
					employee.setRole(newEmployee.getRole());
					return repository.save(employee);
				})
				.orElseGet(()->{
					newEmployee.setId(id);
					return repository.save(newEmployee);
				});
		
		Resource<Employee> resource = assembler.toResource(updatedEmployee);
		
		return ResponseEntity
				.created(new URI(resource.getId().expand().getHref()))
				.body(resource);
		/*return repository.findById(id)
				.map(employee -> {
					employee.setName(newEmployee.getName());
					employee.setRole(newEmployee.getRole());
					return repository.save(employee);
				})
				.orElseGet(()->{
					newEmployee.setId(id);
					return repository.save(newEmployee);
				});*/
	}
	@DeleteMapping("/employees/{id}")
	ResponseEntity<?> deleteEmployee(@PathVariable Long id) {
		repository.deleteById(id);
		return ResponseEntity.noContent().build();		
	}
	
}
