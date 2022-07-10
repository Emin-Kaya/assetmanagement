package com.bht.assetmanagement.core.asset;

import com.bht.assetmanagement.persistence.dto.AssetDto;
import com.bht.assetmanagement.persistence.dto.AssetRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/asset")
@RequiredArgsConstructor
public class AssetController {
    public final AssetService assetService;

    @PostMapping()
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void saveAssetToStorage(@RequestBody AssetRequest assetRequest) {
        assetService.saveRequestToStorage(assetRequest);
    }

    @GetMapping()
    public @ResponseBody
    List<AssetDto> getAllAssets() {
        return assetService.getAll();
    }


    @PutMapping("/{assetId}")
    public void removeAssetFromStorage(@PathVariable String assetId, @RequestParam String storageId) {
        assetService.removeAssetFromStorage(assetId, storageId);
    }
}
