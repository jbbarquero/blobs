package com.malsolo.blobs.domain;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext*.xml")
public class ImageFromMySqlToDataInPostgreSqlTest {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(ImageFromMySqlToDataInPostgreSqlTest.class);
	
	@Autowired
    private DocumentDataOnDemand dod;
	
	@Autowired
    private NoDocumentoDataOnDemand nodo;

	@Test
    public void testMarkerMethod() {
    }
    
	@Test
	@Transactional
    public void testBasico() throws IOException {
    	
    	String path = "src/test/resources/images";
    	String name = "transfertiffok";
    	String extension = ".tiff";
    	
    	Document doc = dod.getRandomDocument();
    	File tiff = createImage(path, name, extension, doc.getId(), doc.getBytes());
    	
    	NoDocumento nodoc = new NoDocumento();
    	nodoc.setNombre(tiff.getName());
    	nodoc.setDescripcion(doc.getId().toString());
    	nodoc.setFichero(tiff.getAbsolutePath());
    	nodoc.setContenido("img/tiff");
    	nodoc.setLongitud(tiff.length());
    	nodoc.setUrli(tiff.toURI().toString());
    	
    	nodoc.persist();
    	nodoc.flush();
    	if (logger.isDebugEnabled()) logger.debug("Grabado NoDocumento: "+nodoc);
    	
    	NoDocumento saved = NoDocumento.findNoDocumentoesByNombreEquals(tiff.getName()).getSingleResult();
    	
    	Assert.assertNotNull("Error, documento grabado no encontrado", saved);
    	Assert.assertTrue("Error, no hay imagen para el documento grabado", (new File(saved.getFichero())).exists());
    	Assert.assertEquals("Lo que faltaba, el buscado "+saved.getId()+" no es el mismo que el guardado "+nodoc.getId(), nodoc.getId(), saved.getId());
    	
    	String cambio = "Guardado en "+nodoc.getId();
    	doc.setDescription(cambio);
    	doc.persist();
    	doc.flush();
    	
    	Document verify = Document.findDocument(doc.getId());
    	Assert.assertEquals("Document no verificado ", cambio, verify.getDescription());
    	
    }
	
	private File createImage(String path, String name, String extension, Long id, byte[] data) throws IOException {
		File newTiff = null;
		int sequential = 0;
		do {
			newTiff = new File(path+File.separator+name+"_"+id+"_"+(sequential++)+extension);
		} while (newTiff.exists());
		FileUtils.writeByteArrayToFile(newTiff, data);
		return newTiff;
	} 

	@Test
	public void testOk() {
		NoDocumento	nodoc		= nodo.getRandomNoDocumento();
		Document	doc			= dod.getRandomDocument();
		
    	String nameNoDocReverse	= StringUtils.reverse(nodoc.getNombre());
    	String nameDocReverse	= StringUtils.reverse(doc.getName());

    	persistNewNames(nodoc.getId(), nameNoDocReverse, doc.getId(), nameDocReverse);
		
    	Assert.assertEquals("testOk: NoDocumento debería haberse modificado ", nameNoDocReverse, NoDocumento.findNoDocumento(nodoc.getId()).getNombre());
    	Assert.assertEquals("testOk: Document debería haberse modificado ", nameDocReverse, Document.findDocument(doc.getId()).getName());
	}
	
	@Test
	public void testKoNoDoc() {
		NoDocumento	nodoc		= nodo.getRandomNoDocumento();
		Document	doc			= dod.getRandomDocument();
		
    	String nameNoDoc		= nodoc.getNombre();
    	String nameDoc			= doc.getName();
    	String nameNoDocReverse = null;
    	String nameDocReverse	= StringUtils.reverse(doc.getName());

    	try {
			persistNewNames(nodoc.getId(), nameNoDocReverse, doc.getId(), nameDocReverse);
			logger.info("testKoNoDoc: ¿debería pasar por aquí?");
		} catch (Exception e) {
			logger.error("testKoNoDoc: fallo, supongo que esperado: "+e.getMessage());
			e.printStackTrace();
		}
		
    	Assert.assertEquals("testOk: NoDocumento NO debería haberse modificado ", nameNoDoc, NoDocumento.findNoDocumento(nodoc.getId()).getNombre());
    	Assert.assertEquals("testOk: Document NO debería haberse modificado ", nameDoc, Document.findDocument(doc.getId()).getName());
	}

	@Test
	public void testKoDoc() {
		NoDocumento	nodoc		= nodo.getRandomNoDocumento();
		Document	doc			= dod.getRandomDocument();
		
    	String nameNoDoc		= nodoc.getNombre();
    	String nameDoc			= doc.getName();
    	String nameNoDocReverse = StringUtils.reverse(nodoc.getNombre());
    	String nameDocReverse	= null;

    	persistNewNames(nodoc.getId(), nameNoDocReverse, doc.getId(), nameDocReverse);
		
    	Assert.assertEquals("testOk: NoDocumento NO debería haberse modificado ", nameNoDoc, NoDocumento.findNoDocumento(nodoc.getId()).getNombre());
    	Assert.assertEquals("testOk: Document NO debería haberse modificado ", nameDoc, Document.findDocument(doc.getId()).getName());
	}

