package douglas.testrunner.actions;

import douglas.domain.TestStep;
import douglas.testrunner.StepException;
import org.json.simple.JSONObject;
import org.openqa.selenium.WebDriver;

public interface Action {

    TestStep execute(WebDriver driver, TestStep step) throws StepException;
}
