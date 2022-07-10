package com.bht.assetmanagement.persistence.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "asset_user_history")
@NoArgsConstructor
@Getter
@Setter
public class AssetUserHistory {
    @Id
    @Column(nullable = false, updatable = false)
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @GeneratedValue(generator = "UUID")
    private UUID id;

    @ManyToOne
    private ApplicationUser applicationUser;

    @ManyToOne
    private Asset asset;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LendStatus lendStatus;

    @Column(nullable = false)
    private java.lang.String rendDate;

    @Column
    private java.lang.String returnDate;
}
