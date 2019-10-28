package fr.agilit.contrat;

import cucumber.api.CucumberOptions;
import net.serenitybdd.cucumber.CucumberWithSerenity;
import org.junit.runner.RunWith;


@RunWith(CucumberWithSerenity.class)
@CucumberOptions(features = "src/test/resources/features", glue = "fr.agilit.contrat.modification", plugin = {"pretty", "html:target/cucumber", "junit:target/junit-report.xml"})
public class CucumberBDDTest {
    // Rien à faire pour le moment
}
