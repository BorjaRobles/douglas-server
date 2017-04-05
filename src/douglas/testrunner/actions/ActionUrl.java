package douglas.testrunner.actions;

import douglas.domain.TestStep;
import douglas.testrunner.StepException;
import org.json.simple.JSONObject;
import org.openqa.selenium.WebDriver;

public class ActionUrl implements Action {

    @Override
    public TestStep execute(WebDriver driver, TestStep step) throws StepException {
        driver.navigate().to(step.getValue());

        return step;
    }
}
