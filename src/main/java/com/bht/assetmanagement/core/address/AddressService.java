package com.bht.assetmanagement.core.address;

import com.bht.assetmanagement.persistence.dto.AddressRequest;
import com.bht.assetmanagement.persistence.entity.Address;
import com.bht.assetmanagement.persistence.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddressService {
    private final AddressRepository addressRepository;

    public Address getOrCreateAddress(AddressRequest addressRequest) {
        return addressRepository
                .findAddressByStreetNameAndStreetNumberAndPostalCode(
                        addressRequest.getStreetName(),
                        addressRequest.getStreetNumber(),
                        addressRequest.getPostalCode())
                .orElseGet(() -> createAddress(addressRequest));
    }

    private Address createAddress(AddressRequest addressRequest) {
        Address address = AddressMapper.INSTANCE.mapRequestToAddress(addressRequest);
        return addressRepository.save(address);
    }
}
