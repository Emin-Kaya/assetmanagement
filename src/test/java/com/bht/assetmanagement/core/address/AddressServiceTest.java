package com.bht.assetmanagement.core.address;


import com.bht.assetmanagement.persistence.dto.AddressRequest;
import com.bht.assetmanagement.persistence.entity.Address;
import com.bht.assetmanagement.persistence.repository.AddressRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(
        properties = {
                "spring.liquibase.enabled=false"
        }
)
class AddressServiceTest {

    @Autowired
    private AddressService addressService;

    @Autowired
    private AddressRepository addressRepository;

    @BeforeEach
    void deleteAll() {
        addressRepository.deleteAll();
    }

    @Test
    void getNewAddress() {
        Address actual = addressService.getAddress(aAddressRequest());
        Address expected = addressRepository.findAll().get(0);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void getExistingAddress() {
        Address expected = addressRepository.save(aAddress());

        Address actual = addressService.getAddress(aAddressRequest());

        Assertions.assertEquals(expected, actual);
    }

    private AddressRequest aAddressRequest() {
        AddressRequest addressRequest = new AddressRequest();

        addressRequest.setStreetName("Naunynstr.");
        addressRequest.setStreetNumber("8");
        addressRequest.setCity("Berlin");
        addressRequest.setPostalCode("10997");
        addressRequest.setCountry("Germany");
        return addressRequest;
    }

    private Address aAddress() {
        Address address = new Address();

        address.setId(UUID.randomUUID());
        address.setStreetName("Naunynstr.");
        address.setStreetNumber("8");
        address.setCity("Berlin");
        address.setPostalCode("10997");
        address.setCountry("Germany");
        return address;
    }
}
