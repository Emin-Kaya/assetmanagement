package com.bht.assetmanagement.core.assetInquiry;

import com.bht.assetmanagement.core.address.AddressMapper;
import com.bht.assetmanagement.core.address.AddressService;
import com.bht.assetmanagement.core.applicationUser.ApplicationUserMapper;
import com.bht.assetmanagement.core.applicationUser.ApplicationUserService;
import com.bht.assetmanagement.core.asset.AssetMapper;
import com.bht.assetmanagement.core.asset.AssetService;
import com.bht.assetmanagement.core.email.EmailService;
import com.bht.assetmanagement.core.userAccount.UserAccountService;
import com.bht.assetmanagement.persistence.dto.*;
import com.bht.assetmanagement.persistence.entity.*;
import com.bht.assetmanagement.persistence.repository.AssetInquiryRepository;
import com.bht.assetmanagement.shared.date.DateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
        assetInquiry.setStatus(Status.NOT_DONE);
        assetInquiry.setOwner(applicationUser);
        assetInquiry.setAddress(address);
        assetInquiry.setAsset(asset);

        assetInquiryRepository.save(assetInquiry);
        List<UserAccount> listOfAssetManagers = userAccountService.getAllUsersByRole(Role.MANAGER);
        listOfAssetManagers.forEach(it->emailService.sendNewAssetInquiryMail(applicationUser.getUserAccount().getEmail(), it.getEmail()));
    }

    public void editAssetInquiry(String assetInquiryId, Boolean isEnabled) {
        ApplicationUser assetManager = applicationUserService.getCurrentApplicationUser();
        AssetInquiry assetInquiry = assetInquiryRepository.findByAssetInquiryId(UUID.fromString(assetInquiryId));
        assetInquiry.setEnable(isEnabled);
        assetInquiry.setStatus(Status.DONE);
        if (!assetInquiry.isEnable()) {
            assetInquiryRepository.delete(assetInquiry);
        } else {
            assetService.saveAssetToApplicationUser(assetInquiry.getAsset(), assetInquiry.getOwner());
        }
        emailService.sendAssetStatusMail(assetManager.getUserAccount().getEmail(), assetInquiry.getOwner().getUserAccount().getEmail(), isEnabled);
    }

    public List<AssetInquiryResponse> getAllAssetInquiry() {
        List<AssetInquiry> assetInquiryList = assetInquiryRepository.findAll();
        AssetInquiryResponse assetInquiryResponse;

        List<AssetInquiryResponse> assetInquiryResponseList = new ArrayList<>();

        for (AssetInquiry assetInquiry : assetInquiryList) {
            assetInquiryResponse = AssetInquiryMapper.INSTANCE.mapEntityToAssetInquiryResponse(assetInquiry);

            assetInquiryResponse.setAssetResponse(AssetMapper.INSTANCE.mapEntityToAssetResponse(assetInquiry.getAsset()));
            assetInquiryResponse.setAddressResponse(AddressMapper.INSTANCE.mapEntityToAddressResponse(assetInquiry.getAddress()));
            assetInquiryResponse.setOwner(ApplicationUserMapper.INSTANCE.mapEntityToApplicationUserResponse(assetInquiry.getOwner()));

            assetInquiryResponseList.add(assetInquiryResponse);
        }

        return assetInquiryResponseList;
    }
}
