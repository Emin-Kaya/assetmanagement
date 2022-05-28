package com.bht.assetmanagement.core.assetInquiry;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/api/v1/manager/assetInquiry")
@RequiredArgsConstructor
public class ManagerAssetInquiryController {
    private final AssetInquiryService assetInquiryService;

    @PutMapping("/{assetInquiryId}")
    public ResponseEntity<String> handleAssetInquiry(@PathVariable String assetInquiryId, @RequestParam Boolean isEnabel) {
        assetInquiryService.editAssetInquiry(assetInquiryId, isEnabel);
        return status(HttpStatus.CREATED).body("Asset Inquiry edited.");
    }
}
