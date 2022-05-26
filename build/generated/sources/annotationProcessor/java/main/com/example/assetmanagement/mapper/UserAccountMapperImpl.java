package com.example.assetmanagement.mapper;

import com.example.assetmanagement.dto.RegisterRequest;
import com.example.assetmanagement.entity.UserAccount;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-05-26T19:09:06+0200",
    comments = "version: 1.5.0.RC1, compiler: IncrementalProcessingEnvironment from gradle-language-java-7.4.jar, environment: Java 17.0.2 (Oracle Corporation)"
)
@Component
public class UserAccountMapperImpl implements UserAccountMapper {

    @Override
    public UserAccount mapRequestToUserAccount(RegisterRequest registerRequest) {
        if ( registerRequest == null ) {
            return null;
        }

        UserAccount userAccount = new UserAccount();

        userAccount.setEmail( registerRequest.getEmail() );
        userAccount.setUsername( registerRequest.getUsername() );
        userAccount.setPassword( registerRequest.getPassword() );

        return userAccount;
    }
}
