package com.samsung.basicsecurity.repositories.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "catalogs")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Catalogs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "status")
    private boolean status;

    @OneToMany(mappedBy = "catalog")
    @ToString.Exclude
    @JsonIgnore
    private List<Product> products;
}

