package com.bht.assetmanagement.persistence.repository;

import com.bht.assetmanagement.persistence.entity.ApplicationUser;
import com.bht.assetmanagement.persistence.entity.Asset;
import com.bht.assetmanagement.persistence.entity.AssetInquiry;
import com.bht.assetmanagement.persistence.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface AssetInquiryRepository extends JpaRepository<AssetInquiry, String> {
    Set<AssetInquiry> findAllByOwner(ApplicationUser owner);
    AssetInquiry findByAssetInquiryId(UUID assetInquiryID);
}
