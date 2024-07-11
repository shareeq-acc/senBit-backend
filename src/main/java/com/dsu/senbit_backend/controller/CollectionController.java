package com.dsu.senbit_backend.controller;

import com.dsu.senbit_backend.entity.Bits;
import com.dsu.senbit_backend.entity.Collections;
import com.dsu.senbit_backend.entity.User;
import com.dsu.senbit_backend.handler.ResponseHandler;
import com.dsu.senbit_backend.service.BitsService;
import com.dsu.senbit_backend.service.CollectionServices;
import com.dsu.senbit_backend.service.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.Optional;

@RestController
@RequestMapping("/collections")

public class CollectionController {

    @Autowired
    private UserServices userService;

    @Autowired
    private BitsService bitsService;

    @Autowired
    private CollectionServices collectionService;

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUserCollection(@PathVariable Long userId){
        try{
            // Get User In Collection
            Optional<User> userInDb = userService.getUserById(userId);
            if(userInDb.isEmpty()){
                return  ResponseHandler.generateResponse("User Not Found", HttpStatus.NOT_FOUND, null);
            }
            User user = userInDb.get();
            Collections userCollection = collectionService.getAuthorCollection(user);
            if(userCollection == null || userCollection.getCollectionsList().isEmpty()){
                return  ResponseHandler.generateResponse("User has no saved Collection!", HttpStatus.OK, null);
            }
            return  ResponseHandler.generateResponse("User Collection Retrieved Successfully", HttpStatus.OK, userCollection);
        }catch (Exception e){
            return  ResponseHandler.generateResponse("Something Went Wrong!", HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @PutMapping("/{userId}/{bitId}")
    public ResponseEntity<Object> addBitToCollection(@PathVariable Long userId, @PathVariable Long bitId){
        try{
            Optional<User> userInDb = userService.getUserById(userId);
            if(userInDb.isEmpty()){
                return  ResponseHandler.generateResponse("User Not Found", HttpStatus.NOT_FOUND, null);
            }
            Optional<Bits> bitInDb = bitsService.getOneBitsById(bitId);
            if(bitInDb.isEmpty()){
                return  ResponseHandler.generateResponse("Bits Not Found", HttpStatus.NOT_FOUND, null);
            }
            User user = userInDb.get();
            Bits bit = bitInDb.get();

            Collections userCollection = collectionService.getAuthorCollection(user);
            if(userCollection.getCollectionsList().contains(bit)){
                return  ResponseHandler.generateResponse("Bit is Already Added to Collection!", HttpStatus.OK, userCollection);
            }

            collectionService.addBitCollection(user, bit);
            return  ResponseHandler.generateResponse("Bit Added to Collection!", HttpStatus.OK, userCollection);
        }catch (Exception e){
            return  ResponseHandler.generateResponse("Something Went Wrong!", HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @DeleteMapping("{userId}/{bitId}")
    public ResponseEntity<Object> removeBitFromCollection(@PathVariable Long userId, @PathVariable Long bitId){
        try{
            Optional<User> userInDb = userService.getUserById(userId);
            if(userInDb.isEmpty()){
                return  ResponseHandler.generateResponse("User Not Found", HttpStatus.NOT_FOUND, null);
            }
            Optional<Bits> bitInDb = bitsService.getOneBitsById(bitId);
            if(bitInDb.isEmpty()){
                return  ResponseHandler.generateResponse("Bits Not Found", HttpStatus.NOT_FOUND, null);
            }
            User user = userInDb.get();
            Bits bit = bitInDb.get();

            Collections userCollection = collectionService.getAuthorCollection(user);
            if(!(userCollection.getCollectionsList().contains(bit))){
                return  ResponseHandler.generateResponse("Bit Already Removed From Collection!", HttpStatus.OK, userCollection);
            }
            collectionService.removeCollectionBitFromDB(user, bit);
            return  ResponseHandler.generateResponse("Bit Removed from Collection!", HttpStatus.OK, user);
        }catch (Exception e){
            return  ResponseHandler.generateResponse("Something Went Wrong!", HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }
}
