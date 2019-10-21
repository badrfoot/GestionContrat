package fr.agilit.contrat.modification;

import fr.agilit.contrat.entities.Adresse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = {"/EC", "/FACE"})
public class ModificationAdresseController {
    static final String PUT_GET_ADRESSE_URL = "/adresses/{id}";

    private AbonneRepository abonneRepository;
    private AdresseRepository adresseRepository;


    ModificationAdresseController(AbonneRepository abonneRepository) {
        this.abonneRepository = abonneRepository;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Adresse> getAdresse(@PathVariable long id){
        return ResponseEntity.of( adresseRepository.findById(id) );
    }

    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<Adresse> changeAdresse(@RequestBody Adresse adresse){
        adresseRepository.save(adresse);
        return ResponseEntity.ok().build();
    }

}
