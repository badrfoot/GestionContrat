package fr.agilit.contrat.modification;

import fr.agilit.contrat.entities.Abonne;
import fr.agilit.contrat.entities.EtatAdresse;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;
import java.util.stream.Stream;

interface AbonneRepository extends CrudRepository<Abonne, Long> {

    @Query("SELECT ab FROM Abonne ab join ab.adresses ad WHERE ad.etatAdresse = :etatAdresse and ad.pays = :pays")
    Set<Abonne> findByEtatAndPaysAdresse(EtatAdresse etatAdresse, String pays);

}
