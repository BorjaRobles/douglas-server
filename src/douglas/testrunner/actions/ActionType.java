package douglas.testrunner.actions;

import douglas.testrunner.ElementLocaterEngine;
import douglas.testrunner.StepException;
import douglas.testrunner.TestStep;
import org.json.simple.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ActionType extends AbstractAction {

    @Override
    public JSONObject execute(WebDriver driver, JSONObject step) throws StepException {
        super.execute(driver, step);

        TestStep testStep = new ElementLocaterEngine().find(driver, step);

        String value = (String)step.get("value");

        testStep.getElement().sendKeys(value);

        return testStep.getStep();
    }
}
