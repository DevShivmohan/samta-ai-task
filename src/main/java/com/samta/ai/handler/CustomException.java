package com.samta.ai.handler;

import lombok.Data;

@Data
public class CustomException extends Throwable{
    private int statusCode;
    private String errorMessage;
    public CustomException(int statusCode,String errorMessage){
        super(errorMessage);
        this.statusCode=statusCode;
        this.errorMessage=errorMessage;
    }
}
