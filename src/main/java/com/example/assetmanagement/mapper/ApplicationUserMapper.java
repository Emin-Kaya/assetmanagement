package com.example.assetmanagement.mapper;

import com.example.assetmanagement.dto.ApplicationUserRequest;
import com.example.assetmanagement.entity.ApplicationUser;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ApplicationUserMapper {
    ApplicationUserMapper INSTANCE = Mappers.getMapper(ApplicationUserMapper.class);

    ApplicationUser mapRequestToApplicationUser(ApplicationUserRequest applicationUserRequest);
}
