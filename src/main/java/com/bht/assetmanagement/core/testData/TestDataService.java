package com.bht.assetmanagement.core.testData;

import com.bht.assetmanagement.persistence.entity.*;
import com.bht.assetmanagement.persistence.repository.*;
import com.bht.assetmanagement.shared.date.DateUtils;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.weaver.ast.Not;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class TestDataService {
    private final AddressRepository addressRepository;
    private final ApplicationUserRepository applicationUserRepository;
    private final AssetInquiryRepository assetInquiryRepository;
    private final AssetRepository assetRepository;
    private final UserAccountRepository userAccountRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final AssetUserHistoryRepository assetUserHistoryRepository;
    private final StorageRepository storageRepository;
    private static final Logger logger = LogManager.getLogger();
    private final DateUtils dateUtils;
    private final PasswordEncoder passwordEncoder;


    public void resetAll() {
        logger.info("Start reseting data");
        assetUserHistoryRepository.deleteAll();
        storageRepository.deleteAll();
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

        Address address = new Address();
        address.setStreetName("Naunynstr.");
        address.setStreetNumber("8");
        address.setPostalCode("10997");
        address.setCity("Berlin");
        address.setCountry("Deutschland");
        addressRepository.save(address);

        Address address1 = new Address();
        address1.setStreetName("Kopfstraße");
        address1.setStreetNumber("45");
        address1.setPostalCode("10115");
        address1.setCity("Berlin");
        address1.setCountry("Deutschland");
        addressRepository.save(address1);


        Address address2 = new Address();
        address2.setStreetName("Luxumburgerstr");
        address2.setStreetNumber("10");
        address2.setPostalCode("13353");
        address2.setCity("Berlin");
        address2.setCountry("Deutschland");
        addressRepository.save(address2);


        Asset asset = new Asset();
        asset.setName("iPhone 11 Pro");
        asset.setCategory("Telefon");
        assetRepository.save(asset);

        Asset asset1 = new Asset();
        asset1.setName("iPhone 12 Pro");
        asset1.setCategory("Telefon");
        assetRepository.save(asset1);

        Asset asset2 = new Asset();
        asset2.setName("iPhone 13 Pro");
        asset2.setCategory("Telefon");
        assetRepository.save(asset2);

        Asset asset3 = new Asset();
        asset3.setName("iPad Pro");
        asset3.setCategory("Tablet");
        assetRepository.save(asset3);

        Asset asset4 = new Asset();
        asset4.setName("MacBook Pro M1");
        asset4.setCategory("Laptop");
        assetRepository.save(asset4);

        Asset asset5 = new Asset();
        asset5.setName("Magic Mouse");
        asset5.setCategory("Zubehör");
        assetRepository.save(asset5);

        Asset asset6 = new Asset();
        asset6.setName("Adobe Creative Cloud");
        asset6.setCategory("Lizensen");
        assetRepository.save(asset6);


        Storage storage = new Storage();
        storage.setName("Storage-1");
        storage.getAssets().add(asset1);
        storage.getAssets().add(asset1);
        storage.getAssets().add(asset2);
        storage.getAssets().add(asset3);
        storage.getAssets().add(asset4);

        storageRepository.save(storage);

        Storage storage1 = new Storage();
        storage1.setName("Storage-2");
        storage1.getAssets().add(asset4);
        storage1.getAssets().add(asset4);
        storage.getAssets().add(asset5);
        storage.getAssets().add(asset6);

        storageRepository.save(storage1);


        UserAccount userAccount = new UserAccount();
        userAccount.setEmail("admin@myassets.de");
        userAccount.setUsername("Admin");
        userAccount.setPassword(passwordEncoder.encode("12345678"));
        userAccount.setRole(Role.ADMIN);
        userAccount.setEnabled(true);
        userAccountRepository.save(userAccount);

        ApplicationUser applicationUser = new ApplicationUser();
        applicationUser.setFirstName("Admin");
        applicationUser.setLastName("Admin");
        applicationUser.setEmployeeId("Admin-ID");
        applicationUser.setUserAccount(userAccount);
        applicationUserRepository.save(applicationUser);


        UserAccount userAccount1 = new UserAccount();
        userAccount1.setEmail("adelevance@myassets.de");
        userAccount1.setUsername("adelevance");
        userAccount1.setPassword(passwordEncoder.encode("12345678"));
        userAccount1.setRole(Role.MANAGER);
        userAccount1.setEnabled(true);
        userAccountRepository.save(userAccount1);

        ApplicationUser applicationUser1 = new ApplicationUser();
        applicationUser1.setFirstName("Adele");
        applicationUser1.setLastName("Vance");
        applicationUser1.setEmployeeId("MA-12345");
        applicationUser1.setUserAccount(userAccount1);
        applicationUserRepository.save(applicationUser1);

        UserAccount userAccount2 = new UserAccount();
        userAccount2.setEmail("alexwilber@myassets.de");
        userAccount2.setUsername("alexwilber");
        userAccount2.setPassword(passwordEncoder.encode("12345678"));
        userAccount2.setRole(Role.MANAGER);
        userAccount2.setEnabled(true);
        userAccountRepository.save(userAccount2);

        ApplicationUser applicationUser2 = new ApplicationUser();
        applicationUser2.setFirstName("Alex");
        applicationUser2.setLastName("Wilber");
        applicationUser2.setEmployeeId("MA-39122");
        applicationUser2.setUserAccount(userAccount2);
        applicationUserRepository.save(applicationUser2);

        UserAccount userAccount3 = new UserAccount();
        userAccount3.setEmail("biancapisani@myassets.de");
        userAccount3.setUsername("biancapisani");
        userAccount3.setPassword(passwordEncoder.encode("12345678"));
        userAccount3.setRole(Role.EMPLOYEE);
        userAccount3.setEnabled(true);
        userAccountRepository.save(userAccount3);

        ApplicationUser applicationUser3 = new ApplicationUser();
        applicationUser3.setFirstName("Bianca");
        applicationUser3.setLastName("Pisani");
        applicationUser3.setEmployeeId("MA-21391");
        applicationUser3.setUserAccount(userAccount3);
        applicationUserRepository.save(applicationUser3);

        UserAccount userAccount4 = new UserAccount();
        userAccount4.setEmail("allendeyoung@myassets.de");
        userAccount4.setUsername("allendeyoung");
        userAccount4.setPassword(passwordEncoder.encode("12345678"));
        userAccount4.setRole(Role.EMPLOYEE);
        userAccount4.setEnabled(true);
        userAccountRepository.save(userAccount4);

        ApplicationUser applicationUser4 = new ApplicationUser();
        applicationUser4.setFirstName("Allen");
        applicationUser4.setLastName("Deyoung");
        applicationUser4.setEmployeeId("MA-31312");
        applicationUser4.setUserAccount(userAccount4);
        applicationUserRepository.save(applicationUser4);


        UserAccount userAccount5 = new UserAccount();
        userAccount5.setEmail("hannahleser@myassets.de");
        userAccount5.setUsername("hannahleser");
        userAccount5.setPassword(passwordEncoder.encode("12345678"));
        userAccount5.setRole(Role.EMPLOYEE);
        userAccount5.setEnabled(true);
        userAccountRepository.save(userAccount5);

        ApplicationUser applicationUser5 = new ApplicationUser();
        applicationUser5.setFirstName("Hannah");
        applicationUser5.setLastName("Leser");
        applicationUser5.setEmployeeId("MA-21231");
        applicationUser5.setUserAccount(userAccount5);
        applicationUserRepository.save(applicationUser5);

        UserAccount userAccount6 = new UserAccount();
        userAccount6.setEmail("chirstiecline@myassets.de");
        userAccount6.setUsername("chirstiecline");
        userAccount6.setPassword(passwordEncoder.encode("12345678"));
        userAccount6.setRole(Role.EMPLOYEE);
        userAccount6.setEnabled(true);
        userAccountRepository.save(userAccount6);

        ApplicationUser applicationUser6 = new ApplicationUser();
        applicationUser6.setFirstName("Christie");
        applicationUser6.setLastName("Cline");
        applicationUser6.setEmployeeId("MA-21039");
        applicationUser6.setUserAccount(userAccount6);
        applicationUserRepository.save(applicationUser6);


        UserAccount userAccount7 = new UserAccount();
        userAccount7.setEmail("enricogustavo@myassets.de");
        userAccount7.setUsername("enricogustavo");
        userAccount7.setPassword(passwordEncoder.encode("12345678"));
        userAccount7.setRole(Role.EMPLOYEE);
        userAccount7.setEnabled(true);
        userAccountRepository.save(userAccount7);

        ApplicationUser applicationUser7 = new ApplicationUser();
        applicationUser7.setFirstName("Enrico");
        applicationUser7.setLastName("Gustavo");
        applicationUser7.setEmployeeId("MA-10129");
        applicationUser7.setUserAccount(userAccount7);
        applicationUserRepository.save(applicationUser7);

        AssetUserHistory assetUserHistory1 = new AssetUserHistory();
        assetUserHistory1.setAsset(asset);
        assetUserHistory1.setLendStatus(LendStatus.RENTED);
        assetUserHistory1.setRendDate(dateUtils.createLocalDate());
        assetUserHistory1.setApplicationUser(applicationUser1);
        assetUserHistoryRepository.save(assetUserHistory1);

        AssetUserHistory assetUserHistory2 = new AssetUserHistory();
        assetUserHistory2.setAsset(asset4);
        assetUserHistory2.setLendStatus(LendStatus.RENTED);
        assetUserHistory2.setRendDate(dateUtils.createLocalDate());
        assetUserHistory2.setApplicationUser(applicationUser1);
        assetUserHistoryRepository.save(assetUserHistory2);

        AssetUserHistory assetUserHistory3 = new AssetUserHistory();
        assetUserHistory3.setAsset(asset);
        assetUserHistory3.setLendStatus(LendStatus.RENTED);
        assetUserHistory3.setRendDate(dateUtils.createLocalDate());
        assetUserHistory3.setApplicationUser(applicationUser2);
        assetUserHistoryRepository.save(assetUserHistory3);

        AssetUserHistory assetUserHistory4 = new AssetUserHistory();
        assetUserHistory4.setAsset(asset4);
        assetUserHistory4.setLendStatus(LendStatus.RENTED);
        assetUserHistory4.setRendDate(dateUtils.createLocalDate());
        assetUserHistory4.setApplicationUser(applicationUser2);
        assetUserHistoryRepository.save(assetUserHistory4);

        AssetUserHistory assetUserHistory5 = new AssetUserHistory();
        assetUserHistory5.setAsset(asset);
        assetUserHistory5.setLendStatus(LendStatus.RENTED);
        assetUserHistory5.setRendDate(dateUtils.createLocalDate());
        assetUserHistory5.setApplicationUser(applicationUser3);
        assetUserHistoryRepository.save(assetUserHistory5);

        AssetUserHistory assetUserHistory6 = new AssetUserHistory();
        assetUserHistory6.setAsset(asset4);
        assetUserHistory6.setLendStatus(LendStatus.RENTED);
        assetUserHistory6.setRendDate(dateUtils.createLocalDate());
        assetUserHistory6.setApplicationUser(applicationUser3);
        assetUserHistoryRepository.save(assetUserHistory6);

        AssetUserHistory assetUserHistory7 = new AssetUserHistory();
        assetUserHistory7.setAsset(asset);
        assetUserHistory7.setLendStatus(LendStatus.RENTED);
        assetUserHistory7.setRendDate(dateUtils.createLocalDate());
        assetUserHistory7.setApplicationUser(applicationUser4);
        assetUserHistoryRepository.save(assetUserHistory7);

        AssetUserHistory assetUserHistory8 = new AssetUserHistory();
        assetUserHistory8.setAsset(asset4);
        assetUserHistory8.setLendStatus(LendStatus.RENTED);
        assetUserHistory8.setRendDate(dateUtils.createLocalDate());
        assetUserHistory8.setApplicationUser(applicationUser4);
        assetUserHistoryRepository.save(assetUserHistory8);

        AssetUserHistory assetUserHistory9 = new AssetUserHistory();
        assetUserHistory9.setAsset(asset);
        assetUserHistory9.setLendStatus(LendStatus.RENTED);
        assetUserHistory9.setRendDate(dateUtils.createLocalDate());
        assetUserHistory9.setApplicationUser(applicationUser5);
        assetUserHistoryRepository.save(assetUserHistory9);

        AssetUserHistory assetUserHistory10 = new AssetUserHistory();
        assetUserHistory10.setAsset(asset4);
        assetUserHistory10.setLendStatus(LendStatus.RENTED);
        assetUserHistory10.setRendDate(dateUtils.createLocalDate());
        assetUserHistory10.setApplicationUser(applicationUser5);
        assetUserHistoryRepository.save(assetUserHistory10);


        AssetInquiry assetInquiry = new AssetInquiry();
        assetInquiry.setEntryDate(dateUtils.createLocalDate());
        assetInquiry.setNote("Bitte die 256GB Variante");
        assetInquiry.setPrice(900.0);
        assetInquiry.setLink("www.apple.com");
        assetInquiry.setEnable(false);
        assetInquiry.setStatus(Status.NOT_DONE);
        assetInquiry.setOwner(applicationUser4);
        assetInquiry.setAddress(address);
        assetInquiry.setAssetName("MacBook Pro M1");
        assetInquiry.setAssetCategory("Laptop");
        assetInquiryRepository.save(assetInquiry);

        AssetInquiry assetInquiry1 = new AssetInquiry();
        assetInquiry1.setEntryDate(dateUtils.createLocalDate());
        assetInquiry1.setNote("Bitte die 512GB Variante");
        assetInquiry1.setPrice(1200.99);
        assetInquiry1.setLink("www.apple.com");
        assetInquiry1.setEnable(false);
        assetInquiry1.setStatus(Status.NOT_DONE);
        assetInquiry1.setOwner(applicationUser6);
        assetInquiry1.setAddress(address);
        assetInquiry1.setAssetName("MacBook Pro M1");
        assetInquiry1.setAssetCategory("Laptop");
        assetInquiryRepository.save(assetInquiry1);

        AssetInquiry assetInquiry2 = new AssetInquiry();
        assetInquiry2.setEntryDate(dateUtils.createLocalDate());
        assetInquiry2.setNote("Bitte in Schwart");
        assetInquiry2.setPrice(79.99);
        assetInquiry2.setLink("www.apple.com");
        assetInquiry2.setEnable(false);
        assetInquiry2.setStatus(Status.NOT_DONE);
        assetInquiry2.setOwner(applicationUser6);
        assetInquiry2.setAddress(address2);
        assetInquiry2.setAssetName("Magic Mouse");
        assetInquiry2.setAssetCategory("Zubehör");
        assetInquiryRepository.save(assetInquiry2);

        AssetInquiry assetInquiry3 = new AssetInquiry();
        assetInquiry3.setEntryDate(dateUtils.createLocalDate());
        assetInquiry3.setNote("Bitte die 64GB Variante");
        assetInquiry3.setPrice(799.99);
        assetInquiry3.setLink("www.apple.com");
        assetInquiry3.setEnable(false);
        assetInquiry3.setStatus(Status.NOT_DONE);
        assetInquiry3.setOwner(applicationUser7);
        assetInquiry3.setAddress(address1);
        assetInquiry3.setAssetName("iPhone 12");
        assetInquiry3.setAssetCategory("Telefon");
        assetInquiryRepository.save(assetInquiry3);

        AssetInquiry assetInquiry4 = new AssetInquiry();
        assetInquiry4.setEntryDate(dateUtils.createLocalDate());
        assetInquiry4.setNote("Bitte in 16 Zoll");
        assetInquiry4.setPrice(2500.00);
        assetInquiry4.setLink("www.apple.com");
        assetInquiry4.setEnable(false);
        assetInquiry4.setStatus(Status.NOT_DONE);
        assetInquiry4.setOwner(applicationUser7);
        assetInquiry4.setAddress(address2);
        assetInquiry4.setAssetName("MacBook Pro M1");
        assetInquiry4.setAssetCategory("Laptop");
        assetInquiryRepository.save(assetInquiry4);
    }
}
