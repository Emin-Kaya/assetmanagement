package com.bht.assetmanagement.persistence.repository;

import com.bht.assetmanagement.persistence.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdressRepository extends JpaRepository<Address, String> {
}
