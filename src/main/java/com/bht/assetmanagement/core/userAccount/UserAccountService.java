package com.bht.assetmanagement.core.userAccount;

import com.bht.assetmanagement.core.applicationUser.ApplicationUserService;
import com.bht.assetmanagement.core.email.EmailService;
import com.bht.assetmanagement.persistence.dto.ProfilInformationDto;
import com.bht.assetmanagement.persistence.dto.UserAccountDto;
import com.bht.assetmanagement.persistence.dto.UserAccountRequest;
import com.bht.assetmanagement.persistence.entity.AssetUserHistory;
import com.bht.assetmanagement.persistence.entity.Role;
import com.bht.assetmanagement.persistence.entity.UserAccount;
import com.bht.assetmanagement.persistence.entity.VerificationToken;
import com.bht.assetmanagement.persistence.repository.UserAccountRepository;
import com.bht.assetmanagement.shared.email.EmailUtils;
import com.bht.assetmanagement.shared.exception.DublicateEntryException;
import com.bht.assetmanagement.shared.exception.EntryNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.bht.assetmanagement.persistence.entity.LendStatus.RENTED;

@Service
@RequiredArgsConstructor
public class UserAccountService implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;
    private final UserAccountRepository userAccountRepository;
    private final EmailService emailService;
    private final EmailUtils emailUtils;
    private final ApplicationUserService applicationUserService;

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

    public boolean existsUserAccountByUserName(String username) {
        return userAccountRepository.existsByUsername(username);
    }

    public boolean existsUserAccountByEmail(String email) {
        return userAccountRepository.existsByEmail(email);
    }

    public boolean existsUserAccount(String username, String email) {
        return existsUserAccountByUserName(username) || existsUserAccountByEmail(email);
    }


    public UserAccount getProfileInformation() {
        return findUserByUsername(getCurrenUser().getUsername());
    }

    public ProfilInformationDto getProfileInformationDto() {
        return UserAccountMapper.INSTANCE.mapUserAccountToProfilInformationDto(getProfileInformation());
    }

    public UserAccount getCurrenUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return findUserByUsername(authentication.getName());
    }

    public UserAccountDto getCurrentUserDto() {
        return UserAccountMapper.INSTANCE.mapUserAccountToUserAccountDto(getCurrenUser());
    }

    public List<UserAccount> getAllUsersByRole(Role role) {
        return userAccountRepository.findAllByRole(role);
    }

    public UserAccount createUserAccount(UserAccountRequest userAccountRequest) throws MalformedURLException {

        if (existsUserAccount(userAccountRequest.getUsername(), userAccountRequest.getEmail())) {
            throw new DublicateEntryException("UserAccount already exists.");
        }

        String password = userAccountRequest.getPassword();

        userAccountRequest.setPassword(passwordEncoder.encode(userAccountRequest.getPassword()));
        UserAccount userAccount = UserAccountMapper.INSTANCE.mapUserAccountRequestToUserAccount(userAccountRequest);

        userAccount.setEnabled(true);
        userAccountRepository.save(userAccount);

        applicationUserService.create(userAccountRequest.getApplicationUserRequest(), userAccount);

        emailService.sendMessage(userAccountRequest.getEmail(), emailUtils.getSubjectNewUserAccount(), emailUtils.getBodyNewUserAccount(userAccountRequest.getEmail(), password));
        return userAccount;
    }

    public void delete(UUID id) {
        UserAccount userAccount = getUserAccountById(id);
        boolean hasAssets = true;

        for (AssetUserHistory assetUserHistory : userAccount.getApplicationUser().getAssetUserHistoryList()) {
            if (assetUserHistory.getLendStatus().equals(RENTED)) {
                hasAssets = false;
                break;
            }
        }

        if (hasAssets) {
            userAccount.setEnabled(false);
            userAccount.setArchived(true);
            userAccountRepository.save(userAccount);
        }
    }

    public List<UserAccountDto> getAll() {
        List<UserAccountDto> userAccountDtos = new ArrayList<>();

        UserAccount currentUser = getCurrenUser();

        userAccountRepository.findAll()
                .stream()
                .filter(it -> it.getId() != currentUser.getId())
                .filter(it -> it.getRole() != Role.ADMIN)
                .filter(it -> !it.isArchived())
                .collect(Collectors.toList())
                .forEach((it -> userAccountDtos.add(UserAccountMapper.INSTANCE.mapUserAccountToUserAccountDto(it))));

        return userAccountDtos;
    }

    public UserAccount getUserAccountById(UUID id) {
        UserAccount userAccount = userAccountRepository.findById(id).orElseThrow(() -> new RuntimeException("UserAccount not found with id" + id));

        if (!userAccount.isArchived()) {
            return userAccount;
        } else throw new RuntimeException("UserAccount not found with id" + id);

    }

    public UserAccountDto getUserAccountDtoById(UUID id) {
        return UserAccountMapper.INSTANCE.mapUserAccountToUserAccountDto(getUserAccountById(id));
    }
}
