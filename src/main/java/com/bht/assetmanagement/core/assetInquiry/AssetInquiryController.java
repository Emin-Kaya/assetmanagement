package com.bht.assetmanagement.core.assetInquiry;

import com.bht.assetmanagement.persistence.dto.AssetInquiryDto;
import com.bht.assetmanagement.persistence.dto.AssetInquiryRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/assetInquiry")
@RequiredArgsConstructor
public class AssetInquiryController {
    private final AssetInquiryService assetInquiryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AssetInquiryDto createAssetInquiry(@RequestBody AssetInquiryRequest assetInquiryRequest) {
        return assetInquiryService.create(assetInquiryRequest);
    }
}
