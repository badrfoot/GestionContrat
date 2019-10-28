package fr.agilit.contrat;


import fr.agilit.contrat.entities.Utilisateur;
import org.springframework.data.repository.CrudRepository;

public interface UtilisateurRepository extends CrudRepository<Utilisateur, Long> {
    // Rien à faire pour le moment
}
