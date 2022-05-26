package com.example.assetmanagement.service;

import com.example.assetmanagement.entity.UserAccount;
import com.example.assetmanagement.entity.VerificationToken;
import com.example.assetmanagement.exception.EntryNotFoundException;
import com.example.assetmanagement.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserAccountService implements UserDetailsService {

    private final UserAccountRepository userAccountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserAccount userAccount = findUserByUsername(username);

        return new User(
                userAccount.getUsername(),
                userAccount.getPassword(),
                userAccount.isEnabled(),
                userAccount.isAccountNonExpired(),
                userAccount.isCredentialsNonExpired(),
                userAccount.isAccountNonLocked(),
                userAccount.getAuthorities()
        );
    }

    private UserAccount findUserByUsername(String username) {
        Optional<UserAccount> userAccountOpt = userAccountRepository.findByUsername(username);

        return userAccountOpt.orElseThrow(() -> new EntryNotFoundException("User account not found"));
    }

    public void enableUserAccount(VerificationToken verificationToken) {
        String username = verificationToken.getUserAccount().getUsername();
        UserAccount userAccount = findUserByUsername(username);
        if (userAccount.isEnabled()) {
            throw new RuntimeException("Already activated.");
        } else {
            userAccount.setEnabled(true);
            userAccountRepository.save(userAccount);
        }
    }

    public void updateUserAccount(UserAccount userAccount) {
        userAccountRepository.save(userAccount);
    }

    public boolean existsUserAccount(String username) {
        return userAccountRepository.existsByUsername(username);
    }

    public UserAccount getProfileInformation() {
        return findUserByUsername(getCurrenUser().getUsername());
    }

    public UserAccount getCurrenUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return findUserByUsername(authentication.getName());
    }

    public UserAccount findByUsername(String name) {
        Optional<UserAccount> userAccount = userAccountRepository.findByUsername(name);
        return userAccount.orElseThrow();
    }
}
