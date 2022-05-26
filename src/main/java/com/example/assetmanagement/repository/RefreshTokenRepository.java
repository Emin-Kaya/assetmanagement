package com.example.assetmanagement.repository;

import com.example.assetmanagement.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findRefreshTokenByToken(String token);

    int deleteRefreshTokenByTokenAndUsername(String token, String username);

    int deleteRefreshTokenByUsernameAndCreatedAtIsBefore(String username, LocalDateTime now);
}