package com.malsolo.blobs.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.malsolo.blobs.domain.Document;
import com.malsolo.blobs.domain.Documento;

public class DocumentosServiceImpl {
	
	@Autowired
	private DocumentService documentService;

	@Autowired
	private DocumentoService documentoService;
	
	/**
	 * MySQL then PostgreSQL
	 * @param document
	 * @param documento
	 */
	public int[] saveDocumentos(Document document, Documento documento) throws RuntimeException {
		documentService.saveDocument(document);
		if (document.getId() == null)
			throw new RuntimeException("Can't save documenT: "+(document==null?"null":document));
		documentoService.saveDocumento(documento);
		if (documento.getId() == null)
			throw new RuntimeException("Can't save documentO: "+(documento==null?"null":documento));
		return new int[] {document.getId().intValue(), documento.getId().intValue()};
	}
	
	/**
	 * PostgreSQL then MySQL
	 * @param documento
	 * @param document
	 */
	public int[] saveDocumentos(Documento documento, Document document) {
		documentoService.saveDocumento(documento);
		if (documento.getId() == null)
			throw new RuntimeException("Can't save documentO: "+(documento==null?"null":documento));
		documentService.saveDocument(document);
		if (document.getId() == null)
			throw new RuntimeException("Can't save documenT: "+(document==null?"null":document));
		return new int[] {document.getId().intValue(), documento.getId().intValue()};
	}

}
