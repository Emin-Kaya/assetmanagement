package com.bht.assetmanagement.core.auth;

import com.bht.assetmanagement.config.security.JwtUtils;
import com.bht.assetmanagement.core.email.EmailService;
import com.bht.assetmanagement.core.refreshToken.RefreshTokenService;
import com.bht.assetmanagement.core.userAccount.UserAccountMapper;
import com.bht.assetmanagement.core.userAccount.UserAccountService;
import com.bht.assetmanagement.core.verificationToken.VerificationTokenService;
import com.bht.assetmanagement.persistence.dto.*;
import com.bht.assetmanagement.persistence.entity.Role;
import com.bht.assetmanagement.persistence.entity.UserAccount;
import com.bht.assetmanagement.persistence.entity.VerificationToken;
import com.bht.assetmanagement.persistence.repository.UserAccountRepository;
import com.bht.assetmanagement.shared.email.EmailUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.net.MalformedURLException;
import java.time.Clock;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserAccountService userAccountService;
    private final PasswordEncoder passwordEncoder;
    private final UserAccountRepository userAccountRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final RefreshTokenService refreshTokenService;
    private final VerificationTokenService verificationTokenService;
    private final Clock clock;
    private final EmailService emailService;
    private final EmailUtils emailUtils;

    public void signUp(RegisterRequest registerRequest, Role role) throws MalformedURLException {
        if (userAccountService.existsUserAccount(registerRequest.getUsername(),registerRequest.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "This user account exists already.");
        }

        registerRequest.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        UserAccount userAccount = UserAccountMapper.INSTANCE.mapRequestToUserAccount(registerRequest);

        userAccount.setRole(role);
        userAccount.setEnabled(false);
        userAccountRepository.save(userAccount);

        String token = verificationTokenService.generateVerificationToken(userAccount);
        emailService.sendMessage(userAccount.getEmail(), emailUtils.getSubjectActivationText(), emailUtils.getBodyActivationText(token));
    }

    public String activateAccount(String token) throws MalformedURLException {
        VerificationToken verificationToken = verificationTokenService.findVerificationTokenByToken(token);
        String msg;
        if (verificationToken.getExpiryDate().isBefore(LocalDateTime.now(clock))) {
            sendNewActivationLink(verificationToken);
            msg = "Link is expired. We have sent you a new link";
        } else {
            userAccountService.enableUserAccount(verificationToken);
            msg = "Account activated";
        }
        return msg;
    }

    public AuthenticationResponse signIn(LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        String jwtToken = jwtUtils.generateJwtToken(authentication);

        return AuthenticationResponse.builder()
                .authenticationToken(jwtToken)
                .username(loginRequest.getUsername())
                .expiresAt(LocalDateTime.now(clock).plusSeconds(jwtUtils.getJwtExpirationMs()))
                .refreshToken(refreshTokenService.generateRefreshToken(loginRequest.getUsername()).getToken())
                .build();
    }

    public String signOut(RefreshTokenRequest refreshTokenRequest) {
        refreshTokenService.deleteRefreshToken(refreshTokenRequest.getRefreshToken(), refreshTokenRequest.getUsername());
        return "You logged out.";
    }

    public AuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        return null;
    }
    //TODO FRAG VIVI

    public void changeUserPassword(PasswordChangeRequest passwordChangeRequest) {
        UserAccount userAccount = userAccountService.getProfileInformation();
        if (!passwordEncoder.matches(passwordChangeRequest.getOldPassword(), userAccount.getPassword())) {
            throw new RuntimeException("Invalid old password");
        }
        userAccount.setPassword(passwordEncoder.encode(passwordChangeRequest.getNewPassword()));
        refreshTokenService.deleteOldRefreshTokenByUsername(userAccount.getUsername());
        userAccountService.updateUserAccount(userAccount);
    }

    public void changeUserEmail(String email) {
        UserAccount userAccount = userAccountService.getCurrenUser();
        userAccount.setEmail(email);
        userAccountService.updateUserAccount(userAccount);
    }

    private void sendNewActivationLink(VerificationToken verificationToken) throws MalformedURLException {
        verificationTokenService.deleteVerificationToken(verificationToken.getToken());
        String newToken = verificationTokenService.generateVerificationToken(verificationToken.getUserAccount());
        emailService.sendMessage(verificationToken.getUserAccount().getEmail(), emailUtils.getSubjectActivationText(), emailUtils.getBodyActivationText(newToken));

    }
}
