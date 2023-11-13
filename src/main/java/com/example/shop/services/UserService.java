package com.example.shop.services;

import com.example.shop.models.User;
import com.example.shop.repositories.UserRepository;
import com.example.shop.system.exceptions.ObjectNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;

    public UserService (UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public User findById(Integer id){
        return this.userRepository.findById(id).orElseThrow(()->new ObjectNotFoundException("user", id));
    }

    public List<User> findAll(){
        return this.userRepository.findAll();
    }

    public User save(User user){
        //TODO ENCRYPT PASSWORD BEFORE SAVING IT
        return this.userRepository.save(user);
    }

    public User update(Integer userId, User update){
        User oldUser = this.userRepository.findById(userId).orElseThrow(
                () -> new ObjectNotFoundException("user", userId));
        oldUser.setUsername(update.getUsername());
        oldUser.setName(update.getName());
        oldUser.setSurname(update.getSurname());
        //maybe need to be considered separated like password
        oldUser.setEmail(update.getEmail());
        oldUser.setRoles(update.getRoles());
        oldUser.setBirthDate(update.getBirthDate());
        //better to handle update of sub/related entities differently
//        oldUser.setAddresses(update.getAddresses());
//        oldUser.setCartItems(update.getCartItems());
//        oldUser.setOrderList(update.getOrderList());
        return this.userRepository.save(oldUser);

    }

    public void delete(Integer userId){
        User user = this.userRepository.findById(userId).orElseThrow(() ->
                new ObjectNotFoundException("user", userId));
        this.userRepository.deleteById(userId);

    }
}
