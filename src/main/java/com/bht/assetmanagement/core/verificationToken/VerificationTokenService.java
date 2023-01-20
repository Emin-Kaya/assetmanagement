package com.bht.assetmanagement.core.verificationToken;

import com.bht.assetmanagement.persistence.entity.UserAccount;
import com.bht.assetmanagement.persistence.entity.VerificationToken;
import com.bht.assetmanagement.persistence.repository.VerificationTokenRepository;
import com.bht.assetmanagement.shared.exception.CouldNotDeleteException;
import com.bht.assetmanagement.shared.exception.VerificationTokenNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;
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
        return verificationTokenRepository.findVerificationTokenByToken(token).orElseThrow(() -> new VerificationTokenNotFoundException("Token not found."));
    }

    public void deleteVerificationToken(String token) {
        int deleted = verificationTokenRepository.deleteVerificationTokenByToken(token);
        if (deleted == 0) {
            throw new CouldNotDeleteException("Could not delete token");
        }
    }
}
