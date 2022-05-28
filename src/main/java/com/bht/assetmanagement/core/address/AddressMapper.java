package com.bht.assetmanagement.core.address;

import com.bht.assetmanagement.persistence.dto.AddressDto;
import com.bht.assetmanagement.persistence.dto.ApplicationUserRequest;
import com.bht.assetmanagement.persistence.entity.Address;
import com.bht.assetmanagement.persistence.entity.ApplicationUser;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    AddressMapper INSTANCE = Mappers.getMapper(AddressMapper.class);

    Address mapDtoToAddress(AddressDto addressDto);
}
