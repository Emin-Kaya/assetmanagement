package com.bht.assetmanagement.core.assetInquiry;

import com.bht.assetmanagement.core.applicationUser.ApplicationUserService;
import com.bht.assetmanagement.persistence.dto.ApplicationUserRequest;
import com.bht.assetmanagement.persistence.dto.AssetInquiryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/api/v1/assetInquiry")
@RequiredArgsConstructor
public class AssetInquiryController {
    private final AssetInquiryService assetInquiryService;

    @PostMapping
    public ResponseEntity<String> createAssetInquiry(@RequestBody AssetInquiryDto assetInquiryDto) {
        assetInquiryService.createAssetInquiry(assetInquiryDto);
        return status(HttpStatus.CREATED).body("Asset Inquiry saved.");
    }
}
