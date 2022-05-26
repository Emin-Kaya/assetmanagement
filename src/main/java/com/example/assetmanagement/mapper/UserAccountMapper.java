package com.example.assetmanagement.mapper;

import com.example.assetmanagement.dto.RegisterRequest;
import com.example.assetmanagement.entity.UserAccount;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserAccountMapper {
    UserAccountMapper INSTANCE = Mappers.getMapper(UserAccountMapper.class);

    UserAccount mapRequestToUserAccount(RegisterRequest registerRequest);
}
