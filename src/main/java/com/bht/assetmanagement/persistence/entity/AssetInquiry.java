package com.bht.assetmanagement.persistence.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "asset_inquiry")
@NoArgsConstructor
@Getter
@Setter
public class AssetInquiry {

    @Id
    @Column(nullable = false, updatable = false)
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @GeneratedValue(generator = "UUID")
    private UUID id;

    @Column
    private String entryDate;

    @Column
    private String note;

    @Column
    private String link;

    @Column
    private boolean enable;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "application_user_id")
    private ApplicationUser owner;

    @OneToOne
    @JoinColumn(name = "address_id")
    private Address address;

    @Column
    private String assetName;

    @Column
    private String assetCategory;

    @Column
    private String memory;

    @Column
    private boolean isArchived = false;
}
