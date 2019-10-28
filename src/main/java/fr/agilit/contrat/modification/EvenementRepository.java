package fr.agilit.contrat.modification;

import fr.agilit.contrat.entities.Evenement;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

interface EvenementRepository extends CrudRepository<Evenement, Long> {

    @Query("SELECT e FROM Evenement e WHERE e.abonne.id = :idAbonne")
    Set<Evenement> findByAbonne(long idAbonne);

}
