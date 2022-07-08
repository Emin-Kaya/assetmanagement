package com.bht.assetmanagement.persistence.repository;

import com.bht.assetmanagement.persistence.entity.ApplicationUser;
import com.bht.assetmanagement.persistence.entity.Asset;
import com.bht.assetmanagement.persistence.entity.AssetUserHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AssetUserHistoryRepository extends JpaRepository<AssetUserHistory, String> {
    Optional<AssetUserHistory> findByApplicationUserAndAsset(ApplicationUser applicationUser, Asset asset);
}
