package com.bht.assetmanagement.core.address;

import com.bht.assetmanagement.persistence.dto.AddressDto;
import com.bht.assetmanagement.persistence.entity.Address;
import com.bht.assetmanagement.persistence.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AddressService {
    private final AddressRepository addressRepository;

    public Address createAddress(AddressDto addressDto) {
        Address address = AddressMapper.INSTANCE.mapDtoToAddress(addressDto);
        return addressRepository.save(address);
    }

    public Address getAddress(AddressDto addressDto) {
        return addressRepository.findAddressByStreetNameAndStreetNumberAndPostalCode(addressDto.getStreetName(), addressDto.getStreetNumber(), addressDto.getPostalCode()).orElseGet(() -> createAddress(addressDto));
    }

}
