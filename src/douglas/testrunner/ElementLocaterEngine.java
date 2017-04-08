package douglas.testrunner;

import douglas.domain.TestStep;
import douglas.util.GenerateNewCSSSelector;
import org.apache.commons.lang3.StringEscapeUtils;
import org.json.simple.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ElementLocaterEngine {

    public LocatedElement createSuggestion(WebDriver driver, TestStep step, String newCssSelector) {
        JSONObject suggestion = new JSONObject();
        Meta meta = new MetaHandler().fetch(driver, step, newCssSelector);
        suggestion.put("path", newCssSelector);
        suggestion.put("metaLocationX", meta.getLocationX());
        suggestion.put("metaLocationY", meta.getLocationY());
        suggestion.put("metaContent", meta.getContent());

        String unescapedSuggestion = StringEscapeUtils.unescapeJson(suggestion.toJSONString());
        step.setSuggestion(unescapedSuggestion);
        step.setTestStepStatus(TestStep.Status.Unstable);

        WebElement newElement = driver.findElement(By.cssSelector(newCssSelector));

        return new LocatedElement(step, newElement);
    }

    public LocatedElement find(WebDriver driver, TestStep step) throws StepException {
        String selector = step.getPath();

        try {
            // Wait maximum 10 seconds for the element to appear
            WebDriverWait wait = new WebDriverWait(driver, 10);
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(selector)));

            // We don't use meta information on URL steps
            if(!"ActionUrl".equals(step.getAction())) {
                Meta meta = new MetaHandler().fetch(driver, step, null);
                step.setMeta(meta.getLocationX(), meta.getLocationY(), meta.getContent());
            }

            return new LocatedElement(step, element);
        } catch(WebDriverException e) {

            // We try to locate the element using various meta data

            // Try using the content of the element
            try {
                String newCssSelector = new GenerateNewCSSSelector().generateFromContent(driver, step.getMetaContent());

                return this.createSuggestion(driver, step, newCssSelector);
            } catch (WebDriverException wde) {}

            // Try using the location of the element
            try {
                String newCssSelector = new GenerateNewCSSSelector().generateFromLocation(driver, step.getMetaLocationX(), step.getMetaLocationY());

                return this.createSuggestion(driver, step, newCssSelector);
            } catch (WebDriverException wde) {}

            throw new StepException(step);
        }

    }
}