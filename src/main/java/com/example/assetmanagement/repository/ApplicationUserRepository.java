package com.example.assetmanagement.repository;

import com.example.assetmanagement.entity.ApplicationUser;
import com.example.assetmanagement.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ApplicationUserRepository extends JpaRepository<ApplicationUser, String> {
    Optional<ApplicationUser> findApplicationUserByUserAccount(UserAccount userAccount);

}
