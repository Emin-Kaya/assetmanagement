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
    private UUID assetId;

    @Column
    private String name;

    @Column
    private String category;

   /* @OneToOne
    @JoinColumn(name = "inquiry_id")
    private AssetInquiry assetInquiry;*/

}
