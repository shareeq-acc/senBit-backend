package com.dsu.senbit_backend.repository;

import com.dsu.senbit_backend.entity.Collections;
import com.dsu.senbit_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CollectionsRepository extends JpaRepository<Collections, Long> {
    Collections findByAuthor(User author);
}
