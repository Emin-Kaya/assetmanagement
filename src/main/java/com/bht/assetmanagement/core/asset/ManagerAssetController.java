package com.bht.assetmanagement.core.asset;

import com.bht.assetmanagement.persistence.dto.AssetDto;
import com.bht.assetmanagement.persistence.dto.AssetRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/manager/asset")
@RequiredArgsConstructor
public class ManagerAssetController {
    public final AssetService assetService;

    @PostMapping()
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void saveAssetToStorage(@RequestBody AssetRequest assetRequest) {
        assetService.saveRequestToStorage(assetRequest);
    }


    @PutMapping()
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void saveAssetToUser(@RequestParam String assetId, @RequestParam String auId) {
        assetService.saveAssetToApplicationUser(assetId, auId);
    }

    @GetMapping()
    public @ResponseBody
    List<AssetDto> getAllAssets() {
        return assetService.getAll();
    } //TODO which not rented

    @GetMapping("/{userId}")
    public @ResponseBody
    List<AssetDto> getAllAssetsOfUserByID(@PathVariable String userId) {
        return assetService.getAllAssetsOfUserByID(UUID.fromString(userId));
    }


    @PutMapping("/{assetId}")
    public void removeAssetFromStorage(@PathVariable String assetId, @RequestParam String storageId) {
        assetService.removeAssetFromStorage(assetId, storageId);
    }
}
