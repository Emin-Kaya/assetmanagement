package com.bht.assetmanagement.core.assetInquiry;

import com.bht.assetmanagement.persistence.dto.AssetInquiryDto;
import com.bht.assetmanagement.persistence.dto.AssetInquiryResponse;
import com.bht.assetmanagement.persistence.dto.AssetRequest;
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

    @PutMapping("/confirm/order/{assetInquiryId}") //TODO URL Ändern zuerst id dann funktion
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void orderAssetForInquiry(@PathVariable String id, @RequestBody AssetRequest assetRequest) {
        assetInquiryService.orderAssetForInquiry(id, assetRequest);
    }

    @GetMapping()
    public @ResponseBody
    List<AssetInquiryDto> getAllAssetInquiry() {
        return assetInquiryService.getAll();
    }
}
