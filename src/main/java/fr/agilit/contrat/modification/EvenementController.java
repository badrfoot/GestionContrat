package fr.agilit.contrat.modification;

import fr.agilit.contrat.entities.Evenement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping(path = {"/EC", "/FACE", ""})
public class EvenementController {

    static final String GET_EVENEMENTS_URL = "/evenements";
    static final String ID_ABONNE_REQUEST_PARAM = "idAbonne";

    private final EvenementRepository evenementRepository;


    public EvenementController(EvenementRepository evenementRepository) {
        this.evenementRepository = evenementRepository;
    }

    @GetMapping(value = GET_EVENEMENTS_URL, params = {ID_ABONNE_REQUEST_PARAM})
    public ResponseEntity<Set<Evenement>> getEvenementForAbonneAndNouvelleAdresse(@RequestParam long idAbonne) {
        return ResponseEntity.ok( evenementRepository.findByAbonne(idAbonne) );
    }

}
