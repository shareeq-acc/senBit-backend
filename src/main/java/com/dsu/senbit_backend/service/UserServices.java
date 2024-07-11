package com.dsu.senbit_backend.service;

import com.dsu.senbit_backend.entity.Collections;
import com.dsu.senbit_backend.entity.User;
import com.dsu.senbit_backend.repository.CollectionsRepository;
import com.dsu.senbit_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
public class UserServices {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private CollectionsRepository collectionRepo;

    @Transactional
    public void saveEntry(User user){
        userRepo.save(user);
        Collections collectionInDb = collectionRepo.findByAuthor(user);
        if(collectionInDb == null){
            Collections c1 = new Collections();
            c1.setAuthor(user);
            collectionRepo.save(c1);
        }
    }

    public List<User> getAll(){
        return userRepo.findAll();
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
