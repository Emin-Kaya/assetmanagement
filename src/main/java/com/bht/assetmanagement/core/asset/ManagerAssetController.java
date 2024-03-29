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

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteAsset(@PathVariable String id) {
        assetService.delete(id);
    }

    @GetMapping()
    public @ResponseBody
    List<AssetDto> getAllAssets() {
        return assetService.getAll();
    }

    @GetMapping("/{userId}")
    public @ResponseBody
    List<AssetDto> getAllAssetsOfUserByID(@PathVariable String userId) {
        return assetService.getAllAssetsOfUserByID(UUID.fromString(userId));
    }
}
