package com.example.assetmanagement.service;

import com.example.assetmanagement.dto.*;
import com.example.assetmanagement.entity.Role;
import com.example.assetmanagement.entity.UserAccount;
import com.example.assetmanagement.entity.VerificationToken;
import com.example.assetmanagement.mapper.UserAccountMapper;
import com.example.assetmanagement.repository.UserAccountRepository;
import com.example.assetmanagement.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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

    public void signUp(RegisterRequest registerRequest) {
        if (userAccountService.existsUserAccount(registerRequest.getUsername())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "This user account exists already.");
        }

        registerRequest.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        UserAccount userAccount = UserAccountMapper.INSTANCE.mapRequestToUserAccount(registerRequest);

        userAccount.setRole(Role.EMPLOYEE);
        userAccount.setEnabled(false);
        userAccountRepository.save(userAccount);

        String token = verificationTokenService.generateVerificationToken(userAccount);
        emailService.sendActivationEmail(userAccount.getEmail(), token);
    }

    public String activateAccount(String token) {
        VerificationToken verificationToken = verificationTokenService.findVerificationTokenByToken(token); //TODO ÄNDER NAME
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

    private void sendNewActivationLink(VerificationToken verificationToken) {
        verificationTokenService.deleteVerificationToken(verificationToken.getToken());
        String newToken = verificationTokenService.generateVerificationToken(verificationToken.getUserAccount());
        emailService.sendActivationEmail(verificationToken.getUserAccount().getUsername(), newToken);
    }

}
