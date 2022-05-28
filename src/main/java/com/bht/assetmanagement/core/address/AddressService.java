package com.bht.assetmanagement.core.address;

import com.bht.assetmanagement.persistence.dto.AddressDto;
import com.bht.assetmanagement.persistence.entity.Address;
import com.bht.assetmanagement.persistence.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddressService {
    private final AddressRepository addressRepository;

    public Address createAddress(AddressDto addressDto) {
        Address address = AddressMapper.INSTANCE.mapDtoToAddress(addressDto);
        return addressRepository.save(address);
    }
}
