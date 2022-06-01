package com.bht.assetmanagement.core.asset;

import com.bht.assetmanagement.persistence.dto.AssetDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/manager/asset")
@RequiredArgsConstructor
public class ManagerAssetController {
    public final AssetService assetService;

    @GetMapping()
    public @ResponseBody
    List<AssetDto> getAllAssets() {
        return assetService.getAllAssets();
    }
}
