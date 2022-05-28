package com.bht.assetmanagement.core.assetInquiry;

import com.bht.assetmanagement.core.address.AddressService;
import com.bht.assetmanagement.core.applicationUser.ApplicationUserService;
import com.bht.assetmanagement.core.asset.AssetService;
import com.bht.assetmanagement.core.email.EmailService;
import com.bht.assetmanagement.persistence.dto.AssetInquiryDto;
import com.bht.assetmanagement.persistence.entity.*;
import com.bht.assetmanagement.persistence.repository.AssetInquiryRepository;
import com.bht.assetmanagement.shared.date.DateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AssetInquiryService {
    private final AddressService addressService;
    private final AssetService assetService;
    private final ApplicationUserService applicationUserService;
    private final AssetInquiryRepository assetInquiryRepository;
    private final EmailService emailService;
    private final DateUtils dateUtils;

    public void createAssetInquiry(AssetInquiryDto assetInquiryDto) {
        AssetInquiry assetInquiry = AssetInquiryMapper.INSTANCE.mapDtoToAssetInquiry(assetInquiryDto);
        ApplicationUser applicationUser = applicationUserService.getCurrentApplicationUser();
        Address address = addressService.getAddress(assetInquiryDto.getAddressDto());
        Asset asset = assetService.getAsset(assetInquiryDto.getAssetDto());

        assetInquiry.setEntryDate(dateUtils.createLocalDate());
        assetInquiry.setStatus(Status.NOT_DONE);
        assetInquiry.setOwner(applicationUser);
        assetInquiry.setAddress(address);
        assetInquiry.setAsset(asset);

        assetInquiryRepository.save(assetInquiry);
    }

    public void editAssetInquiry(String assetInquiryId, Boolean isEnabled) {
        AssetInquiry assetInquiry = assetInquiryRepository.findByAssetInquiryId(UUID.fromString(assetInquiryId));
        assetInquiry.setEnable(isEnabled);
        assetInquiry.setStatus(Status.DONE);
        if (!assetInquiry.isEnable()) {
            assetInquiryRepository.delete(assetInquiry);
        } else {
            assetService.saveAssetToApplicationUser(assetInquiry.getAsset(), assetInquiry.getOwner());
        }
        emailService.sendAssetStatusMail(assetInquiry.getOwner().getUserAccount().getEmail(), isEnabled);
    }
}
