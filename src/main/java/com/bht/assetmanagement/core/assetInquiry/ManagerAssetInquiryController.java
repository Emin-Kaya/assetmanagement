package com.bht.assetmanagement.core.assetInquiry;

import com.bht.assetmanagement.persistence.dto.AssetInquiryDto;
import com.bht.assetmanagement.persistence.dto.AssetInquiryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/api/v1/manager/assetInquiry")
@RequiredArgsConstructor
public class ManagerAssetInquiryController {
    private final AssetInquiryService assetInquiryService;

    @PutMapping("/{assetInquiryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<String> cancelAssetInquiry(@PathVariable String assetInquiryId) {
        assetInquiryService.cancel(assetInquiryId);
        return status(HttpStatus.ACCEPTED).body("Asset inquiry was rejected.");
    }

    @PutMapping("/confirm/{assetInquiryId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public AssetInquiryResponse confirmAssetInquiry(@PathVariable String assetInquiryId) {
        return assetInquiryService.confirm(assetInquiryId);
    }

    @PutMapping("/handle/{assetInquiryId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void handleAssetInquriyInStorage(@PathVariable String assetInquiryId, @RequestParam String storageId) {
        assetInquiryService.handleInStorage(storageId, assetInquiryId);
    }

    @GetMapping()
    public @ResponseBody
    List<AssetInquiryDto> getAllAssetInquiry() {
        return assetInquiryService.getAll();
    }
}
