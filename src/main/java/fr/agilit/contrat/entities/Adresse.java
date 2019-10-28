package fr.agilit.contrat.entities;


import lombok.*;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDate;

@Entity
@Getter @Setter @Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED) @AllArgsConstructor
@EqualsAndHashCode(exclude = {"deleted"}, callSuper = false)
public class Adresse extends BaseEntityClass {

    private int numero;

    private String rue;

    private String codePostal;

    private String ville;

    private String pays;

    private LocalDate dateEffet;

    private boolean deleted = false;

    @Enumerated(EnumType.STRING)
    private TypeAdresse typeAdresse;

    @Enumerated(EnumType.STRING)
    private EtatAdresse etatAdresse;


    boolean isPrincipale(){
        return this.typeAdresse == TypeAdresse.PRINCIPALE && //
               this.etatAdresse == EtatAdresse.ACTIVE && //
               !this.deleted;
    }

}
