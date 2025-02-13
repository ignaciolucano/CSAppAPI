package com.csappapi.auth;

import com.opentext.api.OTCredentials;
import com.opentext.api.security.Authentication;
import com.csappapi.exception.APIException;

public class OTAuthentication {
   private final String otdsUrl;
   private final Authentication otdsAuth;
   
   public OTAuthentication(String otdsUrl) {
       this.otdsUrl = otdsUrl;
       this.otdsAuth = new Authentication(otdsUrl);
   }
   
   public OTCredentials getCredentials() {
       try {
           return otdsAuth.getCurrentCredentials();
       } catch (Exception e) {
           throw new APIException("Error getting credentials", e);
       }
   }
   
   public String validateToken(String token) {
       try {
           return otdsAuth.validateTicket(token);
       } catch (Exception e) {
           throw new APIException("Invalid token", e);
       }
   }
   
   public void logout() {
       try {
           otdsAuth.logout();
       } catch (Exception e) {
           throw new APIException("Error logging out", e);
       }
   }
}
