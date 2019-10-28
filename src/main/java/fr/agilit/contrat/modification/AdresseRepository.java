package fr.agilit.contrat.modification;

import com.sun.xml.bind.v2.model.core.ID;
import fr.agilit.contrat.entities.Adresse;
import org.springframework.data.repository.CrudRepository;

public interface AdresseRepository extends CrudRepository<Adresse, Long> {
    // Rien à faire pour le moment
}
