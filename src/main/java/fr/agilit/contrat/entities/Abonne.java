package fr.agilit.contrat.entities;

import com.google.common.collect.Lists;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Abonne extends BaseEntityClass {

    private String nom;

    private String prenom;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "ID_ABONNE")
    private List<Adresse> adresses = Lists.newArrayList();

    @Getter(value = AccessLevel.NONE)
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "abonne")
    private List<Contrat> contrats = Lists.newArrayList();


    public Abonne(String nom, String prenom, List<Adresse> adresses) {
        Assert.isTrue( getAdressePrincipaleStream(adresses).count()>0, "Une adresse principale doit être définie parmi les adresses de l'abonné(e)" );

        this.nom = nom;
        this.prenom = prenom;
        this.adresses = adresses;
    }

    public List<Contrat> getContrats() {
        return Collections.unmodifiableList(contrats);
    }

    public boolean addContrat(Contrat... contrats){
        return this.contrats.addAll(Lists.newArrayList(contrats));
    }

    public boolean removeContrat(Contrat contrat){
        return this.contrats.remove(contrat);
    }

    public Optional<Adresse> getAdressePrincipale() {
        return getAdressePrincipaleStream(this.adresses).findFirst();
    }

    public Stream<Adresse> getAdressePrincipaleStream(List<Adresse> adresses){
        return adresses.stream()
                .filter(adresse -> TypeAdresse.PRINCIPALE.equals(adresse.getTypeAdresse()) );
    }
}
