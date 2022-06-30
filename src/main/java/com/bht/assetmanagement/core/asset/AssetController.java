package com.bht.assetmanagement.core.asset;

import com.bht.assetmanagement.persistence.dto.AssetDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/api/v1/asset")
@RequiredArgsConstructor
public class AssetController {
    public final AssetService assetService;

    @GetMapping()
    public @ResponseBody
    List<AssetDto> getAllAssets() {
        return assetService.getAllAssets();
    }

    @DeleteMapping
    public ResponseEntity<String> deleteAsset(@RequestParam String assetId) {
        assetService.deleteAsset(assetId);
        return status(HttpStatus.ACCEPTED).body("Asset with id: " + assetId + " is deleted.");
    }
}
