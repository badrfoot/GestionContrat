package fr.agilit.contrat.entities;


import lombok.*;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDate;

@Entity
@EqualsAndHashCode(exclude = {"id"})
@Data
@Builder(toBuilder = true)
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
    private TypeAdresse typeAdresse;

    @Enumerated(EnumType.STRING)
    private EtatAdresse etatAdresse;

}
