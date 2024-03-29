package com.bht.assetmanagement.persistence.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "asset")
@NoArgsConstructor
@Getter
@Setter
public class Asset {

    @Id
    @Column(nullable = false, updatable = false)
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @GeneratedValue(generator = "UUID")
    private UUID id;

    @Column(nullable = false, unique = true)
    private String serialnumber;

    @Column(nullable = false)
    private String name;

    @Column
    private String memory = null;

    @Column
    private String notes = null;

    @Column
    private boolean enable;

    @Column(nullable = false)
    private String category;

    @Column
    private boolean isArchived = false;
}
