package com.bht.assetmanagement;

import com.bht.assetmanagement.persistence.dto.*;
import com.bht.assetmanagement.persistence.entity.*;
import com.bht.assetmanagement.persistence.repository.*;
import lombok.RequiredArgsConstructor;
import org.apache.juli.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.*;
import java.util.UUID;


@Service
public class TestDataBuilder {

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private ApplicationUserRepository applicationUserRepository;

    @Autowired
    private StorageRepository storageRepository;

    @Autowired
    private AssetRepository assetRepository;

    @Autowired
    private AssetInquiryRepository assetInquiryRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public AddressRequest aAddressRequest() {
        AddressRequest addressRequest = new AddressRequest();

        addressRequest.setStreetName("Naunynstr.");
        addressRequest.setStreetNumber("8");
        addressRequest.setCity("Berlin");
        addressRequest.setPostalCode("10997");
        addressRequest.setCountry("Germany");
        return addressRequest;
    }

    public Address aAddress() {
        Address address = new Address();

        address.setId(UUID.randomUUID());
        address.setStreetName("Naunynstr.");
        address.setStreetNumber("8");
        address.setCity("Berlin");
        address.setPostalCode("10997");
        address.setCountry("Germany");
        return addressRepository.save(address);
    }

    public UserAccount aValidUserAccount(String role) {
        UserAccount userAccount = new UserAccount();
        userAccount.setId(UUID.randomUUID());
        userAccount.setUsername("validUsername");
        userAccount.setEmail("validEmail@mail.de");
        userAccount.setPassword("12345678");
        userAccount.setEnabled(true);
        userAccount.setRole(Role.valueOf(role));

        return userAccountRepository.save(userAccount);
    }

    public UserAccountRequest aValidUserAccountRequest(){
        return UserAccountRequest.builder()
                .email("aEmail@email.de")
                .username("aValidUsername")
                .password("aValidPassword")
                .role(Role.MANAGER)
                .build();
    }


    public UserAccount aValidAdminUserAccount() {
        UserAccount userAccount = new UserAccount();
        userAccount.setId(UUID.randomUUID());
        userAccount.setUsername("maxplank");
        userAccount.setEmail("maxplank@mail.de");
        userAccount.setPassword("12345678");
        userAccount.setEnabled(true);
        userAccount.setRole(Role.ADMIN);

        return userAccountRepository.save(userAccount);
    }

    public ApplicationUser aValidAdminApplicationUser() {
        ApplicationUser applicationUser = new ApplicationUser();
        applicationUser.setFirstName("Max");
        applicationUser.setLastName("Plank");
        applicationUser.setEmployeeId("MA-1234");
        applicationUser.setUserAccount(aValidAdminUserAccount());

        return applicationUserRepository.save(applicationUser);
    }

    public UserAccount aValidManagerUserAccount() {
        UserAccount userAccount = new UserAccount();
        userAccount.setId(UUID.randomUUID());
        userAccount.setUsername("fritzleser");
        userAccount.setEmail("fritzleser@mail.de");
        userAccount.setPassword("12345678");
        userAccount.setEnabled(true);
        userAccount.setRole(Role.MANAGER);

        return userAccountRepository.save(userAccount);
    }

    public ApplicationUser aValidManagerApplicationUser() {
        ApplicationUser applicationUser = new ApplicationUser();
        applicationUser.setFirstName("Fritz");
        applicationUser.setLastName("Leser");
        applicationUser.setEmployeeId("MA-2343");
        applicationUser.setUserAccount(aValidManagerUserAccount());

        return applicationUserRepository.save(applicationUser);
    }

    public UserAccount aValidEmployeeUserAccount() {
        UserAccount userAccount = new UserAccount();
        userAccount.setId(UUID.randomUUID());
        userAccount.setUsername("lenamalz");
        userAccount.setEmail("lenamalz@mail.de");
        userAccount.setPassword(passwordEncoder.encode("12345678"));
        userAccount.setEnabled(true);
        userAccount.setRole(Role.EMPLOYEE);

        return userAccountRepository.save(userAccount);
    }

    public ApplicationUser aValidEmployeeApplicationUser() {
        ApplicationUser applicationUser = new ApplicationUser();
        applicationUser.setFirstName("Lena");
        applicationUser.setLastName("Malz");
        applicationUser.setEmployeeId("MA-2121");
        applicationUser.setUserAccount(aValidEmployeeUserAccount());

        return applicationUserRepository.save(applicationUser);
    }

    public LoginRequest aValidEmployeeloginRequest(){
        return LoginRequest.builder()
                .username("lenamalz")
                .password("12345678")
                .build();
    }

    public ApplicationUserRequest aValidApplicationRequest() {
        return new ApplicationUserRequest("MA-2112", "Test", "Tester");
    }

    public Storage aValidStorage() {
        Storage storage = new Storage();
        storage.setName("Storage-1");
        return storageRepository.save(storage);
    }

    public StorageRequest aValidStorageRequest() {
        return StorageRequest.builder()
                .name("Storage-1")
                .build();
    }

    public Asset aValidAsset() {
        Asset asset = new Asset();
        asset.setName("iPhone 13");
        asset.setCategory("Telefon");
        return assetRepository.save(asset);
    }

    public AssetRequest aValidAssetRequest() {
        String storageId = aValidStorage().getId().toString();
        return new AssetRequest("iPhone 12", "Telefon", storageId);
    }

    public AssetInquiryRequest aValidAssetInquiryRequest() {
        return AssetInquiryRequest.builder()
                .note("notes")
                .price(10.0)
                .link("www.testlink.de")
                .addressRequest(aAddressRequest())
                .assetName("testAsset")
                .assetCategory("testCategory")
                .build();
    }

    public AssetInquiry aValidAssetInquiry() {
        AssetInquiry assetInquiry = new AssetInquiry();
        assetInquiry.setEntryDate("09.07.2022");
        assetInquiry.setNote("aNote");
        assetInquiry.setPrice(10.0);
        assetInquiry.setLink("www.aLink.de");
        assetInquiry.setEnable(false);
        assetInquiry.setStatus(Status.NOT_DONE);
        assetInquiry.setOwner(aValidEmployeeApplicationUser());
        assetInquiry.setAddress(aAddress());
        assetInquiry.setAssetName("iPhone 13");
        assetInquiry.setAssetCategory("Telefon");
        return assetInquiryRepository.save(assetInquiry);
    }

    public RegisterRequest aValidRegisterRequest(){
        return RegisterRequest.builder()
                .username("username")
                .password("password")
                .email("username@mail.de")
                .build();
    }
}
