package com.bht.assetmanagement.persistence.repository;

import com.bht.assetmanagement.persistence.entity.ApplicationUser;
import com.bht.assetmanagement.persistence.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApplicationUserRepository extends JpaRepository<ApplicationUser, String> {
    Optional<ApplicationUser> findApplicationUserByUserAccount(UserAccount userAccount);
}
