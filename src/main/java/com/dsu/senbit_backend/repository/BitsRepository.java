package com.dsu.senbit_backend.repository;

import com.dsu.senbit_backend.entity.Bits;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface BitsRepository extends JpaRepository<Bits, Long> {
    List<Bits> findByAuthorId(Long userId);

    @Transactional
    void deleteByAuthorId(long userId);
}
