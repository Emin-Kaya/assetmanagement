package com.bht.assetmanagement.core.auth;

import com.bht.assetmanagement.config.security.JwtUtils;
import com.bht.assetmanagement.core.applicationUser.ApplicationUserService;
import com.bht.assetmanagement.core.email.EmailService;
import com.bht.assetmanagement.core.refreshToken.RefreshTokenService;
import com.bht.assetmanagement.core.userAccount.UserAccountMapper;
import com.bht.assetmanagement.core.userAccount.UserAccountService;
import com.bht.assetmanagement.core.verificationToken.VerificationTokenService;
import com.bht.assetmanagement.persistence.dto.*;
import com.bht.assetmanagement.persistence.entity.RefreshToken;
import com.bht.assetmanagement.persistence.entity.Role;
import com.bht.assetmanagement.persistence.entity.UserAccount;
import com.bht.assetmanagement.persistence.entity.VerificationToken;
import com.bht.assetmanagement.persistence.repository.UserAccountRepository;
import com.bht.assetmanagement.shared.email.EmailUtils;
import com.bht.assetmanagement.shared.exception.DublicateEntryException;
import com.bht.assetmanagement.shared.exception.EntryNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

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

    private final ApplicationUserService applicationUserService;

    public void signUp(RegisterRequest registerRequest, Role role) throws MalformedURLException {
        if (userAccountService.existsUserAccount(registerRequest.getUsername(), registerRequest.getEmail())) {
            throw new DublicateEntryException("This user account exists already.");
        }

        registerRequest.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        UserAccount userAccount = UserAccountMapper.INSTANCE.mapRequestToUserAccount(registerRequest);

        userAccount.setRole(role);
        userAccount.setEnabled(false);
        userAccountRepository.save(userAccount);

        try {
            applicationUserService.create(registerRequest.getApplicationUserRequest(), userAccount);
        } catch (Exception e) {
            userAccountRepository.delete(userAccount);
            throw e;
        }

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

        User user = (User) authentication.getPrincipal();
        String role = user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()).get(0);

        String jwtToken = jwtUtils.generateJwtToken(authentication);

        return AuthenticationResponse.builder()
                .authenticationToken(jwtToken)
                .role(role)
                .expiresAt(LocalDateTime.now(clock).plusSeconds(jwtUtils.getJwtExpirationMs()))
                .refreshToken(refreshTokenService.generateRefreshToken(loginRequest.getUsername()).getToken())
                .build();
    }

    public String signOut(String refreshToken) {

        refreshTokenService.deleteRefreshToken(refreshToken);
        return "You logged out.";
    }

    public AuthenticationResponse refreshToken(String refreshToken) {
      //  refreshTokenService.generateRefreshToken(refreshTokenRequest.getUsername());

        RefreshToken token = refreshTokenService.getRefreshtoken(refreshToken);
        UserAccount userAccount = userAccountService.findUserByUsername(token.getUsername());
        String jwtToken = jwtUtils.generateJwtTokenByRefresh(userAccount);


        return AuthenticationResponse.builder()
                .authenticationToken(jwtToken)
                .role("ROLE_"+userAccount.getRole().toString())
                .expiresAt(LocalDateTime.now(clock).plusSeconds(jwtUtils.getJwtExpirationMs()))
                .refreshToken(token.getToken())
                .build();
    }

    public void changeUserPassword(PasswordChangeRequest passwordChangeRequest) {
        UserAccount userAccount = userAccountService.getProfileInformation();
        if (!passwordEncoder.matches(passwordChangeRequest.getOldPassword(), userAccount.getPassword())) {
            throw new EntryNotFoundException("Invalid old password");
        }
        userAccount.setPassword(passwordEncoder.encode(passwordChangeRequest.getNewPassword()));
        refreshTokenService.deleteOldRefreshTokenByUsername(userAccount.getUsername());
        userAccountService.updateUserAccount(userAccount);
    }

    public void changeUserEmail(EmailChangeRequest emailChangeRequest) {
        UserAccount userAccount = userAccountService.getCurrenUser();
        if (!emailChangeRequest.getOldEmail().equals(userAccount.getEmail())) {
            throw new EntryNotFoundException("Invalid old email");
        }
        userAccount.setEmail(emailChangeRequest.getNewEmail());
        userAccountService.updateUserAccount(userAccount);
    }

    private void sendNewActivationLink(VerificationToken verificationToken) throws MalformedURLException {
        verificationTokenService.deleteVerificationToken(verificationToken.getToken());
        String newToken = verificationTokenService.generateVerificationToken(verificationToken.getUserAccount());
        emailService.sendMessage(verificationToken.getUserAccount().getEmail(), emailUtils.getSubjectActivationText(), emailUtils.getBodyActivationText(newToken));

    }
}
