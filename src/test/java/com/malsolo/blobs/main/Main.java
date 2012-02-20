package com.malsolo.blobs.main;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.malsolo.blobs.domain.Document;
import com.malsolo.blobs.domain.DocumentDataOnDemand;
import com.malsolo.blobs.domain.Documento;
import com.malsolo.blobs.domain.DocumentoDataOnDemand;

@Component
public class Main {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(Main.class);
	
	@Autowired
    private DocumentDataOnDemand dod;
	@Autowired
    private DocumentoDataOnDemand dodo;
	
	public class Documents {
		private Document document;
		private Documento documento;
		
		public Documents(Document document, Documento documento) {
			this.document	= document;
			this.documento	= documento;
		}
		
		public Document getDocument() {
			return document;
		}
		public Documento getDocumento() {
			return documento;
		}
	}
	
	public Documents doStuff() {
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
		
		logger.warn("doStuff() - creating DocumenT...");
		Document doc = dod.getNewTransientDocument(1);
		doc.setBytes(bytes);
		doc.persist();
		doc.flush();
		logger.warn("doStuff() - DocumenT created.");
		
		logger.warn("doStuff() - creating DocumentO...");
		Documento doco = dodo.getNewTransientDocumento(1);
		doco.setOctetos(bytes);
		doco.persist();
		doco.flush();
		logger.warn("doStuff() - DocumentO created.");

		if (logger.isDebugEnabled()) {
			logger.debug("doStuff() - end");
		}
		return new Documents(doc, doco);
	}
	
    @Transactional("mysql")
	public File recoverDocument(Document document) {
		if (logger.isDebugEnabled()) {
			logger.debug("recoverDocument(Document) - start");
		}

		Document doc = Document.findDocument(document.getId());
		if (doc == null) {
			logger.error("recoverDocument(Document) - error, can't find Document with id: "+document.getId());
			return null;
		}
		logger.debug("recoverDocument(Document) - Document found. "+doc.getId());
		
		File img = null;
		try {
			img = createImage(new File("src/test/resources/images"), "mysqltiff", ".tiff", document.getId(), doc.getBytes());
			logger.warn("recoverDocument(Document) - Image created: "+img.getAbsolutePath());
		} catch (IOException e) {
			logger.error("recoverDocument(Document) - error creating images.", e);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("recoverDocument(Document) - end");
		}
		
		return img;
	}

    @Transactional("postgresql")
	public File recoverDocumento(Documento documento) {
		if (logger.isDebugEnabled()) {
			logger.debug("recoverDocumento(Documento) - start");
		}

		Documento doco = Documento.findDocumento(documento.getId());
		if (doco == null) {
			logger.error("recoverDocumento(Documento) - error, can't find DocumentO with id: "+documento.getId());
			return null;
		}
		logger.debug("recoverDocumento(Documento) - DocumentO found. "+doco.getId());
		
		File img = null;
		try {
			img = createImage(new File("src/test/resources/images"), "postgresqltiff", ".tiff", documento.getId(), doco.getOctetos());
			logger.error("recoverDocumento(Documento) - Image created: "+img.getAbsolutePath());
		} catch (IOException e) {
			logger.error("recoverDocumento(Documento) - error creating images.", e);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("recoverDocumento(Documento) - end");
		}
		
		return img;
	}
	
	private File createImage(File path, String name, String extension, Long id, byte[] data) throws IOException {
		File newTiff = new File(path.getPath()+File.separator+name+extension);
		Long sequential = 0L;
		while (newTiff.exists()) {
			newTiff = new File(path.getPath()+File.separator+name+"_"+id+"_"+(sequential++)+".tiff");
		}
		FileUtils.writeByteArrayToFile(newTiff, data);
		return newTiff;
	} 
	
	public void doIt() {
		if (logger.isDebugEnabled()) {
			logger.debug("doIt() - start");
		}

		Documents docs = doStuff();
		logger.info("doIt() - Documents created. ");
		
		File img = recoverDocument(docs.getDocument());
		logger.info("doIt() - Document recovered "+(img!=null?img.getAbsolutePath():"NOPE"));
		img = recoverDocumento(docs.getDocumento());
		logger.info("doIt() - Documento recovered "+(img!=null?img.getAbsolutePath():"NOPE"));

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
