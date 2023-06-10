package com.samta.ai.util;

import com.samta.ai.constants.ApiConstants;
import com.samta.ai.entity.Vendor;
import com.samta.ai.handler.CustomException;
import com.samta.ai.repository.VendorRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@AllArgsConstructor
public class CurrentVendorDetails {

    private static JwtUtil jwtUtil;
    private static VendorRepository vendorRepository;
    public static Vendor getVendor() throws CustomException {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String authorizationHeader=request.getHeader(ApiConstants.AUTHORIZATION);
        if(authorizationHeader==null || authorizationHeader.length()<8)
            throw new CustomException(HttpStatus.FORBIDDEN.value(),"Unauthorized access");
        String token=null;
        if(authorizationHeader!=null && authorizationHeader.startsWith("Bearer"))
            token=authorizationHeader.substring(7);
        String email= jwtUtil.extractUsername(token);
        return vendorRepository.findByEmail(email)
                .orElseThrow(()->new CustomException(HttpStatus.UNAUTHORIZED.value(),"Unauthorized access"));
    }
}
