package com.bht.assetmanagement.core.testData;

import com.bht.assetmanagement.persistence.entity.*;
import com.bht.assetmanagement.persistence.repository.*;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor

public class TestDataService {
    private final AddressRepository addressRepository;
    private final ApplicationUserRepository applicationUserRepository;
    private final AssetInquiryRepository assetInquiryRepository;
    private final AssetRepository assetRepository;
    private final UserAccountRepository userAccountRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private static final Logger logger = LogManager.getLogger();
    private final PasswordEncoder passwordEncoder;


    public void resetAll() {
        logger.info("Start reseting data");
        assetInquiryRepository.deleteAll();
        addressRepository.deleteAll();
        applicationUserRepository.deleteAll();
        assetRepository.deleteAll();
        verificationTokenRepository.deleteAll();
        userAccountRepository.deleteAll();

        initializeTestData();
        logger.info("Finished reseting data");
    }

    public void initializeTestData() {

        Asset asset1 = new Asset();
        asset1.setName("iPhone 11 Pro");
        asset1.setCategory("Telefon");
        assetRepository.save(asset1);

        Asset asset2 = new Asset();
        asset2.setName("iPhone 12");
        asset2.setCategory("Telefon");
        assetRepository.save(asset2);

        Asset asset3 = new Asset();
        asset3.setName("iPhone 12 pro");
        asset3.setCategory("Telefon");
        assetRepository.save(asset3);

        Asset asset4 = new Asset();
        asset4.setName("MacBook Pro 16'");
        asset4.setCategory("Laptop");
        assetRepository.save(asset4);

        Asset asset5 = new Asset();
        asset5.setName("Magic Mouse");
        asset5.setCategory("Zubehör");
        assetRepository.save(asset5);

        Asset asset6 = new Asset();
        asset6.setName("Magic Keyboard");
        asset6.setCategory("Zubehör");
        assetRepository.save(asset6);


        Address address1 = new Address();
        address1.setCountry("Deutschland");
        address1.setCity("Berlin");
        address1.setPostalCode("10119");
        address1.setStreetName("Torstraße");
        address1.setStreetNumber("140");
        addressRepository.save(address1);

        Address address2 = new Address();
        address2.setCountry("Deutschland");
        address2.setCity("Berlin");
        address2.setPostalCode("10969");
        address2.setStreetName("Alexandrinenstraße");
        address2.setStreetNumber("103");
        addressRepository.save(address2);

        Address address3 = new Address();
        address3.setCountry("Deutschland");
        address3.setCity("Berlin");
        address3.setPostalCode("10245");
        address3.setStreetName("Stralauer Allee");
        address3.setStreetNumber("18");
        addressRepository.save(address3);

        Address address4 = new Address();
        address4.setCountry("Deutschland");
        address4.setCity("Berlin");
        address4.setPostalCode("13353");
        address4.setStreetName("Luxemburgerstr.");
        address4.setStreetNumber("10");
        addressRepository.save(address4);


        UserAccount userAccount1 = new UserAccount();
        userAccount1.setEmail("eminkaya@mb.de");
        userAccount1.setUsername("eminkaya");
        userAccount1.setPassword(passwordEncoder.encode("12345678"));
        userAccount1.setRole(Role.EMPLOYEE);
        userAccount1.setEnabled(true);
        userAccountRepository.save(userAccount1);

        ApplicationUser applicationUser1 = new ApplicationUser();
        applicationUser1.setEmployeeId("MA-12345");
        applicationUser1.setFirstName("Emin");
        applicationUser1.setLastName("Kaya");
        applicationUser1.setUserAccount(userAccount1);

        applicationUserRepository.save(applicationUser1);

        AssetInquiry assetInquiry1 = new AssetInquiry();
        assetInquiry1.setEntryDate(LocalDateTime.now().toString());
        assetInquiry1.setPrice(100.20);
        assetInquiry1.setLink("www.apple.com");
        assetInquiry1.setEnable(true);
        assetInquiry1.setStatus(Status.NOT_DONE);
        assetInquiry1.setOwner(applicationUser1);
        assetInquiry1.setAddress(address1);
        assetInquiry1.setAsset(asset1);
        assetInquiryRepository.save(assetInquiry1);

        UserAccount userAccount2 = new UserAccount();
        userAccount2.setEmail("eminkaya@mb.de");
        userAccount2.setUsername("eminkaya");
        userAccount2.setPassword(passwordEncoder.encode("12345678"));
        userAccount2.setRole(Role.EMPLOYEE);
        userAccount2.setEnabled(true);
        userAccountRepository.save(userAccount2);

        ApplicationUser applicationUser2 = new ApplicationUser();
        applicationUser2.setEmployeeId("MA-12345");
        applicationUser2.setFirstName("Emin");
        applicationUser2.setLastName("Kaya");
        applicationUser2.setUserAccount(userAccount2);

        applicationUserRepository.save(applicationUser2);

        AssetInquiry assetInquiry2 = new AssetInquiry();
        assetInquiry2.setEntryDate(LocalDateTime.now().toString());
        assetInquiry2.setPrice(100.20);
        assetInquiry2.setLink("www.apple.com");
        assetInquiry2.setEnable(true);
        assetInquiry2.setStatus(Status.NOT_DONE);
        assetInquiry2.setOwner(applicationUser2);
        assetInquiry2.setAddress(address2);
        assetInquiry2.setAsset(asset2);
        assetInquiryRepository.save(assetInquiry2);

        UserAccount userAccount3 = new UserAccount();
        userAccount3.setEmail("eminkaya@mb.de");
        userAccount3.setUsername("eminkaya");
        userAccount3.setPassword(passwordEncoder.encode("12345678"));
        userAccount3.setRole(Role.EMPLOYEE);
        userAccount3.setEnabled(true);
        userAccountRepository.save(userAccount3);

        ApplicationUser applicationUser3 = new ApplicationUser();
        applicationUser3.setEmployeeId("MA-12345");
        applicationUser3.setFirstName("Emin");
        applicationUser3.setLastName("Kaya");
        applicationUser3.setUserAccount(userAccount3);

        applicationUserRepository.save(applicationUser3);

        AssetInquiry assetInquiry3 = new AssetInquiry();
        assetInquiry3.setEntryDate(LocalDateTime.now().toString());
        assetInquiry3.setPrice(100.20);
        assetInquiry3.setLink("www.apple.com");
        assetInquiry3.setEnable(true);
        assetInquiry3.setStatus(Status.NOT_DONE);
        assetInquiry3.setOwner(applicationUser3);
        assetInquiry3.setAddress(address2);
        assetInquiry3.setAsset(asset2);
        assetInquiryRepository.save(assetInquiry3);

        UserAccount userAccount4 = new UserAccount();
        userAccount4.setEmail("eminkaya@mb.de");
        userAccount4.setUsername("eminkaya");
        userAccount4.setPassword(passwordEncoder.encode("12345678"));
        userAccount4.setRole(Role.EMPLOYEE);
        userAccount4.setEnabled(true);
        userAccountRepository.save(userAccount4);

        ApplicationUser applicationUser4 = new ApplicationUser();
        applicationUser4.setEmployeeId("MA-12345");
        applicationUser4.setFirstName("Emin");
        applicationUser4.setLastName("Kaya");
        applicationUser4.setUserAccount(userAccount4);

        applicationUserRepository.save(applicationUser4);

        AssetInquiry assetInquiry4 = new AssetInquiry();
        assetInquiry4.setEntryDate(LocalDateTime.now().toString());
        assetInquiry4.setPrice(100.20);
        assetInquiry4.setLink("www.apple.com");
        assetInquiry4.setEnable(true);
        assetInquiry4.setStatus(Status.NOT_DONE);
        assetInquiry4.setOwner(applicationUser4);
        assetInquiry4.setAddress(address2);
        assetInquiry4.setAsset(asset2);
        assetInquiryRepository.save(assetInquiry4);

        UserAccount userAccount5 = new UserAccount();
        userAccount5.setEmail("eminkaya@mb.de");
        userAccount5.setUsername("eminkaya");
        userAccount5.setPassword(passwordEncoder.encode("12345678"));
        userAccount5.setRole(Role.EMPLOYEE);
        userAccount5.setEnabled(true);
        userAccountRepository.save(userAccount5);

        ApplicationUser applicationUser5 = new ApplicationUser();
        applicationUser5.setEmployeeId("MA-12345");
        applicationUser5.setFirstName("Emin");
        applicationUser5.setLastName("Kaya");
        applicationUser5.setUserAccount(userAccount5);

        applicationUserRepository.save(applicationUser5);

        AssetInquiry assetInquiry5 = new AssetInquiry();
        assetInquiry5.setEntryDate(LocalDateTime.now().toString());
        assetInquiry5.setPrice(100.20);
        assetInquiry5.setLink("www.apple.com");
        assetInquiry5.setEnable(true);
        assetInquiry5.setStatus(Status.NOT_DONE);
        assetInquiry5.setOwner(applicationUser5);
        assetInquiry5.setAddress(address2);
        assetInquiry5.setAsset(asset2);
        assetInquiryRepository.save(assetInquiry5);

        UserAccount userAccount6 = new UserAccount();
        userAccount6.setEmail("eminkaya@mb.de");
        userAccount6.setUsername("eminkaya");
        userAccount6.setPassword(passwordEncoder.encode("12345678"));
        userAccount6.setRole(Role.EMPLOYEE);
        userAccount6.setEnabled(true);
        userAccountRepository.save(userAccount6);

        ApplicationUser applicationUser6 = new ApplicationUser();
        applicationUser6.setEmployeeId("MA-12345");
        applicationUser6.setFirstName("Emin");
        applicationUser6.setLastName("Kaya");
        applicationUser6.setUserAccount(userAccount6);

        applicationUserRepository.save(applicationUser6);

        AssetInquiry assetInquiry6 = new AssetInquiry();
        assetInquiry6.setEntryDate(LocalDateTime.now().toString());
        assetInquiry6.setPrice(100.20);
        assetInquiry6.setLink("www.apple.com");
        assetInquiry6.setEnable(true);
        assetInquiry6.setStatus(Status.NOT_DONE);
        assetInquiry6.setOwner(applicationUser6);
        assetInquiry6.setAddress(address2);
        assetInquiry6.setAsset(asset2);
        assetInquiryRepository.save(assetInquiry6);

        UserAccount userAccount7 = new UserAccount();
        userAccount7.setEmail("eminkaya@mb.de");
        userAccount7.setUsername("eminkaya");
        userAccount7.setPassword(passwordEncoder.encode("12345678"));
        userAccount7.setRole(Role.EMPLOYEE);
        userAccount7.setEnabled(true);
        userAccountRepository.save(userAccount7);

        ApplicationUser applicationUser7 = new ApplicationUser();
        applicationUser7.setEmployeeId("MA-12345");
        applicationUser7.setFirstName("Emin");
        applicationUser7.setLastName("Kaya");
        applicationUser7.setUserAccount(userAccount7);

        applicationUserRepository.save(applicationUser7);

        AssetInquiry assetInquiry7 = new AssetInquiry();
        assetInquiry7.setEntryDate(LocalDateTime.now().toString());
        assetInquiry7.setPrice(100.20);
        assetInquiry7.setLink("www.apple.com");
        assetInquiry7.setEnable(true);
        assetInquiry7.setStatus(Status.NOT_DONE);
        assetInquiry7.setOwner(applicationUser7);
        assetInquiry7.setAddress(address2);
        assetInquiry7.setAsset(asset2);
        assetInquiryRepository.save(assetInquiry7);

    }
}
