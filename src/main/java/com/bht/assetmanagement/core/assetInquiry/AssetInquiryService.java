package com.bht.assetmanagement.core.assetInquiry;

import com.bht.assetmanagement.core.address.AddressService;
import com.bht.assetmanagement.core.applicationUser.ApplicationUserMapper;
import com.bht.assetmanagement.core.applicationUser.ApplicationUserService;
import com.bht.assetmanagement.core.asset.AssetService;
import com.bht.assetmanagement.core.email.EmailService;
import com.bht.assetmanagement.core.storage.StorageMapper;
import com.bht.assetmanagement.core.storage.StorageService;
import com.bht.assetmanagement.core.userAccount.UserAccountService;
import com.bht.assetmanagement.persistence.dto.ApplicationUserDto;
import com.bht.assetmanagement.persistence.dto.AssetInquiryDto;
import com.bht.assetmanagement.persistence.dto.AssetInquiryRequest;
import com.bht.assetmanagement.persistence.dto.AssetInquiryResponse;
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

import static com.bht.assetmanagement.persistence.entity.Status.DONE;
import static com.bht.assetmanagement.persistence.entity.Status.NOT_DONE;

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
        ApplicationUser applicationUser = applicationUserService.getCurrentUser();
        Address address = addressService.getOrCreateAddress(assetInquiryRequest.getAddressRequest());

        assetInquiry.setEntryDate(dateUtils.createLocalDate());
        assetInquiry.setStatus(NOT_DONE);
        assetInquiry.setAddress(address);

        assetInquiryRepository.save(assetInquiry);
        List<UserAccount> listOfAssetManagers = userAccountService.getAllUsersByRole(Role.MANAGER);

        String subject = "Neue AssetAnfrage";
        String body = "Sie haben eine neue Asset Anfrage erhalten.";

        listOfAssetManagers.forEach(it -> emailService.sendMessage(applicationUser.getUserAccount().getEmail(), it.getEmail(), subject, body));

        ApplicationUserDto applicationUserDto = ApplicationUserMapper.INSTANCE.mapEntityToApplicationUserResponse(applicationUser, applicationUser.getUserAccount().getUsername(), applicationUser.getUserAccount().getEmail());
        AssetInquiryDto assetInquiryDto = AssetInquiryMapper.INSTANCE.mapEntityToAssetInquiryDto(assetInquiry);
        assetInquiryDto.setOwner(applicationUserDto);
        return assetInquiryDto;
    }

    public void cancel(String assetInquiryId) {
        AssetInquiry assetInquiry = find(assetInquiryId);
        assetInquiry.setEnable(false);

        ApplicationUser assetManager = applicationUserService.getCurrentUser();
        String assetManagerMail = assetManager.getUserAccount().getEmail();

        emailService.sendMessage(assetManagerMail, assetInquiry.getOwner().getUserAccount().getEmail(), emailUtils.getSubjectIsEnabledText(), emailUtils.getBodyDisabledText());
        assetInquiry.setStatus(DONE);
        save(assetInquiry);
    }

    public AssetInquiryResponse confirm(String id) {
        AssetInquiry assetInquiry = find(id);
        AssetInquiryDto assetInquiryDto = new AssetInquiryDto();
        ApplicationUser assetManager = applicationUserService.getCurrentUser();
        String assetManagerMail = assetManager.getUserAccount().getEmail();

        ApplicationUserDto applicationUserDto = ApplicationUserMapper.INSTANCE.mapEntityToApplicationUserResponse(assetInquiry.getOwner(),
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
            assetInquiryDto.setOwner(applicationUserDto);

            return AssetInquiryResponse.builder()
                    .assetInquiryDto(assetInquiryDto)
                    .build();
        }
        assetInquiryDto = AssetInquiryMapper.INSTANCE.mapEntityToAssetInquiryDto(assetInquiry);
        assetInquiryDto.setOwner(applicationUserDto);
        return AssetInquiryResponse.builder()
                .assetInquiryDto(assetInquiryDto)
                .storageDtoList(storageList.stream().map(StorageMapper.INSTANCE::mapEntityToStorageDto).collect(Collectors.toList()))
                .build();
    }

    public void handleInStorage(String storageId, String assetInquiryId) {
        ApplicationUser assetManager = applicationUserService.getCurrentUser();
        Storage storage = storageService.findStorage(storageId);
        AssetInquiry assetInquiry = find(assetInquiryId);
        Asset asset = assetService.getAssetFromAssetInquiry(assetInquiry);

        assetService.removeAssetFromStorage(asset.getId().toString(), storage.getId().toString());
        assetService.saveAssetToApplicationUser(asset, assetInquiry.getOwner());

        assetInquiry.setStatus(DONE);
        save(assetInquiry);

        emailService.sendMessage(assetManager.getUserAccount().getEmail(), emailUtils.getSubjectOrderNotificationText(), emailUtils.getBodyOrderAndStorageRemoveNotificationText(assetInquiry, storage));
    }


    public List<AssetInquiryDto> getAll() {
        List<AssetInquiry> assetInquiryList = assetInquiryRepository.findAll();
        AssetInquiryDto assetInquiryDto;

        List<AssetInquiryDto> assetInquiryDtoList = new ArrayList<>();

        for (AssetInquiry assetInquiry : assetInquiryList) {
            if (assetInquiry.getStatus() == NOT_DONE) {
                ApplicationUserDto applicationUserDto = ApplicationUserMapper.INSTANCE.mapEntityToApplicationUserResponse(assetInquiry.getOwner(),
                        assetInquiry.getOwner().getUserAccount().getUsername(),
                        assetInquiry.getOwner().getUserAccount().getEmail());
                assetInquiryDto = AssetInquiryMapper.INSTANCE.mapEntityToAssetInquiryDto(assetInquiry);
                assetInquiryDto.setOwner(applicationUserDto);
                assetInquiryDto.setAssetName(assetInquiry.getAssetName());
                assetInquiryDto.setAssetCategory(assetInquiry.getAssetCategory());
                assetInquiryDtoList.add(assetInquiryDto);
            }
        }

        return assetInquiryDtoList;
    }
}
