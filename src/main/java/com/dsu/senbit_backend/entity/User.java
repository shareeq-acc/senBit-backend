package com.dsu.senbit_backend.entity;

import com.dsu.senbit_backend.enums.Domains;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue
    private Long id;
    @NonNull
    private String fname;
    @NonNull
    private String lname;
    @NonNull
    private String email;
    @NonNull
    private String password;
    private int points = 15;
    private String bio;
    private Integer experience;
    private String organization;
    private Domains domain;

//    @ManyToMany(mappedBy = "likedBy")
//    private Set<Bits> likedPosts = new HashSet<>();
}
