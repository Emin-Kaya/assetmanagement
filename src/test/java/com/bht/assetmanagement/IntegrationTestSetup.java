package com.bht.assetmanagement;

import com.bht.assetmanagement.persistence.entity.ApplicationUser;
import com.bht.assetmanagement.persistence.entity.UserAccount;
import com.bht.assetmanagement.persistence.repository.ApplicationUserRepository;
import com.bht.assetmanagement.persistence.repository.UserAccountRepository;
import com.bht.assetmanagement.persistence.repository.VerificationTokenRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.ClassRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@TestPropertySource(properties = {
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.liquibase.enabled=false"
})
@ActiveProfiles("testdata")
public abstract class IntegrationTestSetup extends DependencyContainerSetup {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected VerificationTokenRepository verificationTokenRepository;

    @Autowired
    protected ApplicationUserRepository applicationUserRepository;

    @Autowired
    protected UserAccountRepository userAccountRepository;

    @Autowired
    protected TestDataBuilder testDataBuilder;


    @BeforeEach
    protected void flush() {
        verificationTokenRepository.deleteAll();
        applicationUserRepository.deleteAll();
        userAccountRepository.deleteAll();
    }

    protected SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor getAuthentication(String role) {
        UserAccount userAccount = testDataBuilder.aValidUserAccount(role);
        return user(userAccount.getUsername()).password(userAccount.getPassword()).roles(userAccount.getRole().toString());
    }

    protected SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor getAuthentication(ApplicationUser applicationUser) {
        return user(applicationUser.getUserAccount().getUsername()).password(applicationUser.getUserAccount().getPassword()).roles(applicationUser.getUserAccount().getRole().toString());
    }

    protected String getRequestBody(Object object) throws JsonProcessingException {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(object);
    }
}
