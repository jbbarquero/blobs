package com.malsolo.blobs.domain;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.test.RooIntegrationTest;

@RooIntegrationTest(entity = Document.class)
public class DocumentIntegrationTest {

	@Autowired
    private DocumentDataOnDemand dod;

	@Test
    public void testMarkerMethod() {
    }
    
	@Test
    public void testSaveImage() {
		File tiff = new File("src/test/resources/images/tiff.tif");
		Assert.assertTrue("Error, TIFF inexistente", tiff.exists());
		byte[] bytes = null;
		try {
			bytes = FileUtils.readFileToByteArray(tiff);
		} catch (IOException e) {
			Assert.fail("Error al leer el TIFF en bytes: "+e.getMessage());
			e.printStackTrace();
		}
		
		Document doc = dod.getNewTransientDocument(1);
		
		doc.setBytes(bytes);
    	
		doc.persist();
		doc.flush();
    }

}
