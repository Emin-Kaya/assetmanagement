package com.bht.assetmanagement.core.assetInquiry;

import com.bht.assetmanagement.core.address.AddressService;
import com.bht.assetmanagement.core.applicationUser.ApplicationUserMapper;
import com.bht.assetmanagement.core.applicationUser.ApplicationUserService;
import com.bht.assetmanagement.core.asset.AssetService;
import com.bht.assetmanagement.core.email.EmailService;
import com.bht.assetmanagement.core.storage.StorageService;
import com.bht.assetmanagement.core.userAccount.UserAccountService;
import com.bht.assetmanagement.persistence.dto.*;
import com.bht.assetmanagement.persistence.entity.*;
import com.bht.assetmanagement.persistence.repository.AssetInquiryRepository;
import com.bht.assetmanagement.shared.date.DateUtils;
import com.bht.assetmanagement.shared.email.EmailUtils;
import com.bht.assetmanagement.shared.exception.EntryNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.bht.assetmanagement.persistence.entity.Status.*;

@Service
@RequiredArgsConstructor
public class AssetInquiryService {
    private final AddressService addressService;
    private final AssetService assetService;

    private final ApplicationUserService applicationUserService;
    private final UserAccountService userAccountService;
    private final AssetInquiryRepository assetInquiryRepository;
    private final EmailService emailService;
    private final StorageService storageService;
    private final DateUtils dateUtils;
    private final EmailUtils emailUtils;

    public AssetInquiry save(AssetInquiry assetInquiry) {
        return assetInquiryRepository.save(assetInquiry);
    }
    public AssetInquiry find(String id) {
        return assetInquiryRepository.findById(UUID.fromString(id)).orElseThrow(() -> new EntryNotFoundException("AssetInquiry with id: " + id + "does not exist."));
    }

    public AssetInquiryDto create(AssetInquiryRequest assetInquiryRequest) {
        AssetInquiry assetInquiry = AssetInquiryMapper.INSTANCE.mapRequestToAssetInquiry(assetInquiryRequest);
        UserAccount userAccount = userAccountService.getCurrenUser();
        ApplicationUser applicationUser = userAccount.getApplicationUser();
        Address address = addressService.getOrCreateAddress(assetInquiryRequest.getAddressRequest());

        assetInquiry.setEntryDate(dateUtils.createLocalDate());
        assetInquiry.setStatus(NOT_DONE);
        assetInquiry.setOwner(applicationUser);
        assetInquiry.setAddress(address);

        assetInquiryRepository.save(assetInquiry);
        List<UserAccount> listOfAssetManagers = userAccountService.getAllUsersByRole(Role.MANAGER);


        listOfAssetManagers.forEach(it -> emailService.sendMessage(
                applicationUser.getUserAccount().getEmail(),
                it.getEmail(),
                emailUtils.getSubjectNewAssetInquiryText(),
                emailUtils.getBodyNewAssetInquiryText()));

        ApplicationUserDto applicationUserDto = ApplicationUserMapper.INSTANCE.mapEntityToApplicationUserResponse(applicationUser, applicationUser.getUserAccount().getUsername(), applicationUser.getUserAccount().getEmail());
        AssetInquiryDto assetInquiryDto = AssetInquiryMapper.INSTANCE.mapEntityToAssetInquiryDto(assetInquiry);
        assetInquiryDto.setOwner(applicationUserDto);
        return assetInquiryDto;
    }

    public void cancel(String assetInquiryId) {
        AssetInquiry assetInquiry = find(assetInquiryId);
        assetInquiry.setEnable(false);

        UserAccount userAccount = userAccountService.getCurrenUser();
        String assetManagerMail = userAccount.getEmail();

        emailService.sendMessage(assetManagerMail, assetInquiry.getOwner().getUserAccount().getEmail(), emailUtils.getSubjectIsEnabledText(), emailUtils.getBodyDisabledText());
        assetInquiry.setStatus(DONE);
        save(assetInquiry);
    }

