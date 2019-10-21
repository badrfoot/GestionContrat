package fr.agilit.contrat.modification;

import fr.agilit.contrat.IntegrationTestSetup;
import fr.agilit.contrat.entities.*;
import io.cucumber.java.fr.Alors;
import io.cucumber.java.fr.Et;
import io.cucumber.java.fr.Etantdonné;
import io.cucumber.java.fr.Lorsque;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.Month;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

public class ModificationContratBDDTest extends IntegrationTestSetup {

    private static final LocalDate DATE_OF_30_08_2019 =  LocalDate.of(2019, Month.AUGUST, 30);

    private static final String PUT_GET_ADRESSE_URL = "/adresses/{id}";
    private static final String GET_EVENEMENT_URL = "/evenments/{id}";

    private Abonne abonne;
    private Adresse nouvelleAdresse;
    private Adresse adresseActuelle;
    private String canal;

    @Autowired private EntityManager entityManager;
    @Autowired private AbonneRepository abonneRepository;
    @Autowired private TestRestTemplate testRestTemplate;


    @Etantdonné("^un abonné avec une adresse principale (\\w+) en (\\w+)$")
    public void abonne_avec_adresse(String etatAdresse, String pays){
        adresseActuelle = createAdresseForTest(etatAdresse, pays);
        abonne = createAndSaveAbonneForTest(adresseActuelle);
    }

    private Abonne createAndSaveAbonneForTest(Adresse adresse){
        Abonne abonne = new Abonne( "Yosra", "CHAABANE", Lists.newArrayList(adresse) );
        abonne.addContrat( createContratForTest(abonne) );
        abonneRepository.save(abonne);

        return abonne;
    }

    private Adresse createAdresseForTest(String etatAdresse, String pays){
        return Adresse.builder().numero(12)
                                .rue("rue Neige Blanche")
                                .codePostal("75000")
                                .ville("Paris")
                                .pays(pays)
                                .typeAdresse(TypeAdresse.PRINCIPALE)
                                .etatAdresse( EtatAdresse.valueOf(etatAdresse.toUpperCase()) )
                                .dateEffet(DATE_OF_30_08_2019)
                                .build();
    }

    private Contrat createContratForTest(Abonne abonne){
        return new Contrat("Documentaire illimité",
                            DATE_OF_30_08_2019,
                            LocalDate.of(2020, Month.AUGUST, 30),
                            createDummyBytes(),
                            abonne);
    }

    private byte[] createDummyBytes(){
        byte[] dummyByte = new byte[20];
        new Random().nextBytes(dummyByte);
        return dummyByte;
    }


    @Lorsque("^le conseiller connecté à (\\w+) modifie l'adresse de l'abonné (.+)$")
    public void conseiller_modifie_adresse_abonne(String canal, String condition){
        this.canal = canal;

        nouvelleAdresse = abonne.getAdresses().get(0).toBuilder().build();
        nouvelleAdresse.setCodePostal("77000");
        nouvelleAdresse.setRue("Rue la fontaine");
        nouvelleAdresse = setDateEffetForAdresse(condition);

        testRestTemplate.put( "/" + canal + PUT_GET_ADRESSE_URL, nouvelleAdresse, adresseActuelle.getId() );
    }

    private Adresse setDateEffetForAdresse(String condition) {
        if ( "sans date d’effet".equals(condition.trim()) ) {
            nouvelleAdresse.setDateEffet(null);
        } else if ( "avec date d’effet".equals(condition.trim()) ){
            nouvelleAdresse.setDateEffet( LocalDate.of(2022, Month.APRIL, 5) );
        }

        return nouvelleAdresse;
    }

    @Alors("^l'adresse de l'abonné modifiée est enregistrée sur l'ensemble des contrats de l'abonné$")
    public void adresse_abonne_enregistre(){
        entityManager.clear();
        Abonne abonne = abonneRepository.findById( this.abonne.getId() ) //
                                        .orElseThrow( () -> new IllegalArgumentException("Abonné non trouvé dans la base de données!") );
        assertThat( abonne.getContrats().stream().map(Contrat::getAdresse).allMatch(this.nouvelleAdresse::equals) ).isTrue();

    }

    @Et("^un mouvement de modification d'adresse est créé")
    public void mouvement_modification_cree(){
        ResponseEntity<Evenement> responseEntity = testRestTemplate.getForEntity("/" + this.canal + GET_EVENEMENT_URL, Evenement.class);
        assertThat( responseEntity.getBody().getAncienneAdresse() ).isEqualTo(adresseActuelle);
        assertThat( responseEntity.getBody().getNouvelleAdresse() ).isEqualTo(nouvelleAdresse);
    }

}
