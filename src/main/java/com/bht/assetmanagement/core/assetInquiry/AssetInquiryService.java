package com.bht.assetmanagement.core.assetInquiry;

import com.bht.assetmanagement.core.address.AddressService;
import com.bht.assetmanagement.core.applicationUser.ApplicationUserService;
import com.bht.assetmanagement.core.asset.AssetService;
import com.bht.assetmanagement.persistence.dto.AssetInquiryDto;
import com.bht.assetmanagement.persistence.entity.Address;
import com.bht.assetmanagement.persistence.entity.ApplicationUser;
import com.bht.assetmanagement.persistence.entity.Asset;
import com.bht.assetmanagement.persistence.entity.AssetInquiry;
import com.bht.assetmanagement.persistence.repository.AssetInquiryRepository;
import com.bht.assetmanagement.shared.date.DateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class AssetInquiryService {
    private final AddressService addressService;
    private final AssetService assetService;
    private final ApplicationUserService applicationUserService;
    private final AssetInquiryRepository assetInquiryRepository;
    private final DateUtils dateUtils;

    public void createAssetInquiry(AssetInquiryDto assetInquiryDto) {
        AssetInquiry assetInquiry = AssetInquiryMapper.INSTANCE.mapDtoToAssetInquiry(assetInquiryDto);
        ApplicationUser applicationUser = applicationUserService.getCurrentApplicationUser();
        Address address = addressService.createAddress(assetInquiryDto.getAddressDto());
        Asset asset = assetService.createAsset(assetInquiryDto.getAssetDto());

        assetInquiry.setOwner(applicationUser);
        assetInquiry.setAsset(asset);
        assetInquiry.setAddress(address);
        assetInquiry.setEntryDate(dateUtils.createLocalDate());

        assetInquiryRepository.save(assetInquiry);

    }
}
