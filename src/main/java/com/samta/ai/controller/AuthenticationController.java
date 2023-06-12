package com.samta.ai.controller;

import com.samta.ai.constants.ApiConstants;
import com.samta.ai.entity.Customer;
import com.samta.ai.entity.Vendor;
import com.samta.ai.handler.CustomException;
import com.samta.ai.model.AuthRequest;
import com.samta.ai.model.AuthResponse;
import com.samta.ai.model.RefreshTokenReq;
import com.samta.ai.repository.CustomerRepository;
import com.samta.ai.repository.VendorRepository;
import com.samta.ai.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/authentication")
@Slf4j
public class AuthenticationController {
    @Autowired
    private VendorRepository vendorRepository;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> generateAccessToken(@RequestBody AuthRequest authRequest) throws CustomException {
        Vendor vendor=null;
        Customer customer=null;
        if(authRequest==null || authRequest.getUserType()<1 || authRequest.getUserType()>2)
            throw new CustomException(HttpStatus.UNAUTHORIZED.value(),"Invalid user type");
        if(authRequest.getUserType()==1){
            vendor=vendorRepository.findByEmail(authRequest.getEmail()).orElseThrow(()->new CustomException(HttpStatus.FORBIDDEN.value(), "Invalid username"));
            if(!bCryptPasswordEncoder.matches(authRequest.getPassword(), vendor.getPassword()))
                throw new CustomException(HttpStatus.UNAUTHORIZED.value(), "Incorrect credentials");
        }
        else {
            customer=customerRepository.findByEmail(authRequest.getEmail()).orElseThrow(()->new CustomException(HttpStatus.FORBIDDEN.value(), "Invalid username"));
            if(!bCryptPasswordEncoder.matches(authRequest.getPassword(), customer.getPassword()))
                throw new CustomException(HttpStatus.UNAUTHORIZED.value(), "Incorrect credentials");
        }
        return ResponseEntity.status(HttpStatus.OK).
                body(new AuthResponse(jwtUtil.generateAccessToken(authRequest), jwtUtil.generateRefreshToken(authRequest)));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> generateRefreshToken(@RequestBody RefreshTokenReq refreshTokenReq) throws CustomException {
        try {
            if(!jwtUtil.isValidRefreshToken(refreshTokenReq.getRefreshToken()))
                throw new CustomException(HttpStatus.BAD_REQUEST.value(), "Invalid refresh token");
            var claims=jwtUtil.extractAllClaims(refreshTokenReq.getRefreshToken());
            var userType=((String)claims.get(ApiConstants.USER_TYPE)).equalsIgnoreCase(ApiConstants.VENDOR) ? 1 :  2;
            var authRequest= new AuthRequest((String) claims.get(ApiConstants.USERNAME),null,userType);
            return ResponseEntity.status(HttpStatus.OK).
                    body(new AuthResponse(jwtUtil.generateAccessToken(authRequest), jwtUtil.generateRefreshToken(authRequest)));
        }catch (ExpiredJwtException | SignatureException exception){
            throw new CustomException(HttpStatus.FORBIDDEN.value(), "JWT token expired");
        }
    }
}
