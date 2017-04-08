package douglas.testrunner.actions;

import douglas.domain.TestStep;
import douglas.testrunner.ElementLocaterEngine;
import douglas.testrunner.LocatedElement;
import douglas.testrunner.StepException;
import org.json.simple.JSONObject;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;

public class ActionClick implements Action {

    @Override
    public TestStep execute(WebDriver driver, TestStep step) throws StepException {
        LocatedElement locatedElement = new ElementLocaterEngine().find(driver, step);

        try {
            locatedElement.getElement().click();
        } catch(WebDriverException e) {
            throw new StepException(step);
        }

        return locatedElement.getStep();
    }
}
