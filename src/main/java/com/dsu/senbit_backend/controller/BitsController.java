package com.dsu.senbit_backend.controller;

import com.dsu.senbit_backend.entity.Bits;
import com.dsu.senbit_backend.entity.User;
import com.dsu.senbit_backend.handler.ResponseHandler;
import com.dsu.senbit_backend.service.BitsService;
import com.dsu.senbit_backend.service.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/bits")
public class BitsController {

    @Autowired
    private BitsService bitsService;

    @Autowired
    private UserServices userServices;

    // Create Bit
    @PostMapping("/new/{userId}")
    public ResponseEntity<Object> createBits(@RequestBody Bits bits, @PathVariable Long userId){
        try{
            Optional<User> userInDb = userServices.getUserById(userId);
            if(userInDb.isEmpty()){
                return  ResponseHandler.generateResponse("User Not Found", HttpStatus.NOT_FOUND, null);
            }
            User author = userInDb.get();
            bits.setAuthor(author);
            bits.setCreatedAt(LocalDateTime.now());
            bitsService.createBit(bits);
            // Increase Points
            userServices.increasePoints(author, 10);
            return  ResponseHandler.generateResponse("Bit Created!", HttpStatus.CREATED, bits);
        }catch (Exception e){
            return  ResponseHandler.generateResponse("Something Went Wrong!", HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    // Get Bit Based on User Id
    @GetMapping("/user/{userId}")
    public ResponseEntity<Object> getBitsByUser(@PathVariable Long userId){
        try{
            Optional<User> userInDb = userServices.getUserById(userId);
            if(userInDb.isEmpty()){
                return  ResponseHandler.generateResponse("User Not Found", HttpStatus.NOT_FOUND, null);
            }
            List<Bits> userBits = bitsService.getBitsByUser(userId);
            if(userBits.isEmpty()){
                return  ResponseHandler.generateResponse("No Bits Created Yet!", HttpStatus.NO_CONTENT, null);
            }
            return  ResponseHandler.generateResponse("Successfully Fetched Bits!", HttpStatus.OK, userBits);
        }catch (Exception e){
            return  ResponseHandler.generateResponse("Something Went Wrong!", HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    // Get All Bits
    @GetMapping
    public ResponseEntity<Object> getAllBits(){
        try{
            List<Bits> bits = bitsService.getAllBits();
            if(bits.isEmpty()){
                return  ResponseHandler.generateResponse("No Bits Created Yet!", HttpStatus.NO_CONTENT, null);
            }
            return  ResponseHandler.generateResponse("Successfully Fetched Bits!", HttpStatus.OK, bits);
        }catch (Exception e){
            return  ResponseHandler.generateResponse("Something Went Wrong!", HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    // Get Bits based on BitId
    @GetMapping("/{bitId}")
    public ResponseEntity<Object> getBitById(@PathVariable Long bitId){
        try{
            Optional<Bits> bit = bitsService.getOneBitsById(bitId);
            if(bit.isEmpty()){
                return  ResponseHandler.generateResponse("No Bit Found with the provided Details!", HttpStatus.NOT_FOUND, null);
            }
            return  ResponseHandler.generateResponse("Successfully Fetched Bit!", HttpStatus.OK, bit);
        }catch (Exception e){
            return  ResponseHandler.generateResponse("Something Went Wrong!", HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    // Get All Bits by User
    @DeleteMapping("/{bitsId}")
    public ResponseEntity<Object> deleteBits(@PathVariable Long bitsId,  @RequestParam Long userId){
        try{
            Optional<User> userInDb = userServices.getUserById(userId);
            if(userInDb.isEmpty()){
                return  ResponseHandler.generateResponse("User Not Found", HttpStatus.NOT_FOUND, null);
            }
            Optional<Bits> bits = bitsService.getOneBitsById(bitsId);
            if(bits.isEmpty()){
                return  ResponseHandler.generateResponse("Bits Not Found", HttpStatus.NOT_FOUND, null);
            }
            User author = userInDb.get();
            Bits userBit = bits.get();

            if(userBit.getAuthor() != author){
                return  ResponseHandler.generateResponse("Unauthorized to Delete this Bit", HttpStatus.UNAUTHORIZED, null);
            }
            bitsService.deleteBit(bitsId);
            return  ResponseHandler.generateResponse("Successfully Deleted Bit!", HttpStatus.OK, null);

        }catch (Exception e){
            return  ResponseHandler.generateResponse("Something Went Wrong!", HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    // Like/Unlike a Bit!
    @PostMapping("/like/{bitsId}")
    public ResponseEntity<Object> likeBit(@PathVariable Long bitsId,  @RequestParam Long userId){
        try {
            Optional<User> userInDb = userServices.getUserById((long)userId);
            if(userInDb.isEmpty()){
                return  ResponseHandler.generateResponse("User Not Found", HttpStatus.NOT_FOUND, null);
            }
            Optional<Bits> bits = bitsService.getOneBitsById(bitsId);
            if(bits.isEmpty()){
                return  ResponseHandler.generateResponse("Bits Not Found", HttpStatus.NOT_FOUND, null);
            }
            User author = userInDb.get();
            Bits userBit = bits.get();



//            if(userBit.getLikedBy().stream().anyMatch(user -> user.getId().equals(author.getId()))){
            if(userBit.getLikedBy().contains(userId)){
                bitsService.unlikeBit(userBit, author.getId());
                return  ResponseHandler.generateResponse("UNLIKED", HttpStatus.OK, userBit);
            }else {
                bitsService.likeBit(userBit, author.getId());
                return  ResponseHandler.generateResponse("LIKED", HttpStatus.OK, userBit);
            }
        }catch (Exception e){
            return  ResponseHandler.generateResponse("Something Went Wrong!", HttpStatus.INTERNAL_SERVER_ERROR, null);

        }
    }

    // Search Bits Based on Text
    @GetMapping("/search")
    public ResponseEntity<Object> searchBits(@RequestParam String text){
        try{
            if(text.isEmpty()){
                return  ResponseHandler.generateResponse("No Result! Try Searching Something!", HttpStatus.BAD_REQUEST, null);
            }
            List<Bits> result = bitsService.searchBitsBasedOnText(text);
            if(result.isEmpty()){
                return  ResponseHandler.generateResponse("No Result Found for " + text + "!", HttpStatus.NOT_FOUND, null);
            }
            return  ResponseHandler.generateResponse("Loading Results for: " + text +":\n", HttpStatus.OK, result);
        }catch (Exception e){
            return  ResponseHandler.generateResponse("Something Went Wrong!", HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

}
