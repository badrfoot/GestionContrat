package fr.agilit.contrat.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@EqualsAndHashCode(exclude = {"deleted", "document"}, callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Contrat extends BaseEntityClass {

    private String sujet;

    private LocalDate dateDebut;

    private LocalDate dateFin;

    private boolean deleted = false;

    @JsonIgnore
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] document;


    @ManyToOne(optional = false)
    @JoinColumn(name = "ID_ABONNE")
    private Abonne abonne;


    public Adresse getAdresse() {
        return abonne.getAdressePrincipale().orElseThrow( () -> new IllegalStateException("Aucune adresse principale n'a été trouvée!") );
    }

}
