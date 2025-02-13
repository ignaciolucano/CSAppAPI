package com.csappapi.api;

import com.opentext.api.OTConnection;
import com.opentext.api.node.Node;
import com.opentext.api.node.Workspace;
import com.opentext.api.document.Document;

public interface ContentServerAPI {

   /**
    * Gets Content Server Workspace URL
    * @param workspaceType Type of workspace (BP/Material)
    * @param workspaceKey Unique workspace identifier
    * @return Full URL to access the workspace
    * @throws APIException if workspace not found or access error
    */
   String getWorkspaceURL(String workspaceType, String workspaceKey);

   /**
    * Closes change request and links documents
    * @param crWorkspaceType Change request workspace type
    * @param crWorkspaceKey Change request workspace key
    * @param finalWorkspaceType Target workspace type
    * @param finalWorkspaceKey Target workspace key
    * @return true if successful
    * @throws APIException if error occurs
    */
   boolean closeChangeRequest(String crWorkspaceType, String crWorkspaceKey, 
                            String finalWorkspaceType, String finalWorkspaceKey);

   /**
    * Adds new document to Business Workspace
    * @param workspaceType Workspace type
    * @param workspaceKey Workspace key
    * @param documentType Type of document
    * @param expirationDate Document expiration date
    * @return Document ID
    * @throws APIException if error occurs
    */
   String addDocumentBW(String workspaceType, String workspaceKey, 
                       String documentType, java.util.Date expirationDate);
}
