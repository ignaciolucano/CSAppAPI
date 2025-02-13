package com.csappapi.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import com.csappapi.api.ContentServerAPI;
import com.csappapi.api.ContentServerAPIImpl;
import com.csappapi.auth.OTAuthentication;
import com.csappapi.exception.APIException;

@WebServlet("/api/*")
public class APIServlet extends HttpServlet {
   private ContentServerAPI api;
   private Gson gson;

   @Override
   public void init() throws ServletException {
       String csUrl = getServletContext().getInitParameter("CS_URL");
       String otdsUrl = getServletContext().getInitParameter("OTDS_URL");
       
       OTAuthentication auth = new OTAuthentication(otdsUrl);
       api = new ContentServerAPIImpl(csUrl, auth);
       gson = new Gson();
   }

   @Override
   protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
       throws ServletException, IOException {
       
       resp.setContentType("application/json");
       
       try {
           switch (req.getPathInfo()) {
               case "/workspace/url":
                   handleGetWorkspaceURL(req, resp);
                   break;
               default:
                   resp.sendError(HttpServletResponse.SC_NOT_FOUND);
           }
       } catch (APIException e) {
           sendError(resp, e);
       }
   }

   @Override
   protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
       throws ServletException, IOException {
       
       resp.setContentType("application/json");
       
       try {
           switch (req.getPathInfo()) {
               case "/changerequest/close":
                   handleCloseChangeRequest(req, resp);
                   break;
               case "/document/add":
                   handleAddDocument(req, resp);
                   break;
               default:
                   resp.sendError(HttpServletResponse.SC_NOT_FOUND);
           }
       } catch (APIException e) {
           sendError(resp, e);
       }
   }

   private void handleGetWorkspaceURL(HttpServletRequest req, HttpServletResponse resp) 
       throws IOException {
       String type = req.getParameter("type");
       String key = req.getParameter("key");
       
       String url = api.getWorkspaceURL(type, key);
       resp.getWriter().write(gson.toJson(new Response("success", url)));
   }

   private void handleCloseChangeRequest(HttpServletRequest req, HttpServletResponse resp) 
       throws IOException {
       String crType = req.getParameter("crType");
       String crKey = req.getParameter("crKey");
       String finalType = req.getParameter("finalType");
       String finalKey = req.getParameter("finalKey");
       
       boolean success = api.closeChangeRequest(crType, crKey, finalType, finalKey);
       resp.getWriter().write(gson.toJson(new Response("success", String.valueOf(success))));
   }

   private void handleAddDocument(HttpServletRequest req, HttpServletResponse resp) 
       throws IOException {
       String type = req.getParameter("type");
       String key = req.getParameter("key");
       String docType = req.getParameter("docType");
       java.util.Date expDate = gson.fromJson(req.getParameter("expDate"), java.util.Date.class);
       
       String docId = api.addDocumentBW(type, key, docType, expDate);
       resp.getWriter().write(gson.toJson(new Response("success", docId)));
   }

   private void sendError(HttpServletResponse resp, Exception e) throws IOException {
       resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
       resp.getWriter().write(gson.toJson(new Response("error", e.getMessage())));
   }

   private static class Response {
       String status;
       String data;
       
       Response(String status, String data) {
           this.status = status;
           this.data = data;
       }
   }
}
