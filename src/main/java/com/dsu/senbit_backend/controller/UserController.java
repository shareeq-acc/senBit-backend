package com.dsu.senbit_backend.controller;

import com.dsu.senbit_backend.entity.User;
import com.dsu.senbit_backend.handler.ResponseHandler;
import com.dsu.senbit_backend.service.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserServices userService;

    @PostMapping
    public ResponseEntity<Object> createUser(@RequestBody User user){
        try{
            Optional<User> userInDb = userService.getUserByEmail(user.getEmail());
            if(userInDb.isPresent()){
                return  ResponseHandler.generateResponse("User Already Exists with this Email", HttpStatus.BAD_REQUEST, null);
            }
            userService.saveEntry(user);
            return  ResponseHandler.generateResponse("User Created", HttpStatus.CREATED, user);
        }catch(Exception e){
            return  ResponseHandler.generateResponse("Something Went Wrong!", HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Object> loginUser(@RequestBody User requestedUser){
        try{
            Optional<User> userInDb = userService.getUserByEmail(requestedUser.getEmail());
            if(userInDb.isEmpty()){
                return  ResponseHandler.generateResponse("Invalid Credentials", HttpStatus.UNAUTHORIZED, null);
            }
            User user = userInDb.get();
            if(!(user.getPassword().equals(requestedUser.getPassword()))){
                return  ResponseHandler.generateResponse("Invalid Credentials", HttpStatus.UNAUTHORIZED, null);
            }
            return  ResponseHandler.generateResponse("SUCCESSFUL LOGIN", HttpStatus.OK, user);
        }catch(Exception e){
            return  ResponseHandler.generateResponse("Something Went Wrong!", HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Object> updateUser(@PathVariable Long userId, @RequestBody User user){
        try{
            Optional<User> userInDb = userService.getUserById(userId);
            if(userInDb.isEmpty()){
                return  ResponseHandler.generateResponse("User Does Not Exists", HttpStatus.BAD_REQUEST, null);
            }
            User oldEntry = userInDb.get();
            oldEntry.setBio(user.getBio() != null ? user.getBio() : oldEntry.getBio());
            oldEntry.setDomain(user.getDomain() != null ? user.getDomain() : oldEntry.getDomain());
            oldEntry.setExperience(user.getExperience() != null ? user.getExperience() : oldEntry.getExperience());
            oldEntry.setOrganization(user.getOrganization() != null ? user.getOrganization() : oldEntry.getOrganization());

            userService.saveEntry(oldEntry);
            return  ResponseHandler.generateResponse("User Introduction Updated", HttpStatus.OK, oldEntry);
        }catch(Exception e){
            return  ResponseHandler.generateResponse("Something Went Wrong!", HttpStatus.INTERNAL_SERVER_ERROR, null);
        }

    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> displayUser(@PathVariable Long userId){
        try{
            Optional<User> userInDb = userService.getUserById(userId);
            if(userInDb.isEmpty()){
                return  ResponseHandler.generateResponse("User Not Found", HttpStatus.NOT_FOUND, null);
            }
            return  ResponseHandler.generateResponse("User Retrieved Successfully", HttpStatus.OK, userInDb);
        }catch (Exception e){
            return  ResponseHandler.generateResponse("Something Went Wrong!", HttpStatus.INTERNAL_SERVER_ERROR, null);
        }

    }

    // For Testing Purpose
    @GetMapping
    public ResponseEntity<Object> displayAllUsers(){
        List<User> users = userService.getAll();
        return ResponseHandler.generateResponse("Successfully retrieved data!", HttpStatus.OK, users);
    }


    @DeleteMapping
    public boolean deleteAllUsers(){
        userService.delAll();
        return true;
    }
}
