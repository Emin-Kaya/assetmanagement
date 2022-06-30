package com.bht.assetmanagement.core.assetInquiry;

import com.bht.assetmanagement.core.address.AddressMapper;
import com.bht.assetmanagement.core.address.AddressService;
import com.bht.assetmanagement.core.applicationUser.ApplicationUserMapper;
import com.bht.assetmanagement.core.applicationUser.ApplicationUserService;
import com.bht.assetmanagement.core.asset.AssetMapper;
import com.bht.assetmanagement.core.asset.AssetService;
import com.bht.assetmanagement.core.email.EmailService;
import com.bht.assetmanagement.core.userAccount.UserAccountService;
import com.bht.assetmanagement.persistence.dto.AssetInquiryDto;
import com.bht.assetmanagement.persistence.dto.AssetInquiryRequest;
import com.bht.assetmanagement.persistence.entity.*;
import com.bht.assetmanagement.persistence.repository.AssetInquiryRepository;
import com.bht.assetmanagement.shared.date.DateUtils;
import com.bht.assetmanagement.shared.exception.EntryNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
    private final DateUtils dateUtils;

    public void createAssetInquiry(AssetInquiryRequest assetInquiryRequest) {
        AssetInquiry assetInquiry = AssetInquiryMapper.INSTANCE.mapRequestToAssetInquiry(assetInquiryRequest);
        ApplicationUser applicationUser = applicationUserService.getCurrentApplicationUser();
        Address address = addressService.getAddress(assetInquiryRequest.getAddressRequest());
        Asset asset = assetService.getAsset(assetInquiryRequest.getAssetRequest());

        assetInquiry.setEntryDate(dateUtils.createLocalDate());
        assetInquiry.setStatus(NOT_DONE);
        assetInquiry.setOwner(applicationUser);
        assetInquiry.setAddress(address);
        assetInquiry.setAsset(asset);

        assetInquiryRepository.save(assetInquiry);
        List<UserAccount> listOfAssetManagers = userAccountService.getAllUsersByRole(Role.MANAGER);
        listOfAssetManagers.forEach(it -> emailService.sendNewAssetInquiryMail(applicationUser.getUserAccount().getEmail(), it.getEmail()));
    }

    public void editAssetInquiry(String id, Boolean isEnabled) {
        ApplicationUser assetManager = applicationUserService.getCurrentApplicationUser();
        AssetInquiry assetInquiry = assetInquiryRepository.findById(UUID.fromString(id)).orElseThrow(() -> new EntryNotFoundException("AssetInquiry with id " + id + "does not exist."));
        assetInquiry.setEnable(isEnabled);
        assetInquiry.setStatus(Status.DONE);
        if (!assetInquiry.isEnable()) {
            assetInquiryRepository.delete(assetInquiry);
        } else {
            //todo check if asset in storage exist if exists -> return
            //Todo else create Asset
            assetService.saveAssetToApplicationUser(assetInquiry.getAsset(), assetInquiry.getOwner());
        }
        emailService.sendAssetStatusMail(assetManager.getUserAccount().getEmail(), assetInquiry.getOwner().getUserAccount().getEmail(), isEnabled);
    }

    public List<AssetInquiryDto> getAllAssetInquiry() {
        List<AssetInquiry> assetInquiryList = assetInquiryRepository.findAll();
        AssetInquiryDto assetInquiryDto;

        List<AssetInquiryDto> assetInquiryDtoList = new ArrayList<>();

        for (AssetInquiry assetInquiry : assetInquiryList) {
            if (assetInquiry.getStatus() == NOT_DONE) {
                assetInquiryDto = AssetInquiryMapper.INSTANCE.mapEntityToAssetInquiryResponse(assetInquiry);
                assetInquiryDto.setAssetDto(AssetMapper.INSTANCE.mapEntityToAssetDto(assetInquiry.getAsset()));
                assetInquiryDto.setAddressDTO(AddressMapper.INSTANCE.mapEntityToAddressResponse(assetInquiry.getAddress()));
                assetInquiryDto.setOwner(ApplicationUserMapper.INSTANCE.mapEntityToApplicationUserResponse(
                        assetInquiry.getOwner(),
                        assetInquiry.getOwner().getUserAccount().getUsername(),
                        assetInquiry.getOwner().getUserAccount().getEmail()));

                assetInquiryDtoList.add(assetInquiryDto);
            }
        }

        return assetInquiryDtoList;
    }
}
