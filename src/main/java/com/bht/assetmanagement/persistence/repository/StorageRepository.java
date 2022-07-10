package com.bht.assetmanagement.persistence.repository;

import com.bht.assetmanagement.persistence.entity.Storage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface StorageRepository extends JpaRepository<Storage, String> {
    Boolean existsByName(String name);
    Optional<Storage> findById(UUID id);

}
