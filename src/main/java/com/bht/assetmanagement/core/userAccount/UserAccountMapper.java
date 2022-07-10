package com.bht.assetmanagement.core.userAccount;

import com.bht.assetmanagement.persistence.dto.RegisterRequest;
import com.bht.assetmanagement.persistence.dto.UserAccountDto;
import com.bht.assetmanagement.persistence.dto.UserAccountRequest;
import com.bht.assetmanagement.persistence.entity.UserAccount;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserAccountMapper {
    UserAccountMapper INSTANCE = Mappers.getMapper(UserAccountMapper.class);

    UserAccount mapRequestToUserAccount(RegisterRequest registerRequest);

    UserAccount mapUserAccountRequestToUserAccount(UserAccountRequest userAccountRequest);

    UserAccountDto mapUserAccountToUserAccountDto(UserAccount userAccount);
}
