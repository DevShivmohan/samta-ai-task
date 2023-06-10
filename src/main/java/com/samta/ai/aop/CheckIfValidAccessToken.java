package com.samta.ai.aop;

import com.samta.ai.constants.ApiConstants;
import com.samta.ai.handler.CustomException;
import com.samta.ai.util.JwtUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@AllArgsConstructor
@Aspect
@Component
@Slf4j
public class CheckIfValidAccessToken {
    private JwtUtil jwtUtil;

    @Before("@annotation(com.samta.ai.aop.CheckIfValidCustomerAccessToken)")
    public void checkIfValidAccessToken(final JoinPoint joinPoint) throws CustomException {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String authorizationHeader=request.getHeader(ApiConstants.AUTHORIZATION);
        if(authorizationHeader==null || authorizationHeader.length()<8)
            throw new CustomException(HttpStatus.FORBIDDEN.value(),"Unauthorized access");
        String token=null;
        if(authorizationHeader!=null && authorizationHeader.startsWith("Bearer"))
            token=authorizationHeader.substring(7);
        if(jwtUtil.isTokenExpired(token) || !jwtUtil.isValidAccessToken(token))
            throw new CustomException(HttpStatus.FORBIDDEN.value(), "Unauthorized access");
        log.info("Access token-"+token);
    }
}
