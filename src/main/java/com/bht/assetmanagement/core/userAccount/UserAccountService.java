package com.bht.assetmanagement.core.userAccount;

import com.bht.assetmanagement.persistence.dto.UserAccountRequest;
import com.bht.assetmanagement.persistence.entity.Role;
import com.bht.assetmanagement.persistence.entity.UserAccount;
import com.bht.assetmanagement.persistence.entity.VerificationToken;
import com.bht.assetmanagement.persistence.repository.UserAccountRepository;
import com.bht.assetmanagement.shared.exception.EntryNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserAccountService implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;
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

    public List<UserAccount> getAllUsersByRole(Role role) {
        return userAccountRepository.findAllByRole(role);
    }

    public void createAssetManagerUserAccount(UserAccountRequest userAccountRequest) {

        if(existsUserAccount(userAccountRequest.getUsername())) {
            throw new RuntimeException("UserAccount already exists.");
        }

        userAccountRequest.setPassword(passwordEncoder.encode(userAccountRequest.getPassword()));
        UserAccount assetManagerUserAccount = UserAccountMapper.INSTANCE.mapUserAccountRequestToUserAccount(userAccountRequest);

        userAccountRepository.save(assetManagerUserAccount);
    }

    public void deleteUserAccount(UUID id){
        userAccountRepository.findById(id).orElseThrow(() -> new RuntimeException("UserAccount not found with id" + id ));
        userAccountRepository.deleteById(id);

    }
}
