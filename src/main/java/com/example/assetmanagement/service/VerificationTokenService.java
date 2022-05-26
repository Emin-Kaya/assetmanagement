package com.example.assetmanagement.service;

import com.example.assetmanagement.entity.UserAccount;
import com.example.assetmanagement.entity.VerificationToken;
import com.example.assetmanagement.exception.VerificationTokenNotFoundException;
import com.example.assetmanagement.repository.VerificationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VerificationTokenService {

    private final VerificationTokenRepository verificationTokenRepository;
    private final Clock clock;

    private Long verificationTokenExpiration = 45000L;


    public String generateVerificationToken(UserAccount userAccount) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUserAccount(userAccount);
        verificationToken.setExpiryDate(LocalDateTime.now(clock).plusSeconds(verificationTokenExpiration));

        verificationTokenRepository.save(verificationToken);
        return token;
    }

    public VerificationToken findVerificationTokenByToken(String token) {
        Optional<VerificationToken> verificationToken = verificationTokenRepository.findVerificationTokenByToken(token);
        verificationToken.orElseThrow(VerificationTokenNotFoundException::new);

        return verificationToken.get();
    }

    public void deleteVerificationToken(String token) {
        int deleted = verificationTokenRepository.deleteVerificationTokenByToken(token);
        if (deleted == 0) {
            throw new RuntimeException("Could not delete token"); //TODO CUSTOM EXCEPTION
        }
    }
}
