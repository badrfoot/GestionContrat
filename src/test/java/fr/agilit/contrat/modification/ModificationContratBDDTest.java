package fr.agilit.contrat.modification;

import fr.agilit.contrat.IntegrationTestSetup;
import fr.agilit.contrat.UtilisateurRepository;
import fr.agilit.contrat.entities.*;
import cucumber.api.java.Before;
import cucumber.api.java.fr.Alors;
import cucumber.api.java.fr.Et;
import cucumber.api.java.fr.Etantdonné;
import cucumber.api.java.fr.Lorsque;
import org.assertj.core.util.Sets;
import org.junit.Assume;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.Stream;

import static fr.agilit.contrat.modification.AbonneController.*;
import static fr.agilit.contrat.modification.EvenementController.GET_EVENEMENTS_URL;
import static fr.agilit.contrat.modification.EvenementController.ID_ABONNE_REQUEST_PARAM;
import static org.assertj.core.api.Assertions.assertThat;

public class ModificationContratBDDTest extends IntegrationTestSetup {

    private static final LocalDate DATE_OF_30_08_2019 =  LocalDate.of(2019, Month.AUGUST, 30);
    private static final String PUT_ADRESSE_URL_WITH_PARAMS = PUT_GET_ADRESSE_URL + "?" + ID_UTILISATEUR_REQUEST_PARAM  + "={idUtilisateur}";
    private static final String GET_ABONNE_URL_BY_ADRESSE_ETAT_PAYS = GET_ABONNE_URL + "?" + ETATADRESSE_REQUEST_PARAM + "={etatAdresse}&" + PAYS_REQUEST_PARAM + "={pays}";
    private static final String GET_EVENEMENT_BY_IDABONNE = GET_EVENEMENTS_URL + "?" + ID_ABONNE_REQUEST_PARAM  + "={idAbonne}";

    private Abonne abonneBeforeChange;
    private Adresse nouvelleAdresse;
    private Adresse adresseTochange;
    private String canal;
    private Utilisateur utilisateur;

    @Autowired private TestRestTemplate testRestTemplate;
    @Autowired private AbonneRepository abonneRepository;
    @Autowired private UtilisateurRepository utilisateurRepository;


    @Before
    public void createAbonnesWithAdresse(){
        utilisateur = createAndSaveUtilisateur();

        createAndSaveAbonneForTest( createAdresseForTest(EtatAdresse.INACTIVE, "France") );
        createAndSaveAbonneForTest( createAdresseForTest(EtatAdresse.ACTIVE, "Pologne") );
    }

    private Utilisateur createAndSaveUtilisateur(){
        return  utilisateurRepository.save( new Utilisateur("myLoing", "******") );
    }

    private Abonne createAndSaveAbonneForTest(Adresse adresse){
        Abonne abonne = new Abonne( "Yosra", "CHAABANE", Sets.newHashSet(Arrays.asList(adresse)) );
        abonne.addContrat( createContratForTest(abonne) );
        abonneRepository.save(abonne);

        return abonne;
    }

    private Adresse createAdresseForTest(EtatAdresse etatAdresse, String pays){
        return Adresse.builder().numero(12) //
                                .rue("rue Neige Blanche") //
                                .codePostal("75000") //
                                .ville("Paris") //
                                .pays(pays) //
                                .typeAdresse(TypeAdresse.PRINCIPALE) //
                                .etatAdresse(etatAdresse) //
                                .dateEffet(DATE_OF_30_08_2019) //
                                .build();
    }

    private Contrat createContratForTest(Abonne abonne){
        return new Contrat("Documentaire illimité", //
                            DATE_OF_30_08_2019, //
                            LocalDate.of(2020, Month.AUGUST, 30), //
                            false, //
                            createDummyBytes(), //
                            abonne);
    }

    private byte[] createDummyBytes(){
        byte[] dummyByte = new byte[20];
        new Random().nextBytes(dummyByte);
        return dummyByte;
    }

    @Etantdonné("^un abonné avec une adresse principale (\\w+) en (\\w+)$")
    public void abonne_avec_adresse(String etatAdresse, String pays){
        Abonne[] abonnes = testRestTemplate.getForEntity(GET_ABONNE_URL_BY_ADRESSE_ETAT_PAYS, Abonne[].class, etatAdresse.toUpperCase(), pays).getBody();
        Assume.assumeTrue("Aucun abonné n'a été trouvé ayant une adresse [EtatAdresse = " +  etatAdresse + "] et [Pays = " +  pays + "]", abonnes.length > 0);

        abonneBeforeChange = abonnes[0];
        adresseTochange = abonneBeforeChange.getAdresses().iterator().next();
    }

    @Lorsque("^le conseiller connecté à (\\w+) modifie l'adresse de l'abonné (.+)$")
    public void conseiller_modifie_adresse_abonne(String canal, String condition){
        this.canal = canal;

        nouvelleAdresse = abonneBeforeChange.getAdresseById(adresseTochange.getId()).get().toBuilder().build();
        nouvelleAdresse.setCodePostal("77000");
        nouvelleAdresse.setRue("Rue la fontaine");
        nouvelleAdresse.setEtatAdresse(EtatAdresse.ACTIVE);
        nouvelleAdresse = setDateEffetForAdresse(condition);

        HttpEntity<Adresse> requestBody = new HttpEntity<>(nouvelleAdresse);

        testRestTemplate.exchange( "/" + canal + PUT_ADRESSE_URL_WITH_PARAMS, HttpMethod.PUT, requestBody, Void.class, abonneBeforeChange.getId(), adresseTochange.getId(), utilisateur.getId() );
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
        ResponseEntity<Contrat[]> responseEntity = testRestTemplate.getForEntity("/" + this.canal + GET_CONTRATS_URL, Contrat[].class, abonneBeforeChange.getId() );
        Contrat[] contrats = responseEntity.getBody();

        assertThat( responseEntity.getStatusCode() ).isEqualTo(HttpStatus.OK);
        assertThat( Stream.of(contrats).map(Contrat::getAdresse).allMatch(this.nouvelleAdresse::equals) ).isTrue();
    }

    @Et("^un mouvement de modification d'adresse est créé")
    public void mouvement_modification_cree(){
        ResponseEntity<Evenement[]> responseEntity = testRestTemplate.getForEntity( "/" + this.canal + GET_EVENEMENT_BY_IDABONNE, Evenement[].class, abonneBeforeChange.getId() );
        Evenement createdEvenement = responseEntity.getBody()[0];

        assertThat( createdEvenement.getAbonne() .getId() ).isEqualTo( abonneBeforeChange.getId() );
        assertThat( createdEvenement.getNouvelleAdresse() ).isEqualTo(nouvelleAdresse);
    }

}
