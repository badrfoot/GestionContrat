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
public class Evenement extends BaseEntityClass{

    @Temporal(TemporalType.DATE)
    private LocalDate dateModification;

    @OneToOne(optional=false)
    @JoinColumn(name = "ID_nouvelleAdresse", nullable=false)
    private Adresse nouvelleAdresse;

    @OneToOne(optional=false)
    @JoinColumn(name = "ID_ancienneAdresse", nullable=false)
    private Adresse ancienneAdresse;

    @OneToOne(optional=false)
    @JoinColumn(name = "ID_utilisateur", nullable=false)
    private Utilisateur utilisateur;

}
