package com.dsu.senbit_backend.repository;

import com.dsu.senbit_backend.entity.Bits;
import com.dsu.senbit_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}
