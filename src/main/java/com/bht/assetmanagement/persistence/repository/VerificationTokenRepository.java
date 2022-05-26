package com.bht.assetmanagement.persistence.repository;

import com.bht.assetmanagement.persistence.entity.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {

    Optional<VerificationToken> findVerificationTokenByToken(String token);

    int deleteVerificationTokenByToken(String token);
}