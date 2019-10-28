package fr.agilit.contrat.modification;

import fr.agilit.contrat.UtilisateurRepository;
import fr.agilit.contrat.entities.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping(path = {"/EC", "/FACE", ""})
public class AbonneController {

    static final String PUT_GET_ADRESSE_URL = "/abonnes/{idAbonne}/adresses/{idAdresse}";
    static final String GET_CONTRATS_URL = "/abonnes/{idAbonne}/contrats";
    static final String GET_ABONNE_URL_BY_ID = "/abonnes/{idAbonne}";
    static final String GET_ABONNE_URL = "/abonnes";
    static final String ETATADRESSE_REQUEST_PARAM = "etatAdresse";
    static final String PAYS_REQUEST_PARAM = "pays";
    static final String ID_UTILISATEUR_REQUEST_PARAM = "idUtilisateur";


    private final AbonneRepository abonneRepository;
    private final EvenementRepository evenementRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final AdresseRepository adresseRepository;


    AbonneController(AbonneRepository abonneRepository, EvenementRepository evenementRepository, UtilisateurRepository utilisateurRepository, AdresseRepository adresseRepository) {
        this.abonneRepository = abonneRepository;
        this.evenementRepository = evenementRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.adresseRepository = adresseRepository;
    }

    @GetMapping(GET_ABONNE_URL_BY_ID)
    public ResponseEntity<Abonne> getAbonne(@PathVariable long idAbonne){
        return ResponseEntity.of( abonneRepository.findById(idAbonne) );
    }

    @GetMapping(GET_CONTRATS_URL)
    public ResponseEntity<Set<Contrat>> getContrats(@PathVariable long idAbonne){
        return ResponseEntity.of(abonneRepository.findById(idAbonne).map(Abonne::getContrats));
    }

    @GetMapping(value = GET_ABONNE_URL, params = {ETATADRESSE_REQUEST_PARAM, PAYS_REQUEST_PARAM})
    public ResponseEntity<Set<Abonne>> getAbonneByAdresseInfo(@RequestParam EtatAdresse etatAdresse, @RequestParam String pays){
        return ResponseEntity.ok( abonneRepository.findByEtatAndPaysAdresse(etatAdresse, pays) );
    }

    @GetMapping(PUT_GET_ADRESSE_URL)
    public ResponseEntity<Adresse> getAdresse(@PathVariable long idAbonne, @PathVariable long idAdresse){
        Optional<Adresse> adresseFound = abonneRepository.findById(idAbonne) //
                                                         .flatMap( abonne -> abonne.getAdresseById(idAdresse) );
        return ResponseEntity.of(adresseFound);
    }

    @PutMapping( value = PUT_GET_ADRESSE_URL, params = {ID_UTILISATEUR_REQUEST_PARAM} )
    public ResponseEntity<Adresse> changeAdresse(@PathVariable long idAbonne, @PathVariable long idAdresse, @RequestParam long idUtilisateur, @RequestBody Adresse newAdresse){
        Optional<Abonne> abonne = abonneRepository.findById(idAbonne);
        Optional<Utilisateur> utilisateur = utilisateurRepository.findById(idUtilisateur);

        if ( !(abonne.isPresent()) || (!(utilisateur.isPresent())) ) {
            return ResponseEntity.badRequest().build();
        }

        Abonne abonneToChange = abonne.get();
        Adresse ancienneAdresse = abonneToChange.getAdresseById(idAdresse) //
                                                .orElseThrow( () -> new IllegalStateException("Aucune adresse n'a été trouvée!") );

        newAdresse = adresseRepository.save(newAdresse);
        Evenement evenement = abonneToChange.changeAdresse( newAdresse, ancienneAdresse, utilisateur.get() );
        abonneRepository.save(abonneToChange);
        evenementRepository.save(evenement);

        return ResponseEntity.ok().build();
    }

}
