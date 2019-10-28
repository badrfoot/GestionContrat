package fr.agilit.contrat.entities;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@AllArgsConstructor @NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter @EqualsAndHashCode(callSuper = false)
public class Evenement extends BaseEntityClass {

    private LocalDate dateModification;

    @OneToOne(optional=false)
    @JoinColumn(name = "ID_ABONNE", nullable=false)
    private Abonne abonne;

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
