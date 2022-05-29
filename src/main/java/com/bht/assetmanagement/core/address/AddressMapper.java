package com.bht.assetmanagement.core.address;

import com.bht.assetmanagement.persistence.dto.AddressRequest;
import com.bht.assetmanagement.persistence.dto.AddressResponse;
import com.bht.assetmanagement.persistence.entity.Address;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    AddressMapper INSTANCE = Mappers.getMapper(AddressMapper.class);

    Address mapRequestToAddress(AddressRequest addressRequest);

    AddressResponse mapEntityToAddressResponse(Address address);
}
