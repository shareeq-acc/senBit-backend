package com.dsu.senbit_backend.service;

import com.dsu.senbit_backend.entity.User;
import com.dsu.senbit_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UserServices {

    @Autowired
    private UserRepository userRepo;

    public void saveEntry(User user){
        userRepo.save(user);
    }

    public List<User> getAll(){
        return userRepo.findAll();
    }
    public void delAll(){
        userRepo.deleteAll();
    }

    public Optional<User> getUserByEmail(String email){
        return Optional.ofNullable(userRepo.findByEmail(email));
    }

    public Optional<User> getUserById(Long userId) {
        return userRepo.findById(userId);
    }

    public void increasePoints(User user,int amount){
        user.setPoints(user.getPoints() + amount);
        userRepo.save(user);
    }
}
