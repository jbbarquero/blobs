package com.malsolo.blobs.domain;

import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Transactional;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(table = "T_DOCUMENTO", persistenceUnit = "persistenceUnitPg")
public class Documento {

    @NotNull
    @Size(max = 100)
    private String nombre;

    private String descripcion;

    @NotNull
    private String fichero;

    @NotNull
    private String tipoContenido;

    private Long longitud;

    @NotNull
    @Size(max = 100)
    private String uri;

    @NotNull
    @Column(name = "octetos")
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] octetos;

    @Transactional
	public static Documento findDocumento(Long id) {
        if (id == null) return null;
        return entityManager().find(Documento.class, id);
    }

    @Transactional
    public static long countDocumentoes() {
        //return entityManager().createQuery("SELECT COUNT(o) FROM Documento o", Long.class).getSingleResult();
        TypedQuery<Long> query = entityManager().createQuery("SELECT COUNT(o) FROM Documento o", Long.class);
        return query.getSingleResult();
    }

    @Transactional
	public static List<Documento> findDocumentoEntries(int firstResult, int maxResults) {
    	TypedQuery<Documento> query = entityManager().createQuery("SELECT o FROM Documento o", Documento.class).setFirstResult(firstResult).setMaxResults(maxResults);
        return query.getResultList();
    }
}
