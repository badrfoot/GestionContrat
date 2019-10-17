package com.contrat.contrat;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Abonne extends BaseEntityClass {

    private String nom;

    private String prenom;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "abonne")
    private List<Adresse> adresses;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "abonne")
    private List<Contrat> contrats;
}
