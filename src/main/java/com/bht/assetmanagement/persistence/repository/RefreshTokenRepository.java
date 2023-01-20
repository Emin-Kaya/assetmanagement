package com.bht.assetmanagement.persistence.repository;

import com.bht.assetmanagement.persistence.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findRefreshTokenByToken(String token);

    int deleteRefreshTokenByToken(String token);

    int deleteRefreshTokenByUsernameAndCreatedAtIsBefore(String username, LocalDateTime now);
}