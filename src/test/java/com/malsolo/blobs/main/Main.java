package com.malsolo.blobs.main;

import org.apache.log4j.Logger;

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
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(Main.class);
	
	private static final Long MYSQL_DOC_ID = 245L;

	@Autowired
    private DocumentDataOnDemand dod;
	
	@Transactional
	public Document doStuff() {
		if (logger.isDebugEnabled()) {
			logger.debug("doStuff() - start");
		}

		File tiff = new File("src/test/resources/images/tiff.tif");
		Assert.assertTrue("Error, TIFF inexistente", tiff.exists());
		byte[] bytes = null;
		try {
			bytes = FileUtils.readFileToByteArray(tiff);
		} catch (IOException e) {
			logger.error("doStuff()", e);

			Assert.fail("Error al leer el TIFF en bytes: "+e.getMessage());
			e.printStackTrace();
		}
		
		Document doc = dod.getNewTransientDocument(1);
		
		doc.setImagen(bytes);
    	
		doc.persist();
		doc.flush();
		
		if (logger.isDebugEnabled()) {
			logger.debug("doStuff() - end");
		}
		return doc;
	}
	
	public void unDoStuff(Long id) {
		if (logger.isDebugEnabled()) {
			logger.debug("unDoStuff(Long) - start");
		}

		Document doc = Document.findDocument(id);
		try {
			File newTiff = new File("src/test/resources/images/mysqltiff.tif");
			Long sequential = 0L;
			while (newTiff.exists()) {
				newTiff = new File("src/test/resources/images/mysqltiff_"+id+"_"+(sequential++)+".tiff");
			}
			FileUtils.writeByteArrayToFile(newTiff, doc.getImagen());
		} catch (IOException e) {
			logger.error("unDoStuff(Long)", e);

			e.printStackTrace();
		}

		if (logger.isDebugEnabled()) {
			logger.debug("unDoStuff(Long) - end");
		}
	}
	
	public void doIt() {
		if (logger.isDebugEnabled()) {
			logger.debug("doIt() - start");
		}

		Document doc = Document.findDocument(MYSQL_DOC_ID);
		if (doc==null) {
			System.out.println("Document doesn't exist: "+MYSQL_DOC_ID);
			doc = doStuff();
			System.out.println("New document created: "+doc.getId());
		}

		unDoStuff(doc.getId());

		if (logger.isDebugEnabled()) {
			logger.debug("doIt() - end");
		}
	}

	/**
	 * @param args
	 */
	public static void main(String... args) {
		if (logger.isDebugEnabled()) {
			logger.debug("main(String) - start");
		}

		long start = System.currentTimeMillis();

		ApplicationContext context = new ClassPathXmlApplicationContext("/META-INF/spring/applicationContext.xml");
		long end = System.currentTimeMillis() - start;

		if (logger.isDebugEnabled()) {
			logger.debug("main(String) - application context instantiated in "+end);
		}

		Main main = context.getBean(Main.class);
		main.doIt();
		end = System.currentTimeMillis() - start;

		if (logger.isDebugEnabled()) {
			logger.debug("main(String) - end in "+end);
		}
	}

}
