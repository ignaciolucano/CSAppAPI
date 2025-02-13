package com.csappapi.api;

import com.opentext.api.OTConnection;
import com.opentext.api.node.Node;
import com.opentext.api.node.Workspace;
import com.opentext.api.document.Document;
import com.csappapi.auth.OTAuthentication;
import com.csappapi.exception.APIException;

public class ContentServerAPIImpl implements ContentServerAPI {
   private final String csUrl;
   private final OTConnection connection;
   private final OTAuthentication auth;

   public ContentServerAPIImpl(String csUrl, OTAuthentication auth) {
       this.csUrl = csUrl;
       this.auth = auth;
       this.connection = new OTConnection(csUrl);
   }

   @Override
   public String getWorkspaceURL(String workspaceType, String workspaceKey) {
       try {
           // Get workspace by business key
           Workspace workspace = connection.getWorkspaceByKey(workspaceType, workspaceKey);
           if (workspace == null) {
               throw new APIException("Workspace not found");
           }

           // Build URL with workspace ID
           return String.format("%s/otcs/cs.exe?func=ll&objId=%s&objAction=browse",
                   csUrl, workspace.getId());

       } catch (Exception e) {
           throw new APIException("Error getting workspace URL", e);
       }
   }

   @Override
   public boolean closeChangeRequest(String crWorkspaceType, String crWorkspaceKey,
                                   String finalWorkspaceType, String finalWorkspaceKey) {
       try {
           // Get source and target workspaces
           Workspace crWorkspace = connection.getWorkspaceByKey(crWorkspaceType, crWorkspaceKey);
           Workspace finalWorkspace = connection.getWorkspaceByKey(finalWorkspaceType, finalWorkspaceKey);

           if (crWorkspace == null || finalWorkspace == null) {
               throw new APIException("Workspace not found");
           }

           // Get all documents from change request workspace
           Node[] nodes = crWorkspace.getNodes();
           
           // Link documents to final workspace
           for (Node node : nodes) {
               if (node instanceof Document) {
                   Document doc = (Document) node;
                   finalWorkspace.addDocumentLink(doc);
               }
           }

           // Update status
           crWorkspace.setAttribute("Status", "Closed");
           crWorkspace.update();

           return true;

       } catch (Exception e) {
           throw new APIException("Error closing change request", e);
       }
   }

   @Override
   public String addDocumentBW(String workspaceType, String workspaceKey,
                              String documentType, java.util.Date expirationDate) {
       try {
           // Get target workspace
           Workspace workspace = connection.getWorkspaceByKey(workspaceType, workspaceKey);
           if (workspace == null) {
               throw new APIException("Workspace not found");
           }

           // Create document
           Document doc = workspace.createDocument(documentType);
           doc.setAttribute("ExpirationDate", expirationDate);
           doc.update();

           return doc.getId();

       } catch (Exception e) {
           throw new APIException("Error adding document", e);
       }
   }

   private void validateConnection() {
       if (!connection.isConnected()) {
           connection.connect(auth.getCredentials());
       }
   }
}
