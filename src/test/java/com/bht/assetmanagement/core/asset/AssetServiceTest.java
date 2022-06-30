package com.bht.assetmanagement.core.asset;

import com.bht.assetmanagement.persistence.dto.AssetDto;
import com.bht.assetmanagement.persistence.dto.AssetRequest;
import com.bht.assetmanagement.persistence.entity.ApplicationUser;
import com.bht.assetmanagement.persistence.entity.Asset;
import com.bht.assetmanagement.persistence.entity.Role;
import com.bht.assetmanagement.persistence.entity.UserAccount;
import com.bht.assetmanagement.persistence.repository.ApplicationUserRepository;
import com.bht.assetmanagement.persistence.repository.AssetRepository;
import com.bht.assetmanagement.persistence.repository.UserAccountRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(properties = {"spring.liquibase.enabled=false"})
public class AssetServiceTest {

    @Autowired
    private AssetService assetService;

    @Autowired
    private AssetRepository assetRepository;

    @Autowired
    private ApplicationUserRepository applicationUserRepository;

    @Autowired
    private UserAccountRepository userAccountRepository;

    private UserAccount userAccount;

    @BeforeEach
    void deleteAll() {
        applicationUserRepository.deleteAll();
        userAccountRepository.deleteAll();
        assetRepository.deleteAll();
        userAccount = aValidUserAccount();
    }

    @Test
    void createAssetTest() {
        Asset asset = assetService.createAsset(aAssetRequest());
        assertEquals(assetRepository.findAll().get(0).getId(), asset.getId());
    }

    @Test
    void getAssetTest() {
        Asset asset = assetService.getAsset(aAssetRequest());
        assertEquals(assetRepository.findAll().get(0).getId(), asset.getId());
    }

    @Test
    void saveAssetToApplicationUserTest() {
        ApplicationUser applicationUser = aApplicationUser();
        Asset expected = aAsset();

        applicationUser.getAssets().add(expected);

        List<Asset> actual = applicationUser.getAssets();
        assertEquals(expected.getId(), actual.get(0).getId());
    }

    @Test
    @WithMockUser(username = "testtester", password = "12345678")
    void getAllAssetsOfUserTest() {
        List<AssetDto> assetsOfUser = assetService.getAllAssetsOfUser();

        System.out.println(assetsOfUser);
    }

    @Test
    void removeAssetFromUserTest() {
        ApplicationUser applicationUser = aApplicationUser();
        Asset asset = aAsset();

        applicationUser.setAssets(List.of(asset));

        assetService.removeAssetFromUser(asset.getId().toString());

        assertEquals(applicationUser.getAssets().size(), 0);

    }


    @Test
    void getAllAssetsTest() {
        aAsset();
        List<AssetDto> actual = assetService.getAllAssets();

        List<AssetDto> expected = new ArrayList<>();
        assetRepository.findAll().forEach(it -> expected.add(AssetMapper.INSTANCE.mapEntityToAssetDto(it)));

        Assertions.assertEquals(actual, expected);
    }


    private AssetRequest aAssetRequest() {
        return new AssetRequest("testAsset", "testCategory");
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
}
