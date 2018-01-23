package com.zhao.example.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zhao.example.model.Comp;

public interface CompRepository extends JpaRepository<Comp, Long> {
	
}
