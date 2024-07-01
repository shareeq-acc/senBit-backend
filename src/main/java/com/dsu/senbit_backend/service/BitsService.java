package com.dsu.senbit_backend.service;

import com.dsu.senbit_backend.entity.Bits;
import com.dsu.senbit_backend.entity.User;
import com.dsu.senbit_backend.repository.BitsRepository;
import com.dsu.senbit_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class BitsService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private BitsRepository bitsRepo;

    public Optional<Bits> getOneBitsById(Long bitId){
        return bitsRepo.findById(bitId);
    }

    public List<Bits> getAllBits(){
        return bitsRepo.findAll();
    }

    public List<Bits> getBitsByUser(Long userId){
        return bitsRepo.findByAuthorId(userId);
    }

    public void createBit(Bits bits){
        bitsRepo.save(bits);
    }

    public void updateBit(Bits bits){
        bitsRepo.save(bits);
    }

    public void deleteAllByUser(Long userId){
        bitsRepo.deleteByAuthorId(userId);
    }

    public void deleteBit(Long bitsId){
        bitsRepo.deleteById(bitsId);
    }

    public void likeBit(Bits bits, Long userId){
       bits.getLikedBy().add(userId);
       bitsRepo.save(bits);
    }

    public void unlikeBit(Bits bits, Long userId){
        bits.getLikedBy().remove(userId);
        bitsRepo.save(bits);
    }

    public List<Bits> searchBitsBasedOnText(String text){
        return bitsRepo.findByTagIgnoreCase(text);
    }
}
