package com.transactguard.transactguard.exception;

import lombok.Getter;

@Getter
public class RequestException extends RuntimeException {
    private final String field;
     public RequestException(String field, String message) {
         super(message);
         this.field = field;
     }
}
