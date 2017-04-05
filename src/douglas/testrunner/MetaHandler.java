package douglas.testrunner;

import douglas.domain.Test;
import douglas.domain.TestResult;
import douglas.domain.TestStep;
import douglas.util.StepParser;
import org.apache.commons.lang3.StringEscapeUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.openqa.selenium.*;

import java.util.ArrayList;
import java.util.List;

public class MetaHandler {

    public Meta fetch(WebDriver driver, TestStep step, String newCssSelector) {
        Meta meta = new Meta();
        String selector = (newCssSelector == null) ? step.getPath() : newCssSelector;
        WebElement element = driver.findElement(By.cssSelector(selector));

        // Determining the center of the element
        Point elmLocation = element.getLocation();
        Dimension elmSize = element.getSize();
        float x = elmLocation.getX() + (elmSize.getWidth() / 2);
        float y = elmLocation.getY() + (elmSize.getHeight() / 2);
        meta.setLocationX(Long.valueOf(Math.round(x)));
        meta.setLocationY(Long.valueOf(Math.round(y)));

        // Click elements can also have content
        if("ActionClick".equals(step.getAction())) {
            meta.setContent(element.getText());
        }

        return meta;
    }

    // Method that takes the updated meta information from the testresult and transfers it to the actual test
    // This is done to make sure we always have the most up-to-date meta information on the elements
    public Test transferMeta(Test test, TestResult testResult) {
        List<TestStep> testResultSteps = testResult.getTestSteps();
        List<TestStep> testSteps = test.getTestSteps();

        List<TestStep> updatedTestSteps = new ArrayList<>();

        for (int i = 0; i < testSteps.size(); i++) {
            TestStep step = testSteps.get(i);
            TestStep testResultStep = testResultSteps.get(i);
            step.setMetaLocationX(testResultStep.getMetaLocationX());
            step.setMetaLocationY(testResultStep.getMetaLocationY());
            step.setMetaContent(testResultStep.getMetaContent());

            updatedTestSteps.add(step);
        }

        test.setTestSteps(updatedTestSteps);
        return test;
    }
}
