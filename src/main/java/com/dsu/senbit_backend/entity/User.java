package com.dsu.senbit_backend.entity;

import com.dsu.senbit_backend.enums.Domains;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.List;

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

    @JsonIgnore
    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "Collections_Bits",
            joinColumns = { @JoinColumn(name = "users_id") },
            inverseJoinColumns = { @JoinColumn(name = "bits_id") }
    )
    private List<Bits> collections = new ArrayList<>();
}
