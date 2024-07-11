package com.dsu.senbit_backend.service;

import com.dsu.senbit_backend.entity.Bits;
import com.dsu.senbit_backend.entity.Collections;
import com.dsu.senbit_backend.entity.User;
import com.dsu.senbit_backend.repository.CollectionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class CollectionServices {

    @Autowired
    private CollectionsRepository collectionRepo;


    public Collections getAuthorCollection(User user){
        return collectionRepo.findByAuthor(user);
    }

    public void addBitCollection(User user, Bits bit){
        Collections collectionInDb = collectionRepo.findByAuthor(user);
        if(collectionInDb == null){
            Collections c1 = new Collections();
            c1.setAuthor(user);
            c1.getCollectionsList().add(bit);
            collectionRepo.save(c1);
        }else {
            collectionInDb.getCollectionsList().add(bit);
            collectionRepo.save(collectionInDb);
        }
    }

    public void removeCollectionBitFromDB(User user, Bits bit){
        Collections collectionInDb = collectionRepo.findByAuthor(user);
        collectionInDb.getCollectionsList().remove(bit);
        collectionRepo.save(collectionInDb);
    }
}
