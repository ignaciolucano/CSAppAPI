package com.csappapi.exception;

public class APIException extends RuntimeException {
   
   private static final long serialVersionUID = 1L;

   public APIException(String message) {
       super(message);
   }

   public APIException(String message, Throwable cause) {
       super(message, cause);
   }

   public APIException(Throwable cause) {
       super(cause);
   }
}
