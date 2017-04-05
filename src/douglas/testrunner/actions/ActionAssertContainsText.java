package douglas.testrunner.actions;

import douglas.domain.TestStep;
import douglas.testrunner.ElementLocaterEngine;
import douglas.testrunner.LocatedElement;
import douglas.testrunner.StepException;
import org.json.simple.JSONObject;
import org.openqa.selenium.WebDriver;

public class ActionAssertContainsText implements Action {

    @Override
    public TestStep execute(WebDriver driver, TestStep step) throws StepException {
        LocatedElement locatedElement = new ElementLocaterEngine().find(driver, step);

        String elementText = locatedElement.getElement().getText();
        if(!step.getValue().equals(elementText)) {
            throw new StepException(locatedElement.getStep());
        }

        return locatedElement.getStep();
    }
}
