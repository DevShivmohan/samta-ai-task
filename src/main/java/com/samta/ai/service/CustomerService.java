package com.samta.ai.service;

import com.samta.ai.entity.Customer;
import com.samta.ai.handler.CustomException;
import org.springframework.http.ResponseEntity;

public interface CustomerService {
    ResponseEntity<?> addCustomer(Customer customer);
    ResponseEntity<?> updateCustomer(int id,Customer customer) throws CustomException;
    ResponseEntity<?> deleteCustomer(int id) throws CustomException;
    ResponseEntity<?> getCustomer(int id) throws CustomException, CustomException;
}