    public AssetInquiryResponse availableAssetsForInquiry(String id) {
        AssetInquiry assetInquiry = find(id);
        ApplicationUserDto employeeDto = ApplicationUserMapper.INSTANCE.mapEntityToApplicationUserResponse(assetInquiry.getOwner(),
                assetInquiry.getOwner().getUserAccount().getUsername(),
                assetInquiry.getOwner().getUserAccount().getEmail());

        List<StorageDto> storageDtoList  = storageService.getAllStoragesContainsAsset(assetInquiry.getAssetName(), assetInquiry.getAssetCategory());

        List<AssetDto> assetDtoList = assetService.getFreeAssetsByAttributes(assetInquiry.getAssetName(), assetInquiry.getAssetCategory());


        assetInquiry.setStatus(OPEN);
        save(assetInquiry);


        return AssetInquiryResponse.builder()
                .id(assetInquiry.getId().toString())
                .assetDtos(assetDtoList)
                .storageDto(storageDtoList)
                .build();
    }



/*    public AssetInquiryResponse confirm(String id) {
        AssetInquiry assetInquiry = find(id);
        AssetInquiryDto assetInquiryDto;
        UserAccount userAccount = userAccountService.getCurrenUser();

        String assetManagerMail = userAccount.getEmail();

        ApplicationUserDto employeeDto = ApplicationUserMapper.INSTANCE.mapEntityToApplicationUserResponse(assetInquiry.getOwner(),
                assetInquiry.getOwner().getUserAccount().getUsername(),
                assetInquiry.getOwner().getUserAccount().getEmail());

        Asset asset = assetService.getAssetFromAssetInquiry(assetInquiry);
        List<Storage> storageList = assetService.getAllStoragesContainsAsset(asset.getId().toString());

        if (storageList.isEmpty()) {

            emailService.sendMessage(assetManagerMail, emailUtils.getSubjectOrderNotificationText(), emailUtils.getBodyOrderNotificationText(assetInquiry));

            emailService.sendMessage(assetManagerMail, assetInquiry.getOwner().getUserAccount().getEmail(), emailUtils.getSubjectIsEnabledText(), emailUtils.getBodyEnabledText());

            assetService.saveAssetToApplicationUser(asset, assetInquiry.getOwner());
            assetInquiry.setStatus(DONE);
            save(assetInquiry);
            assetInquiryDto = AssetInquiryMapper.INSTANCE.mapEntityToAssetInquiryDto(assetInquiry);
            assetInquiryDto.setOwner(employeeDto);

            return AssetInquiryResponse.builder()
                    .assetInquiryDto(assetInquiryDto)
                    .build();
        }
        assetInquiryDto = AssetInquiryMapper.INSTANCE.mapEntityToAssetInquiryDto(assetInquiry);
        assetInquiryDto.setOwner(employeeDto);

        return AssetInquiryResponse.builder()
                .assetInquiryDto(assetInquiryDto)
                .storageDtoList(storageList.stream().map(StorageMapper.INSTANCE::mapEntityToStorageDto).collect(Collectors.toList()))
                .build();
    }

    public void handleInStorage(String storageId, String assetInquiryId) {
        UserAccount userAccount = userAccountService.getCurrenUser();

        ApplicationUser assetManager = userAccount.getApplicationUser();
        Storage storage = storageService.findStorage(storageId);
        AssetInquiry assetInquiry = find(assetInquiryId);
        Asset asset = assetService.getAssetFromAssetInquiry(assetInquiry);

        assetService.removeAssetFromStorage(asset.getId().toString(), storage.getId().toString());
        assetService.saveAssetToApplicationUser(asset, assetInquiry.getOwner());

        assetInquiry.setStatus(DONE);
        save(assetInquiry);

        emailService.sendMessage(assetManager.getUserAccount().getEmail(), emailUtils.getSubjectOrderNotificationText(), emailUtils.getBodyOrderAndStorageRemoveNotificationText(assetInquiry, storage));
    }*/


    public List<AssetInquiryDto> getAll() {
        List<AssetInquiry> assetInquiryList = assetInquiryRepository.findAll();
        AssetInquiryDto assetInquiryDto;

        List<AssetInquiryDto> assetInquiryDtoList = new ArrayList<>();

        for (AssetInquiry assetInquiry : assetInquiryList) {
            if (assetInquiry.getStatus() != DONE) {
                ApplicationUserDto applicationUserDto = ApplicationUserMapper.INSTANCE.mapEntityToApplicationUserResponse(assetInquiry.getOwner(),
                        assetInquiry.getOwner().getUserAccount().getUsername(),
                        assetInquiry.getOwner().getUserAccount().getEmail());

                assetInquiryDto = AssetInquiryMapper.INSTANCE.mapEntityToAssetInquiryDto(assetInquiry);
                assetInquiryDto.setOwner(applicationUserDto);

                assetInquiryDtoList.add(assetInquiryDto);
            }
        }

        return assetInquiryDtoList;
    }

    public void confirmAssetInquriy(String inquiryId, String assetId) {
        AssetInquiry assetInquiry = find(inquiryId);
        assetService.saveAssetToApplicationUser(assetId, assetInquiry.getOwner().getId().toString());

        assetInquiry.setEnable(true);
        assetInquiry.setStatus(DONE);
        save(assetInquiry);
    }
}
