package com.malsolo.blobs.domain;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Transactional;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(table = "T_DOCUMENTO", persistenceUnit = "persistenceUnitPg", transactionManager = "postgresql")
public class Documento {

    @NotNull
    @Size(max = 100)
    private String nombre;

    private String descripcion;

    @NotNull
    private String fichero;

    @NotNull
    @Column(name = "datos")
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] datos;

    @NotNull
    private String contenido;

    private Long longitud;

    @NotNull
    @Size(max = 100)
    private String urli;

    @Transactional("postgresql")
	public static Documento findDocumento(Long id) {
        if (id == null) return null;
        return entityManager().find(Documento.class, id);
    }
}
