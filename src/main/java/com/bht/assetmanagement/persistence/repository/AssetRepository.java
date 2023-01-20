package com.bht.assetmanagement.persistence.repository;

import com.bht.assetmanagement.persistence.entity.Asset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AssetRepository extends JpaRepository<Asset, String> {
    Optional<Asset> findBySerialnumber(String serialnumber);
    Boolean existsBySerialnumber(String serialnumber);
    Optional<Asset> findById(UUID id);
}
