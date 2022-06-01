package com.bht.assetmanagement.core.asset;

import com.bht.assetmanagement.persistence.dto.AssetDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/asset")
@RequiredArgsConstructor
public class AssetController {
    public final AssetService assetService;

    @GetMapping
    public @ResponseBody
    List<AssetDto> gettAllAssetsOfUser() {
        return assetService.getAllAssetsOfUser();
    }

    @PutMapping("/{assetId}")
    public void removeAssetFromUser(@PathVariable String assetId) {
        assetService.removeAssetFromUser(assetId);
    }
}
