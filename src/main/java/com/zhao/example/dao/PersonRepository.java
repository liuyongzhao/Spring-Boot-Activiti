package com.zhao.example.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zhao.example.model.Person;


public interface PersonRepository extends JpaRepository<Person, Long> {
	
	public Person findByPersonName(String personName);
	
}

