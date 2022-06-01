package com.bht.assetmanagement.persistence.repository;

import com.bht.assetmanagement.persistence.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, String> {
    Optional<Address> findAddressByStreetNameAndStreetNumberAndPostalCode(String streetName, String streetNumber, String postalCode);
}
