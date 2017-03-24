package douglas.testrunner.actions;

import douglas.testrunner.ElementLocaterEngine;
import douglas.testrunner.StepException;
import douglas.testrunner.LocatedElement;
import org.json.simple.JSONObject;
import org.openqa.selenium.WebDriver;

public class ActionAssertContainsText implements Action {

    @Override
    public JSONObject execute(WebDriver driver, JSONObject step) throws StepException {
        LocatedElement locatedElement = new ElementLocaterEngine().find(driver, step);

        String value = (String)step.get("value");

        String elementText = locatedElement.getElement().getText();
        if(!value.equals(elementText)) {
            throw new StepException(locatedElement.getStep());
        }

        return locatedElement.getStep();
    }
}
