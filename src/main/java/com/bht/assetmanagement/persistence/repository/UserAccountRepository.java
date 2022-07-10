package com.bht.assetmanagement.persistence.repository;

import com.bht.assetmanagement.persistence.entity.Role;
import com.bht.assetmanagement.persistence.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, String> {
    Optional<UserAccount> findByUsername(String username);

    List<UserAccount> findAllByRole(Role role);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    Optional<UserAccount> findById(UUID id);

    void deleteById(UUID id);
}
