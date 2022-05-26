package com.bht.assetmanagement.core.applicationUser;

import com.bht.assetmanagement.persistence.dto.ApplicationUserRequest;
import com.bht.assetmanagement.persistence.entity.ApplicationUser;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ApplicationUserMapper {
    ApplicationUserMapper INSTANCE = Mappers.getMapper(ApplicationUserMapper.class);

    ApplicationUser mapRequestToApplicationUser(ApplicationUserRequest applicationUserRequest);
}
