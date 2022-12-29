package com.bht.assetmanagement.core.assetInquiry;

import com.bht.assetmanagement.persistence.dto.AssetInquiryDto;
import com.bht.assetmanagement.persistence.dto.AssetInquiryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/manager/assetInquiry")
@RequiredArgsConstructor
public class ManagerAssetInquiryController {
    private final AssetInquiryService assetInquiryService;

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelAssetInquiry(@PathVariable String id) {
        assetInquiryService.cancel(id);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public AssetInquiryResponse getAvailableAssetsForAssetInquiry(@PathVariable String id) {
        return assetInquiryService.availableAssetsForInquiry(id);
    }

    @PutMapping("/confirm/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void confirmAssetInquriy(@PathVariable String id, @RequestParam String assetId) {
        assetInquiryService.confirmAssetInquriy(id, assetId);
    }



    @PutMapping("/confirm/handle/{assetInquiryId}") //TODO URL Ändern zuerst id dann funktion
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void handleAssetInquriyInStorage(@PathVariable String assetInquiryId, @RequestParam String storageId) {
        //assetInquiryService.handleInStorage(storageId, assetInquiryId);
    }

    @GetMapping()
    public @ResponseBody
    List<AssetInquiryDto> getAllAssetInquiry() {
        return assetInquiryService.getAll();
    }
}
