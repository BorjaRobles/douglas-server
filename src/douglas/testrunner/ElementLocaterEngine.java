package douglas.testrunner;

import douglas.util.GenerateNewCSSSelector;
import org.json.simple.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ElementLocaterEngine {

    public LocatedElement createSuggestion(WebDriver driver, JSONObject step, String newCssSelector) {
        JSONObject suggestion = new JSONObject();
        suggestion.put("path", newCssSelector);
        suggestion.put("meta", new MetaHandler().fetch(driver, step, newCssSelector));

        JSONObject updatedStep = step;
        updatedStep.put("suggestion", suggestion);
        updatedStep.put("status", ActionDispatcher.TestResultStepStatus.Unstable.name().toLowerCase());

        WebElement newElement = driver.findElement(By.cssSelector(newCssSelector));

        return new LocatedElement(updatedStep, newElement);
    }

    public LocatedElement find(WebDriver driver, JSONObject step) throws StepException {
        String selector = (String)step.get("path");

        try {
            // Wait maximum 10 seconds for the element to appear
            WebDriverWait wait = new WebDriverWait(driver, 10);
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(selector)));

            // We don't use meta information on URL steps
            if(!"url".equals(step.get("action"))) {
                step.put("meta", new MetaHandler().fetch(driver, step, null));
            }

            return new LocatedElement(step, element);
        } catch(WebDriverException e) {

            // We try to locate the element using various meta data

            // Try using the content of the element
            try {
                JSONObject metaObj = (JSONObject)step.get("meta");
                String content = (String)metaObj.get("content");
                String newCssSelector = new GenerateNewCSSSelector().generateFromContent(driver, content);

                return this.createSuggestion(driver, step, newCssSelector);
            } catch (WebDriverException wde) {}

            // Try using the location of the element
            try {
                JSONObject metaObj = (JSONObject)step.get("meta");
                JSONObject location = (JSONObject)metaObj.get("location");
                String newCssSelector = new GenerateNewCSSSelector().generateFromLocation(driver, location);

                return this.createSuggestion(driver, step, newCssSelector);
            } catch (WebDriverException wde) {}

            throw new StepException(step);
        }

    }
}
