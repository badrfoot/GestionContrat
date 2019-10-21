package fr.agilit.contrat.entities;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Evenement extends BaseEntityClass {

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
