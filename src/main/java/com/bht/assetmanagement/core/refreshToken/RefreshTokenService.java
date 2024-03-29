package com.bht.assetmanagement.core.refreshToken;

import com.bht.assetmanagement.persistence.entity.RefreshToken;
import com.bht.assetmanagement.persistence.repository.RefreshTokenRepository;
import com.bht.assetmanagement.shared.exception.EntryNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final Clock clock;

    public RefreshToken generateRefreshToken(String username) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setCreatedAt(LocalDateTime.now());
        refreshToken.setUsername(username);

        return refreshTokenRepository.save(refreshToken);
    }

    public void deleteRefreshToken(String refreshToken) {

        int countDelete = refreshTokenRepository.deleteRefreshTokenByToken(refreshToken);
        if (countDelete == 0) {
            throw new RuntimeException("Could not delete token");
        }
    }

    public void deleteOldRefreshTokenByUsername(String username) {
        int deleted = refreshTokenRepository.deleteRefreshTokenByUsernameAndCreatedAtIsBefore(username, LocalDateTime.now(clock));
        if (deleted == 0) {
            throw new RuntimeException("Could not delete token");
        }
    }

    public RefreshToken getRefreshtoken(String refreshToken) {
        return refreshTokenRepository.findRefreshTokenByToken(refreshToken).orElseThrow(()-> new EntryNotFoundException("Refreshtoken found"));
    }
}
