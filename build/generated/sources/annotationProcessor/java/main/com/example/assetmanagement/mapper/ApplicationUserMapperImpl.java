package com.example.assetmanagement.mapper;

import com.example.assetmanagement.dto.ApplicationUserRequest;
import com.example.assetmanagement.entity.ApplicationUser;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-05-26T19:09:06+0200",
    comments = "version: 1.5.0.RC1, compiler: IncrementalProcessingEnvironment from gradle-language-java-7.4.jar, environment: Java 17.0.2 (Oracle Corporation)"
)
@Component
public class ApplicationUserMapperImpl implements ApplicationUserMapper {

    @Override
    public ApplicationUser mapRequestToApplicationUser(ApplicationUserRequest applicationUserRequest) {
        if ( applicationUserRequest == null ) {
            return null;
        }

        ApplicationUser applicationUser = new ApplicationUser();

        applicationUser.setFirstName( applicationUserRequest.getFirstName() );
        applicationUser.setLastName( applicationUserRequest.getLastName() );

        return applicationUser;
    }
}
