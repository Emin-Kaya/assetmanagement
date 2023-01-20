package com.bht.assetmanagement.core.assetInquiry;

import com.bht.assetmanagement.core.address.AddressService;
import com.bht.assetmanagement.core.applicationUser.ApplicationUserMapper;
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
import java.util.stream.Collectors;

import static com.bht.assetmanagement.persistence.entity.Status.*;

@Service
@RequiredArgsConstructor
public class AssetInquiryService {
    private final AddressService addressService;
    private final AssetService assetService;
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
        return assetInquiryRepository.findById(UUID.fromString(id)).filter(it->!it.isArchived()).orElseThrow(() -> new EntryNotFoundException("AssetInquiry with id: " + id + "does not exist."));
    }
    public List<AssetInquiryDto> getAll() {
        List<AssetInquiry> assetInquiryList = assetInquiryRepository.findAll().stream().filter(it->!it.isArchived()).collect(Collectors.toList());
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

        List<StorageResponse> storageDtoList  = storageService.getAllStoragesContainsAsset(assetInquiry.getAssetName(), assetInquiry.getAssetCategory());

        assetInquiry.setStatus(OPEN);
        save(assetInquiry);


        return AssetInquiryResponse.builder()
                .id(assetInquiry.getId().toString())
                .storageDto(storageDtoList)
                .build();
    }
    public void confirmAssetInquriy(String inquiryId, String assetId) {
        AssetInquiry assetInquiry = find(inquiryId);
        assetService.saveAssetToApplicationUser(assetId, assetInquiry.getOwner().getId().toString());
        Storage storage = storageService.getStorageIdByAsset(assetId);
        assetService.removeAssetFromStorage(assetId, storage.getId().toString());
        assetInquiry.setEnable(true);
        assetInquiry.setStatus(DONE);
        save(assetInquiry);

        UserAccount userAccount = userAccountService.getCurrenUser();
        String assetManagerMail = userAccount.getEmail();
        String employeeMail = assetInquiry.getOwner().getUserAccount().getEmail();


        emailService.sendMessage(assetManagerMail, emailUtils.getSubjectOrderNotificationText(), emailUtils.getBodyOrderAndStorageRemoveNotificationText(assetInquiry, storage));
        emailService.sendMessage(assetManagerMail, assetInquiry.getOwner().getUserAccount().getEmail(),  emailUtils.getSubjectIsEnabledText(), emailUtils.getBodyEnabledText());
    }

    public void orderAssetForInquiry(String inquiryId, AssetRequest assetRequest){
        AssetInquiry assetInquiry = find(inquiryId);
        assetService.create(assetRequest, false);

        assetInquiry.setEnable(false);
        assetInquiry.setStatus(DONE);
        save(assetInquiry);

        UserAccount userAccount = userAccountService.getCurrenUser();
        String assetManagerMail = userAccount.getEmail();
        String employeeMail = assetInquiry.getOwner().getUserAccount().getEmail();

        emailService.sendMessage(assetManagerMail, emailUtils.getSubjectOrderNotificationText(), emailUtils.getBodyOrderNotificationText(assetInquiry));
        emailService.sendMessage(assetManagerMail, employeeMail,  emailUtils.getSubjectIsEnabledText(), emailUtils.getBodyEnabledText());
    }

    public void delete(AssetInquiry assetInquiry) {
        assetInquiryRepository.delete(assetInquiry);
    }

    public AssetInquiryDto getById(String id) {
        return AssetInquiryMapper.INSTANCE.mapEntityToAssetInquiryDto(find(id));
    }
}
