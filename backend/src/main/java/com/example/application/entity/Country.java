package com.example.application.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Entity
@Table(name = "countries")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Country {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    // Optional bidirectional mapping can be useful, but to keep it simple and
    // clean,
    // we often just need the ID references or just the list on the parent if
    // needed.
    // However, for API "Fetch states by country ID", simply querying
    // StateRepository by CountryId is enough.
    // So strictly one-to-many isn't always pushed into the entity unless we want
    // cascade deletes.
    // But usually standard JPA:

    // @OneToMany(mappedBy = "country", cascade = CascadeType.ALL, orphanRemoval =
    // true)
    // private List<State> states;
}
