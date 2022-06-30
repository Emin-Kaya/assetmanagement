package com.bht.assetmanagement;

import com.bht.assetmanagement.persistence.dto.AddressRequest;
import com.bht.assetmanagement.persistence.dto.ApplicationUserRequest;
import com.bht.assetmanagement.persistence.entity.Address;
import com.bht.assetmanagement.persistence.entity.UserAccount;
import com.bht.assetmanagement.persistence.repository.AddressRepository;
import com.bht.assetmanagement.persistence.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;


public class TestDataBuilder {

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private AddressRepository addressRepository;



    public AddressRequest aAddressRequest() {
        AddressRequest addressRequest = new AddressRequest();

        addressRequest.setStreetName("Naunynstr.");
        addressRequest.setStreetNumber("8");
        addressRequest.setCity("Berlin");
        addressRequest.setPostalCode("10997");
        addressRequest.setCountry("Germany");
        return addressRequest;
    }

    public Address aAddress() {
        Address address = new Address();

        address.setId(UUID.randomUUID());
        address.setStreetName("Naunynstr.");
        address.setStreetNumber("8");
        address.setCity("Berlin");
        address.setPostalCode("10997");
        address.setCountry("Germany");
        return addressRepository.save(address);
    }

    public UserAccount aValidUserAccount(){
            UserAccount userAccount = new UserAccount();
            userAccount.setId(UUID.randomUUID());
            userAccount.setUsername("testtester");
            userAccount.setEmail("test@mail.de");
            userAccount.setPassword("12345678");
            userAccount.setEnabled(true);

            return userAccountRepository.save(userAccount);
    }

}
