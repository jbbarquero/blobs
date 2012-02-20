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

@RooJavaBean
@RooToString
@RooJpaActiveRecord(persistenceUnit = "persistenceUnitMy", table = "T_DOCUMENT")
public class Document {

    @NotNull
    @Size(max = 100)
    private String name;

    private String description;

    @NotNull
    private String filename;

    @NotNull
    private String contentType;

    private Long length;

    @NotNull
    @Size(max = 100)
    private String uri;

    @NotNull
    @Column(name = "bytes")
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] bytes;

}
