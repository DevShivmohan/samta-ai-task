package com.samta.ai.service.impl;

import com.samta.ai.entity.Customer;
import com.samta.ai.handler.CustomException;
import com.samta.ai.repository.CustomerRepository;
import com.samta.ai.repository.VendorRepository;
import com.samta.ai.service.CustomerService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private CustomerRepository customerRepository;
    private VendorRepository vendorRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Override
    public ResponseEntity<?> addCustomer(Customer customer) {
        customer.setId(0);
        customer.setPassword(bCryptPasswordEncoder.encode(customer.getPassword()));
        return ResponseEntity.status(HttpStatus.CREATED).body(customerRepository.save(customer));
    }

    @Override
    public ResponseEntity<?> updateCustomer(int id, Customer customer) throws CustomException {
        var cust=customerRepository.findById(id)
                .orElseThrow(()->new CustomException(HttpStatus.NOT_FOUND.value(), "Customer does not exists"));
        customer.setId(id);
        customer.setPassword(bCryptPasswordEncoder.encode(customer.getPassword()));
        return ResponseEntity.status(HttpStatus.OK).body(customerRepository.save(customer));
    }

    @Override
    public ResponseEntity<?> deleteCustomer(int id) throws CustomException {
        var cust=customerRepository.findById(id)
                .orElseThrow(()->new CustomException(HttpStatus.NOT_FOUND.value(), "Customer does not exists"));
        customerRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body(cust);
    }

    @Override
    public ResponseEntity<?> getCustomer(int id) throws CustomException {
        return ResponseEntity.status(HttpStatus.OK)
                .body(customerRepository.findById(id)
                        .orElseThrow(()->new CustomException(HttpStatus.NOT_FOUND.value(), "Customer does not exists")));
    }
}
