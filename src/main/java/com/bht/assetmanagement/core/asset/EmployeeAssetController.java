package com.bht.assetmanagement.core.asset;

import com.bht.assetmanagement.persistence.dto.AssetDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/employee/asset")
@RequiredArgsConstructor
public class EmployeeAssetController {
    public final AssetService assetService;

    @GetMapping
    public @ResponseBody
    List<AssetDto> getAllAssetsOfUser() {
        return assetService.getAllAssetsOfUser();
    }

    @PutMapping("/{assetId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeAssetFromUser(@PathVariable String assetId, @RequestParam String storageId) {
        assetService.removeAssetFromUser(assetId, storageId);
    }
}
