package com.contrat.contrat;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Contrat extends BaseEntityClass{

    private String objet;

    private LocalDate dateDebut;

    private LocalDate dateFin;

    private String document;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ID_ABONNE")
    private Abonne abonne;
}
