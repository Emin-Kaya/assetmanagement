package com.bht.assetmanagement.core.testData;

import com.bht.assetmanagement.persistence.entity.*;
import com.bht.assetmanagement.persistence.repository.*;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor

public class TestDataService {
    private final AddressRepository addressRepository;
    private final ApplicationUserRepository applicationUserRepository;
    private final AssetInquiryRepository assetInquiryRepository;
    private final AssetRepository assetRepository;
    private final UserAccountRepository userAccountRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private static final Logger logger = LogManager.getLogger();
    private final PasswordEncoder passwordEncoder;


    public void resetAll() {
        logger.info("Start reseting data");
        assetInquiryRepository.deleteAll();
        addressRepository.deleteAll();
        applicationUserRepository.deleteAll();
        assetRepository.deleteAll();
        verificationTokenRepository.deleteAll();
        userAccountRepository.deleteAll();

        initializeTestData();
        logger.info("Finished reseting data");
    }

    public void initializeTestData() {


    }
}
