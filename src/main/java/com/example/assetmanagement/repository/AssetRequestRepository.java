package com.example.assetmanagement.repository;

import com.example.assetmanagement.entity.AssetInquiry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssetRequestRepository extends JpaRepository<AssetInquiry, String> {
}
