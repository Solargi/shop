package com.example.shop.services;

import com.example.shop.models.Address;
import com.example.shop.models.User;
import com.example.shop.repositories.AddressRepository;
import com.example.shop.repositories.UserRepository;
import com.example.shop.system.exceptions.ObjectNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class AddressService {
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    public AddressService (AddressRepository addressRepository, UserRepository userRepository){
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
    }

    public Address findById (Integer addressId){
        return this.addressRepository.findById(addressId).orElseThrow(
                () -> new ObjectNotFoundException("address", addressId));
    }

    public List<Address> findAll () {
        return this.addressRepository.findAll();
    }

    public Address save (Address address, Integer userId){
        User foundUser = this.userRepository.findById(userId)
                .orElseThrow(()->new ObjectNotFoundException("user", userId));
        address.setUser(foundUser);
        return this.addressRepository.save(address);
    }

    public Address update(Integer addressId, Address address){
        Address oldAddress = findById(addressId);
        oldAddress.setCity(address.getCity());
        oldAddress.setCountry(address.getCountry());
        oldAddress.setStreet(address.getStreet());
        oldAddress.setZipCode(address.getZipCode());
        oldAddress.setState(address.getState());
        oldAddress.setUser(address.getUser());
        return save(oldAddress, oldAddress.getUser().getId());
    }

    public void delete(Integer addressId){
        Address address = findById(addressId);
        this.addressRepository.deleteById(addressId);
    }
}
