package com.example.gestion_user.services;

import com.example.gestion_user.configurations.AlfrescoConfig;
import com.example.gestion_user.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.data.ContentStream;
import org.apache.chemistry.opencmis.commons.data.PropertyData;
import org.apache.chemistry.opencmis.commons.exceptions.CmisObjectNotFoundException;
import org.apache.chemistry.opencmis.commons.impl.json.JSONObject;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.apache.chemistry.opencmis.client.api.*;
import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.util.ContentStreamUtils;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.enums.BaseTypeId;
import org.apache.chemistry.opencmis.commons.enums.VersioningState;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AlfrescoService {

    private final AlfrescoConfig alfrescoConfig;
    private final String alfrescoBaseUrl = "http://localhost:8080/alfresco/api/-default-/public/alfresco/versions/1";
    public void updateFileName(String fileId, String newName) {
        updateNodeName(fileId, newName);
    }

    public void updateFolderName(String folderId, String newName) {
        updateNodeName(folderId, newName);
    }

    private void updateNodeName(String nodeId, String newName) {
        String url = alfrescoBaseUrl + "/nodes/" + nodeId;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBasicAuth("admin", "admin"); // Replace with your Alfresco credentials

        JSONObject requestBody = new JSONObject();
        requestBody.put("name", newName);

        HttpEntity<String> entity = new HttpEntity<>(requestBody.toString(), headers);

        try {
            ResponseEntity<String> response = new RestTemplate().exchange(url, HttpMethod.PUT, entity, String.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                System.out.println("Node name updated in Alfresco.");
            } else {
                throw new RuntimeException("Failed to update node name in Alfresco. Status code: " + response.getStatusCodeValue());
            }
        } catch (RestClientException e) {
            throw new RuntimeException("Error updating node name in Alfresco: " + e.getMessage(), e);
        }
    }
    public boolean folderExists(String folderName) {
        Session session = this.alfrescoConfig.createSession();
        try {
            Folder folder = (Folder) session.getObjectByPath("/" + folderName);
            return folder != null;
        } catch (CmisObjectNotFoundException e) {
            return false;
        }
    }
    public String getFolderId(String folderName) {
        Session session = this.alfrescoConfig.createSession();
        Folder parent = session.getRootFolder();

        Folder folder = (Folder) session.getObjectByPath("/" + folderName);
        return folder.getId();
    }
    public String addFolder(String name) {
        Session session = this.alfrescoConfig.createSession();
        Folder parent = session.getRootFolder();

        // prepare properties
        Map<String, Object> properties = new HashMap<String, Object>();
        properties.put(PropertyIds.NAME, name);
        properties.put(PropertyIds.OBJECT_TYPE_ID, "cmis:folder");

        // create the folder
        Folder newFolder = parent.createFolder(properties);
        return newFolder.getId(); // Return the ID of the newly created folder
    }
/*    public void deleteFolder(String folderName) {
        Session session = alfrescoConfig.createSession();
        Folder parent = session.getRootFolder();
        for (CmisObject object : parent.getChildren()) {
            if (object.getName().equals(folderName)) {
                object.delete(true);
                return;
            }
        }
        throw new IllegalArgumentException("Folder not found: " + folderName);
    }*/
public void deleteFolderById(String folderId) {
    Session session = alfrescoConfig.createSession();
    // Fetch the folder by ID
    CmisObject cmisObject = session.getObject(folderId);
    if (cmisObject instanceof Folder) {
        Folder folder = (Folder) cmisObject;
        // Recursively delete all children
        deleteFolderContents(folder);
        // Delete the folder itself
        folder.delete(true);
    } else {
        throw new NotFoundException("Folder not found with id: " + folderId);
    }
}

    private void deleteFolderContents(Folder folder) {
        for (CmisObject child : folder.getChildren()) {
            if (child instanceof Folder) {
                deleteFolderById(child.getId());
            } else {
                child.delete(true);
            }
        }
    }
    public String addFileToFolder(String folderName, MultipartFile file) throws IOException {
        Session session = alfrescoConfig.createSession();
        // Get the folder by name
        Folder folder = getFolderByName(session, folderName);
        if (folder == null) {
            throw new NotFoundException("Folder not found: " + folderName);
        }
        // Prepare properties for the file
        Map<String, Object> properties = new HashMap<>();
        properties.put(PropertyIds.OBJECT_TYPE_ID, BaseTypeId.CMIS_DOCUMENT.value());
        // Set the file name
        String fileName = file.getOriginalFilename();
        properties.put(PropertyIds.NAME, fileName);
        // Set the content stream
        InputStream stream = file.getInputStream();
        ContentStream contentStream = session.getObjectFactory().createContentStream(
                fileName,
                file.getSize(),
                file.getContentType(),
                stream
        );
        // Create the file in the folder
        Document document = folder.createDocument(properties, contentStream, VersioningState.MAJOR);
        // Close the stream
        stream.close();
        // Return the ID of the uploaded file
        return document.getId();
    }

    private Folder getFolderByName(Session session, String folderName) {
        // Query the folder by name
        String query = "SELECT * FROM cmis:folder WHERE cmis:name = '" + folderName + "'";
        ItemIterable<QueryResult> results = session.query(query, false);

        // Iterate through the results
        for (QueryResult result : results) {
            PropertyData<?> propertyData = result.getPropertyById(PropertyIds.OBJECT_ID);
            String folderId = propertyData.getFirstValue().toString();

            // Get the folder by ID
            CmisObject object = session.getObject(folderId);
            if (object instanceof Folder) {
                return (Folder) object;
            }
        }

        return null;
    }

    public void deleteFileById(String fileId) {
        Session session = alfrescoConfig.createSession();
        // Fetch the document by ID
        CmisObject cmisObject = session.getObject(fileId);
        if (cmisObject instanceof Document) {
            Document document = (Document) cmisObject;
            // Delete the document
            document.delete(true);
        } else {
            throw new NotFoundException("File not found with id: " + fileId);
        }
    }
    private Document getDocumentByName(Folder folder, String fileName) {
        // Get all the children (documents and folders) of the folder
        ItemIterable<CmisObject> children = folder.getChildren();

        // Iterate through the children to find the document by name
        for (CmisObject child : children) {
            if (child instanceof Document && child.getName().equals(fileName)) {
                return (Document) child;
            }
        }

        return null;
    }
    public String createSubFolder(String parentFolderName, String subFolderName) {
        Session session = alfrescoConfig.createSession();
        // Get the parent folder by name
        Folder parentFolder = getFolderByName(session, parentFolderName);
        if (parentFolder == null) {
            throw new NotFoundException("Parent folder not found: " + parentFolderName);
        }
        // Prepare properties for the sub-folder
        Map<String, Object> properties = new HashMap<>();
        properties.put(PropertyIds.NAME, subFolderName);
        properties.put(PropertyIds.OBJECT_TYPE_ID, "cmis:folder");
        // Create the sub-folder in the parent folder
        Folder subFolder = parentFolder.createFolder(properties);
        // Return the ID of the newly created sub-folder
        return subFolder.getId();
    }

    public Document getFileById(String fileId) {
        Session session = alfrescoConfig.createSession();
        // Fetch the document by ID
        CmisObject cmisObject = session.getObject(fileId);
        if (cmisObject instanceof Document) {
            Document document = (Document) cmisObject;
            return document;
        } else {
            throw new NotFoundException("File not found with id: " + fileId);
        }
    }
    public void createOrUpdateProfileImage(String profileImgId, MultipartFile file) {
        String url = alfrescoBaseUrl + "/" + profileImgId + "/children";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.setBasicAuth("admin", "admin"); // Replace with your Alfresco credentials

        // Prepare the file content part
        ByteArrayResource fileResource;
        try {
            fileResource = new ByteArrayResource(file.getBytes()) {
                @Override
                public String getFilename() {
                    return file.getOriginalFilename();
                }
            };
        } catch (Exception e) {
            throw new RuntimeException("Error reading file: " + e.getMessage(), e);
        }

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("filedata", fileResource);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response = new RestTemplate().exchange(url, HttpMethod.POST, requestEntity, String.class);
            if (response.getStatusCode() == HttpStatus.CREATED || response.getStatusCode() == HttpStatus.OK) {
                System.out.println("Profile image uploaded/updated successfully in Alfresco.");
            } else {
                throw new RuntimeException("Failed to upload/update profile image in Alfresco. Status code: " + response.getStatusCodeValue());
            }
        } catch (RestClientException e) {
            throw new RuntimeException("Error uploading/updating profile image in Alfresco: " + e.getMessage(), e);
        }
    }
    public Document getFileByIdByPath(String fileId) {
        Session session = alfrescoConfig.createSession();
        CmisObject cmisObject = session.getObject(fileId);
        if (cmisObject instanceof Document) {
            return (Document) cmisObject;
        } else {
            throw new NotFoundException("File not found with id: " + fileId);
        }
    }


}

