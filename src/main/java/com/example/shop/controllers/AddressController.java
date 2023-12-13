package com.example.shop.controllers;

import com.example.shop.dtos.AddressDTO;
import com.example.shop.dtos.AddressRequestDTO;
import com.example.shop.dtos.converters.AddressRequestDTOToAddressConverter;
import com.example.shop.dtos.converters.AddressToAddressDTOConverter;
import com.example.shop.dtos.converters.AddressDTOToAddressConverter;
import com.example.shop.models.Address;
import com.example.shop.models.User;
import com.example.shop.services.AddressService;
import com.example.shop.services.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.endpoint.base-url}/addresses")
@AllArgsConstructor
public class AddressController {
    private AddressToAddressDTOConverter addressToAddressDTOConverter;
    private AddressDTOToAddressConverter addressDTOToAddressConverter;
    private AddressRequestDTOToAddressConverter addressRequestDTOToAddressConverter;
    private AddressService addressService;

    @GetMapping("/{addressId}")
    ResponseEntity<AddressDTO> getAddress(@PathVariable Integer addressId){
        Address address = this.addressService.findById(addressId);
        return ResponseEntity.ok(this.addressToAddressDTOConverter.convert(address));

    }
    @GetMapping("")
    ResponseEntity<List<AddressDTO>> getAddresses (){
        return ResponseEntity.ok(this.addressService.findAll()
                .stream()
                .map(this.addressToAddressDTOConverter::convert)
                .toList());
    }

    @PostMapping("/{userId}")
    ResponseEntity<AddressDTO> saveAddress(@Valid @RequestBody AddressRequestDTO addressRequestDTO,
                                           @PathVariable Integer userId){
        Address address = this.addressRequestDTOToAddressConverter.convert(addressRequestDTO);
        //TODO rework address save controller and service fetch user in service save
        // remove user dto use userId is enough
        Address savedAddress = this.addressService.save(address, userId);
        return ResponseEntity.ok(this.addressToAddressDTOConverter.convert(savedAddress));
    }

    @PutMapping("/{addressId}")
    ResponseEntity<AddressDTO> updateAddress (@PathVariable Integer addressId, @Valid @RequestBody AddressDTO addressDTO){
        Address address = this.addressDTOToAddressConverter.convert(addressDTO);
        Address updatedAddress = this.addressService.update(addressId,address);
        AddressDTO updatedAddressDTO = this.addressToAddressDTOConverter.convert(updatedAddress);
        return ResponseEntity.ok(updatedAddressDTO);
    }

    @DeleteMapping("/{addressId}")
    public ResponseEntity<String> deleteAddress (@PathVariable Integer addressId){
        this.addressService.delete(addressId);
        return ResponseEntity.ok("Address deleted successfully!");
    }

}
