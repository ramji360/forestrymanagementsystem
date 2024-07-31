package com.cg.fms.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cg.fms.entity.Customer;

public interface CustomerDao extends JpaRepository<Customer,String>{

	boolean existsByCustomerName(String customerName);

	Customer findByCustomerName(String customerName);

}
