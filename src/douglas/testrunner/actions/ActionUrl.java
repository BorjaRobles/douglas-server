package douglas.testrunner.actions;

import douglas.testrunner.StepException;
import org.json.simple.JSONObject;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;

public class ActionUrl extends AbstractAction {

    @Override
    public JSONObject execute(WebDriver driver, JSONObject step) throws StepException {
        super.execute(driver, step);

        String url = (String)step.get("value");

        driver.navigate().to(url);

        return step;
    }
}
