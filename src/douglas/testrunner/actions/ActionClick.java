package douglas.testrunner.actions;

import douglas.testrunner.ElementLocaterEngine;
import douglas.testrunner.StepException;
import douglas.testrunner.TestStep;
import org.json.simple.JSONObject;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;

public class ActionClick extends AbstractAction {

    @Override
    public JSONObject execute(WebDriver driver, JSONObject step) throws StepException {
        super.execute(driver, step);

        TestStep testStep = new ElementLocaterEngine().find(driver, step);

        try {
            testStep.getElement().click();
        } catch(WebDriverException e) {
            throw new StepException(step);
        }

        return testStep.getStep();
    }
}
