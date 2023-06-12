package com.samta.ai.handler;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<?> usernameNotFound(UsernameNotFoundException usernameNotFoundException){
        log.error(usernameNotFoundException.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(usernameNotFoundException.getMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handleBadCredentials(BadCredentialsException badCredentialsException){
        log.error(badCredentialsException.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(badCredentialsException.getMessage());
    }

    @ExceptionHandler(CustomException.class)
    @ResponseBody
    public ResponseEntity<?> handleCustomException(CustomException customException){
        log.error(customException.toString());
        return ResponseEntity.status(customException.getStatusCode()).body(customException.getErrorMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException methodArgumentNotValidException){
        log.error("Args");
        Map<String,String> map=new HashMap<>();
        methodArgumentNotValidException.getBindingResult()
                .getAllErrors().forEach(error->map.put(((FieldError)error).getField(),error.getDefaultMessage()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    @ResponseBody
    public ResponseEntity<?> handleExpiredJWTException(ExpiredJwtException expiredJwtException){
        log.error(expiredJwtException.toString());
        return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body("Token expired");
    }
}
