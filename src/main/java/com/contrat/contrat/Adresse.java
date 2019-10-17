package com.contrat.contrat;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Adresse extends BaseEntityClass {

    private int numero;

    private String rue;

    private String codePostal;

    private String ville;

    private String pays;

    private LocalDate dateEffet;

    @Enumerated(EnumType.STRING)
    private Type type;

    @Enumerated(EnumType.STRING)
    private Etat etat;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ID_ABONNE")
    private Abonne abonne;

}
