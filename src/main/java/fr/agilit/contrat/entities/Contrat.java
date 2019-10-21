package fr.agilit.contrat.entities;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@EqualsAndHashCode(exclude = {"id"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Contrat extends BaseEntityClass {

    private String objet;

    private LocalDate dateDebut;

    private LocalDate dateFin;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] document;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ID_ABONNE")
    private Abonne abonne;


    public Adresse getAdresse() {
        return abonne.getAdressePrincipale().orElseThrow( () -> new IllegalStateException("Acune addresse principale n'est trouvé pour le client"));
    }
}
