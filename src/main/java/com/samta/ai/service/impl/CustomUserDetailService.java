package com.samta.ai.service.impl;

import com.samta.ai.constants.ApiConstants;
import com.samta.ai.entity.Customer;
import com.samta.ai.entity.Vendor;
import com.samta.ai.handler.CustomException;
import com.samta.ai.repository.CustomerRepository;
import com.samta.ai.repository.VendorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    private VendorRepository vendorRepository;
    @Autowired
    private CustomerRepository customerRepository;
    public UserDetails loadUserByUsername(String username, String userType) throws UsernameNotFoundException, CustomException {
        Vendor vendor=null;
        Customer customer=null;
        System.out.println(userType);
        if(userType.equalsIgnoreCase(ApiConstants.VENDOR))
            vendor= vendorRepository.findByEmail(username)
                .orElseThrow(()->new CustomException(HttpStatus.NOT_FOUND.value(), "User not found"));
        else
            customer=customerRepository.findByEmail(username)
                    .orElseThrow(()->new CustomException(HttpStatus.NOT_FOUND.value(), "User not found"));
        return new CustomUserDetail(vendor);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new CustomUserDetail(vendorRepository.findByEmail(username)
                .orElseThrow(()->new UsernameNotFoundException("User not found")));
    }
}
