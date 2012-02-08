package com.malsolo.blobs.main;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.malsolo.blobs.domain.Document;
import com.malsolo.blobs.domain.DocumentDataOnDemand;

@Component
public class Main {

	@Autowired
    private DocumentDataOnDemand dod;
	
	@Transactional
	public void doStuff() {
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
		
		doc.setImagen(bytes);
    	
		doc.persist();
		doc.flush();
	}
	
	public void unDoStuff() {
		Document doc = Document.findDocument(245L);
		try {
			FileUtils.writeByteArrayToFile(new File("src/test/resources/images/mysqltiff.tif"), doc.getImagen());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param args
	 */
	public static void main(String... args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("/META-INF/spring/applicationContext.xml");
		Main main = context.getBean(Main.class);
		main.unDoStuff();
	}

}
