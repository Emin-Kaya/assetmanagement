package com.example.assetmanagement.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;
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
    private UUID assetRequestId;

    @Column
    private LocalDate entryDate;

    @Column
    private String note;

    @Column
    private boolean enable;

    @OneToMany(mappedBy = "assetInquiry")
    private Set<Asset> assets;
}
