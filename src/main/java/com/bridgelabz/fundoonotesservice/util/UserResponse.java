package com.bridgelabz.fundoonotesservice.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
public class UserResponse {
    private String message;
    private int errorCode;
    private Object data;
}
