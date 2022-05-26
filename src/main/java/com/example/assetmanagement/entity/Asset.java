package com.example.assetmanagement.entity;

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
    private byte[] image;

    @Column
    private String category;

    @Column
    private String price;

    @Column
    private String link;

    @ManyToOne
    @JoinColumn(name = "address_id")
    private Address deliveryAddress;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private ApplicationUser assetOwner;

    @ManyToOne
    @JoinColumn(name = "inquiry_id")
    private AssetInquiry assetInquiry;

}
