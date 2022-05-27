package com.bht.assetmanagement.persistence.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "application_user")
@NoArgsConstructor
@Getter
@Setter
public class ApplicationUser {

    @Id
    @Column(nullable = false, updatable = false)
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @GeneratedValue(generator = "UUID")
    private UUID userId;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    /*@OneToMany(mappedBy = "owner")
    private Set<AssetInquiry> assetInquiries;*/

    @OneToOne
    @JoinColumn(name = "account_id")
    private UserAccount userAccount;
}
