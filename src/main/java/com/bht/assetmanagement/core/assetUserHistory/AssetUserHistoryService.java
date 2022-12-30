package com.bht.assetmanagement.core.assetUserHistory;

import com.bht.assetmanagement.persistence.entity.ApplicationUser;
import com.bht.assetmanagement.persistence.entity.Asset;
import com.bht.assetmanagement.persistence.entity.AssetUserHistory;
import com.bht.assetmanagement.persistence.entity.LendStatus;
import com.bht.assetmanagement.persistence.repository.AssetUserHistoryRepository;
import com.bht.assetmanagement.shared.date.DateUtils;
import com.bht.assetmanagement.shared.exception.EntryNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.bht.assetmanagement.persistence.entity.LendStatus.RENTED;

@Service
@RequiredArgsConstructor
public class AssetUserHistoryService {
    private final AssetUserHistoryRepository assetUserHistoryRepository;
    private final DateUtils dateUtils;


    public AssetUserHistory create(ApplicationUser applicationUser, Asset asset) {

        AssetUserHistory assetUserHistory = new AssetUserHistory();
        assetUserHistory.setApplicationUser(applicationUser);
        assetUserHistory.setLendStatus(RENTED);
        assetUserHistory.setAsset(asset);
        assetUserHistory.setRendDate(dateUtils.createLocalDate());
        return assetUserHistoryRepository.save(assetUserHistory);
    }

    public AssetUserHistory update(ApplicationUser applicationUser, Asset asset) {
        AssetUserHistory assetUserHistory = assetUserHistoryRepository
                .findByApplicationUserAndAssetAndLendStatus(applicationUser, asset, RENTED)
                .orElseThrow(() -> new EntryNotFoundException("History entry of application user with id: "
                        + applicationUser.getId() + " and asset with id: " + asset.getId() + " was not found."));
        assetUserHistory.setLendStatus(LendStatus.RETURNED);
        assetUserHistory.setReturnDate(dateUtils.createLocalDate());
        return assetUserHistoryRepository.save(assetUserHistory);
    }
}
