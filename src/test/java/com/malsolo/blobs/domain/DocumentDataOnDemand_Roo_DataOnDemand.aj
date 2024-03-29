// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.malsolo.blobs.domain;

import com.malsolo.blobs.domain.Document;
import com.malsolo.blobs.domain.DocumentDataOnDemand;
import com.malsolo.blobs.service.DocumentService;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

privileged aspect DocumentDataOnDemand_Roo_DataOnDemand {
    
    declare @type: DocumentDataOnDemand: @Component;
    
    private Random DocumentDataOnDemand.rnd = new SecureRandom();
    
    private List<Document> DocumentDataOnDemand.data;
    
    @Autowired
    DocumentService DocumentDataOnDemand.documentService;
    
    public Document DocumentDataOnDemand.getNewTransientDocument(int index) {
        Document obj = new Document();
        setBytes(obj, index);
        setContentType(obj, index);
        setDescription(obj, index);
        setFilename(obj, index);
        setLength(obj, index);
        setName(obj, index);
        setUri(obj, index);
        return obj;
    }
    
    public void DocumentDataOnDemand.setBytes(Document obj, int index) {
        byte[] bytes = String.valueOf(index).getBytes();
        obj.setBytes(bytes);
    }
    
    public void DocumentDataOnDemand.setContentType(Document obj, int index) {
        String contentType = "contentType_" + index;
        obj.setContentType(contentType);
    }
    
    public void DocumentDataOnDemand.setDescription(Document obj, int index) {
        String description = "description_" + index;
        obj.setDescription(description);
    }
    
    public void DocumentDataOnDemand.setFilename(Document obj, int index) {
        String filename = "filename_" + index;
        obj.setFilename(filename);
    }
    
    public void DocumentDataOnDemand.setLength(Document obj, int index) {
        Long length = new Integer(index).longValue();
        obj.setLength(length);
    }
    
    public void DocumentDataOnDemand.setName(Document obj, int index) {
        String name = "name_" + index;
        if (name.length() > 100) {
            name = name.substring(0, 100);
        }
        obj.setName(name);
    }
    
    public void DocumentDataOnDemand.setUri(Document obj, int index) {
        String uri = "uri_" + index;
        if (uri.length() > 100) {
            uri = uri.substring(0, 100);
        }
        obj.setUri(uri);
    }
    
    public Document DocumentDataOnDemand.getSpecificDocument(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        Document obj = data.get(index);
        Long id = obj.getId();
        return documentService.findDocument(id);
    }
    
    public Document DocumentDataOnDemand.getRandomDocument() {
        init();
        Document obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return documentService.findDocument(id);
    }
    
    public boolean DocumentDataOnDemand.modifyDocument(Document obj) {
        return false;
    }
    
    public void DocumentDataOnDemand.init() {
        int from = 0;
        int to = 10;
        data = documentService.findDocumentEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'Document' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<Document>();
        for (int i = 0; i < 10; i++) {
            Document obj = getNewTransientDocument(i);
            try {
                documentService.saveDocument(obj);
            } catch (ConstraintViolationException e) {
                StringBuilder msg = new StringBuilder();
                for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
                    ConstraintViolation<?> cv = iter.next();
                    msg.append("[").append(cv.getConstraintDescriptor()).append(":").append(cv.getMessage()).append("=").append(cv.getInvalidValue()).append("]");
                }
                throw new RuntimeException(msg.toString(), e);
            }
            obj.flush();
            data.add(obj);
        }
    }
    
}
