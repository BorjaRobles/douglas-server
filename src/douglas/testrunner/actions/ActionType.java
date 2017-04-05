package douglas.testrunner.actions;

import douglas.domain.TestStep;
import douglas.testrunner.ElementLocaterEngine;
import douglas.testrunner.LocatedElement;
import douglas.testrunner.StepException;
import org.json.simple.JSONObject;
import org.openqa.selenium.WebDriver;

public class ActionType implements Action {

    @Override
    public TestStep execute(WebDriver driver, TestStep step) throws StepException {
        LocatedElement locatedElement = new ElementLocaterEngine().find(driver, step);

        locatedElement.getElement().sendKeys(step.getValue());

        return locatedElement.getStep();
    }
}
