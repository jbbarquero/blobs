package com.malsolo.blobs.domain;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.test.RooIntegrationTest;

@RooIntegrationTest(entity = Documento.class)
public class DocumentoIntegrationTest {

    @Autowired
    private DocumentoDataOnDemand dod;
    
    @Test
    public void testMarkerMethod() {
    }

	@Test
    public void testCountAllDocumentoes() {
        Assert.assertNotNull("Data on demand for 'Documento' failed to initialize correctly", dod.getRandomDocumento());
        long count = Documento.countDocumentoes();
        Assert.assertTrue("Counter for 'Documento' incorrectly reported there were no entries", count > 0);
    }
}
