package fr.agilit.contrat;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class IntegrationTestSetup {

//    @ClassRule
//    public static final SpringClassRule SPRING_CLASS_RULE = new SpringClassRule();
//
//    @Rule
//    public final SpringMethodRule springMethodRule = new SpringMethodRule();
}
