package com.csappapi.config;

import com.csappapi.auth.OTAuthentication;
import com.csappapi.api.ContentServerAPI;
import com.csappapi.api.ContentServerAPIImpl;

public class AppConfig {
   private static final String CS_URL;
   private static final String OTDS_URL;
   private static ContentServerAPI api;

   static {
       CS_URL = System.getenv("CS_URL");
       OTDS_URL = System.getenv("OTDS_URL");
       
       if (CS_URL == null || OTDS_URL == null) {
           throw new RuntimeException("Required environment variables not set");
       }
   }

   public static ContentServerAPI getAPI() {
       if (api == null) {
           OTAuthentication auth = new OTAuthentication(OTDS_URL);
           api = new ContentServerAPIImpl(CS_URL, auth);
       }
       return api;
   }

   public static String getCSURL() {
       return CS_URL;
   }

   public static String getOTDSURL() {
       return OTDS_URL;
   }
}
