package com.waes.test.v1.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.MappedSuperclass;

@Data
@EqualsAndHashCode
@MappedSuperclass
public class Binary {

    @Id
    protected Long id;
    @Lob
    @Column(name = "data", columnDefinition = "BLOB")
    protected byte[] data;
}
