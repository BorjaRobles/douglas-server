package douglas.testrunner.actions;

import douglas.testrunner.StepException;
import org.json.simple.JSONObject;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;

abstract public class AbstractAction implements Action {

    public JSONObject execute(WebDriver driver, JSONObject step) throws StepException {
        return step;
    }
}
