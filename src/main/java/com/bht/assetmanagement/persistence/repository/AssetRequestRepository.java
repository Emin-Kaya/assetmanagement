package com.bht.assetmanagement.persistence.repository;

import com.bht.assetmanagement.persistence.entity.AssetInquiry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssetRequestRepository extends JpaRepository<AssetInquiry, String> {
}
