package com.dsu.senbit_backend.entity;
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
@Table(name = "collections")
public class Collections {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "users_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User author;

    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "Collections_Data",
            joinColumns = { @JoinColumn(name = "collections_id") },
            inverseJoinColumns = { @JoinColumn(name = "bits_id") }
    )
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Bits> collectionsList = new ArrayList<>();

}
