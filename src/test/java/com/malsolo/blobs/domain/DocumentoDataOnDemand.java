package com.malsolo.blobs.domain;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.roo.addon.dod.RooDataOnDemand;
import org.springframework.transaction.annotation.Transactional;

@RooDataOnDemand(entity = Documento.class)
public class DocumentoDataOnDemand {

    private List<Documento> data;
    
	public void init() {
        int from = 0;
        int to = 10;
        data = Documento.findDocumentoEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'Documento' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<Documento>();
        for (int i = 0; i < 10; i++) {
            Documento obj = getNewTransientDocumento(i);
            try {
                obj.persist();
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