	@Transactional
	private void persistNewNames(Long idNoDocumento, String nameNoDoc, Long idDocument, String nameDoc) {
    	try {
			NoDocumento nodoc = NoDocumento.findNoDocumento(idNoDocumento);
			Document doc = Document.findDocument(idDocument);

			if (logger.isDebugEnabled()) logger.debug("Grabando NoDocumento... "+nodoc);
			nodoc.setNombre(nameNoDoc);
			nodoc.persist();
			nodoc.flush();
			if (logger.isDebugEnabled()) logger.debug("Grabado NoDocumento: "+nodoc);

			if (logger.isDebugEnabled()) logger.debug("Grabando Document... "+idDocument);
			doc.setName(nameDoc);
			doc.persist();
			doc.flush();
			if (logger.isDebugEnabled()) logger.debug("Grabado Document: "+doc);
		} catch (Exception e) {
			logger.error("Error persistiendo : "+e.getMessage());
			e.printStackTrace();
		}
	}


	@Test
	public void testOkInverso() {
		Document	doc			= dod.getRandomDocument();
		NoDocumento	nodoc		= nodo.getRandomNoDocumento();
		
    	String nameDocReverse	= StringUtils.reverse(doc.getName());
    	String nameNoDocReverse	= StringUtils.reverse(nodoc.getNombre());

    	persistNewNamesInverse(doc.getId(), nameDocReverse, nodoc.getId(), nameNoDocReverse);
		
    	Assert.assertEquals("testOkInverso: Document debería haberse modificado ", nameDocReverse, Document.findDocument(doc.getId()).getName());
    	Assert.assertEquals("testOkInverso: NoDocumento debería haberse modificado ", nameNoDocReverse, NoDocumento.findNoDocumento(nodoc.getId()).getNombre());
	}
	
	@Test
	public void testKoNoDocInverso() {
		Document	doc			= dod.getRandomDocument();
		NoDocumento	nodoc		= nodo.getRandomNoDocumento();
		
    	String nameDoc			= doc.getName();
    	String nameNoDoc		= nodoc.getNombre();
    	String nameDocReverse	= StringUtils.reverse(doc.getName());
    	String nameNoDocReverse = null;

    	persistNewNamesInverse(doc.getId(), nameDocReverse, nodoc.getId(), nameNoDocReverse);
		
    	Assert.assertEquals("testOk: NoDocumento NO debería haberse modificado ", nameNoDoc, NoDocumento.findNoDocumento(nodoc.getId()).getNombre());
    	Assert.assertEquals("testOk: Document NO debería haberse modificado ", nameDoc, Document.findDocument(doc.getId()).getName());
	}

	@Test
	public void testKoDocInverso() {
		Document	doc			= dod.getRandomDocument();
		NoDocumento	nodoc		= nodo.getRandomNoDocumento();
		
    	String nameDoc			= doc.getName();
    	String nameNoDoc		= nodoc.getNombre();
    	String nameDocReverse	= null;
    	String nameNoDocReverse = StringUtils.reverse(nodoc.getNombre());

    	persistNewNames(doc.getId(), nameDocReverse, nodoc.getId(), nameNoDocReverse);
		
    	Assert.assertEquals("testOk: NoDocumento NO debería haberse modificado ", nameNoDoc, NoDocumento.findNoDocumento(nodoc.getId()).getNombre());
    	Assert.assertEquals("testOk: Document NO debería haberse modificado ", nameDoc, Document.findDocument(doc.getId()).getName());
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	private void persistNewNamesInverse(Long idDocument, String nameDoc, Long idNoDocumento, String nameNoDoc) {
		try {
			Document doc = Document.findDocument(idDocument);

			if (logger.isDebugEnabled()) logger.debug("Grabando Document... "+idDocument);
			doc.setName(nameDoc);
			doc.persist();
			doc.flush();
			if (logger.isDebugEnabled()) logger.debug("Grabado Document: "+doc);

			NoDocumento nodoc = NoDocumento.findNoDocumento(idNoDocumento);
			if (logger.isDebugEnabled()) logger.debug("Grabando NoDocumento... "+nodoc);
			nodoc.setNombre(nameNoDoc);
			nodoc.persist();
			nodoc.flush();
			if (logger.isDebugEnabled()) logger.debug("Grabado NoDocumento: "+nodoc);
		} catch (Exception e) {
			logger.error("Error persistiendo INVERSO: "+e.getMessage());
			e.printStackTrace();
		}
	}
}
