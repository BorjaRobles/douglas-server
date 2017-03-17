package douglas.testrunner.actions;

import douglas.testrunner.StepException;
import org.json.simple.JSONObject;
import org.openqa.selenium.WebDriver;

public interface Action {

    JSONObject execute(WebDriver driver, JSONObject step) throws StepException;
}
