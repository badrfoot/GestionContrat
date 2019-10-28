package fr.agilit.contrat.entities;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

@Entity
@Getter @AllArgsConstructor @NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonAutoDetect(getterVisibility = Visibility.NONE, //
                setterVisibility = Visibility.NONE, //
                fieldVisibility = Visibility.ANY)
public class Abonne extends BaseEntityClass {

    private String nom;

    private String prenom;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "ID_ABONNE")
    private Set<Adresse> adresses = Sets.newHashSet();

    @JsonIgnore
    @Getter(value = AccessLevel.NONE)
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "abonne")
    private Set<Contrat> contrats = Sets.newHashSet();


    public Abonne(String nom, String prenom, Set<Adresse> adresses) {
        this.nom = nom;
        this.prenom = prenom;
        this.adresses = adresses;
    }

    public Set<Contrat> getContrats() {
        return Collections.unmodifiableSet(contrats);
    }

    public boolean addContrat(Contrat... contrats){
        return this.contrats.addAll(Lists.newArrayList(contrats));
    }

    public boolean removeContrat(Contrat contratToRemove){
        if ( contrats.contains(contratToRemove) ){
            contrats.stream()
                    .filter( contrat -> contrat.equals(contratToRemove) )
                    .findFirst()
                    .ifPresent( contrat -> contrat.setDeleted(true) );
            return true;
        }

        return false;
    }

    public Optional<Adresse> getAdressePrincipale() {
        return getAdressePrincipaleStream(this.adresses).findFirst();
    }

    private Stream<Adresse> getAdressePrincipaleStream(Set<Adresse> adresses){
        return adresses.stream().filter(Adresse::isPrincipale);
    }

    public Optional<Adresse> getAdresseById(long idAdresse){
        return adresses.stream().filter( adresse -> Objects.equals(adresse.getId(), idAdresse)) //
                                .findFirst();
    }

    public Evenement changeAdresse(Adresse newAdresse, Adresse ancienneAdresse, Utilisateur utilisateur) {
        if ( adresses.contains(newAdresse) ){
            return new Evenement();
        }

        Optional<Adresse> ancienAdressePrincipale = getAdressePrincipale();
        if ( newAdresse.isPrincipale() && ancienAdressePrincipale.isPresent() ){
            getAdressePrincipale().get().setEtatAdresse(EtatAdresse.INACTIVE);
        }

        adresses.add(newAdresse);
        removeAdresse( ancienneAdresse.getId() );
        return new Evenement( LocalDate.now(), this, newAdresse, ancienneAdresse, utilisateur);
    }

    public boolean removeAdresse(long idAdresse) {
        getAdresseById(idAdresse).ifPresent( adresse -> adresse.setDeleted(true) );

        return getAdresseById(idAdresse).isPresent();
    }

}
