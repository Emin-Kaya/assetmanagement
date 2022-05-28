package com.bht.assetmanagement.persistence.repository;

import com.bht.assetmanagement.persistence.entity.Asset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AssetRepository extends JpaRepository<Asset, String> {
    Optional<Asset> findByName (String name);
}
