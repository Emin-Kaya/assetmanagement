package com.example.assetmanagement.repository;

import com.example.assetmanagement.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdressRepository extends JpaRepository<Address, String> {
}
