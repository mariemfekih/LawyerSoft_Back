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
import org.springframework.stereotype.Service;
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

    public String createFolder(String name)
    {

        Session session=this.alfrescoConfig.createSession();
        Folder parent = session.getRootFolder();

        // prepare properties
        Map<String, Object> properties = new HashMap<String, Object>();
        properties.put(PropertyIds.NAME, name);
        properties.put(PropertyIds.OBJECT_TYPE_ID, "cmis:folder");

        // create the folder
        Folder newFolder = parent.createFolder(properties);
        String nameFolder=newFolder.getName();
        return nameFolder;
    }

    public void deleteFolder(String folderName) {
        Session session = alfrescoConfig.createSession();
        Folder parent = session.getRootFolder();
        for (CmisObject object : parent.getChildren()) {
            if (object.getName().equals(folderName)) {
                object.delete(true);
                return;
            }
        }
        throw new IllegalArgumentException("Folder not found: " + folderName);
    }

    public void updateFolderName(String currentName, String newName) {
        System.out.println("Updating folder name from " + currentName + " to " + newName);
        Session session = alfrescoConfig.createSession();
        Folder parent = session.getRootFolder();
        for (CmisObject object : parent.getChildren()) {
            if (object.getName().equals(currentName)) {
                Folder folder = (Folder) object;
                Map<String, Object> properties = new HashMap<>();
                properties.put(PropertyIds.NAME, newName);
                folder.updateProperties(properties);
                return;
            }
        }
        throw new IllegalArgumentException("Folder not found: " + currentName);
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
    public void deleteFileByName(String folderName, String fileName) {
        Session session = alfrescoConfig.createSession();
        // Get the folder by name
        Folder folder = getFolderByName(session, folderName);
        if (folder == null) {
            throw new NotFoundException("Folder not found: " + folderName);
        }
        // Find the document by name
        Document document = getDocumentByName(folder, fileName);
        if (document == null) {
            throw new NotFoundException("File not found: " + fileName);
        }
        // Delete the document
        document.delete(true);
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

}

