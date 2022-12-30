package com.bht.assetmanagement.persistence.repository;

import com.bht.assetmanagement.persistence.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AssetUserHistoryRepository extends JpaRepository<AssetUserHistory, String> {
    Optional<AssetUserHistory> findByApplicationUserAndAssetAndLendStatus(ApplicationUser applicationUser, Asset asset, LendStatus lendStatus);
}
