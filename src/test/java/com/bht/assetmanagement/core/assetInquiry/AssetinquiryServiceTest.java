package com.bht.assetmanagement.core.assetInquiry;


import com.bht.assetmanagement.persistence.dto.AssetInquiryDto;
import com.bht.assetmanagement.persistence.entity.*;
import com.bht.assetmanagement.persistence.repository.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(
        properties = {
                "spring.jpa.hibernate.ddl-auto=validate",
                "spring.liquibase.enabled=false"
        })
public class AssetinquiryServiceTest {

    @Autowired
    private AssetInquiryService assetInquiryService;

    @Autowired
    private AssetInquiryRepository assetInquiryRepository;

    @Autowired
    private AssetRepository assetRepository;

    @Autowired
    private ApplicationUserRepository applicationUserRepository;

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private AddressRepository addressRepository;

    private UserAccount userAccount;


    @BeforeEach
    void deleteAll() {
        assetInquiryRepository.deleteAll();
        assetRepository.deleteAll();
        applicationUserRepository.deleteAll();
        userAccountRepository.deleteAll();
        addressRepository.deleteAll();
        userAccount = aValidUserAccount();
    }

    @Test
    void createAssetInquiry() {

    }

    @Test
    void getAllAssetInquiryTest() {
        aAssetInquiry();
        List<AssetInquiryDto> l = assetInquiryService.getAllAssetInquiry();
        Assertions.assertEquals(l.size(), 1);
    }

    private AssetInquiry aAssetInquiry() {
        AssetInquiry assetInquiry = new AssetInquiry();
        assetInquiry.setId(UUID.randomUUID());
        assetInquiry.setEntryDate("10.09.2022");
        assetInquiry.setNote("");
        assetInquiry.setPrice(10.20);
        assetInquiry.setLink("");
        assetInquiry.setEnable(false);
        assetInquiry.setStatus(Status.NOT_DONE);
        assetInquiry.setAddress(aAddress());
        assetInquiry.setOwner(aApplicationUser());
        assetInquiry.setAsset(aAsset());

        return assetInquiryRepository.save(assetInquiry);
    }

    private Asset aAsset() {
        Asset asset = new Asset();
        asset.setId(UUID.randomUUID());
        asset.setName("testAsset");
        asset.setCategory("testCategory");
        return assetRepository.save(asset);
    }

    private ApplicationUser aApplicationUser() {
        ApplicationUser applicationUser = new ApplicationUser();
        applicationUser.setId(UUID.randomUUID());
        applicationUser.setEmployeeId("MA-12345");
        applicationUser.setFirstName("Test");
        applicationUser.setLastName("Tester");
        applicationUser.setUserAccount(userAccount);
        return applicationUserRepository.save(applicationUser);
    }

    private UserAccount aValidUserAccount() {
        UserAccount userAccount = new UserAccount();
        userAccount.setId(UUID.randomUUID());
        userAccount.setUsername("testtester");
        userAccount.setEmail("test@mail.de");
        userAccount.setPassword("12345678");
        userAccount.setRole(Role.EMPLOYEE);
        userAccount.setEnabled(true);

        return userAccountRepository.save(userAccount);
    }


    private Address aAddress() {
        Address address = new Address();

        address.setId(UUID.randomUUID());
        address.setStreetName("Naunynstr.");
        address.setStreetNumber("8");
        address.setCity("Berlin");
        address.setPostalCode("10997");
        address.setCountry("Germany");
        return addressRepository.save(address);
    }
}
