package douglas.testrunner;

import douglas.domain.Test;
import douglas.domain.TestResult;
import douglas.util.StepParser;
import org.apache.commons.lang3.StringEscapeUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.openqa.selenium.*;

public class MetaHandler {

    public JSONObject fetch(WebDriver driver, JSONObject step, String newCssSelector) {
        JSONObject resultingMetaObj = new JSONObject();
        String selector = (newCssSelector == null) ? (String)step.get("path") : newCssSelector;
        WebElement element = driver.findElement(By.cssSelector(selector));

        // Determining the center of the element
        JSONObject location = new JSONObject();
        Point elmLocation = element.getLocation();
        Dimension elmSize = element.getSize();
        float x = elmLocation.getX() + (elmSize.getWidth() / 2);
        float y = elmLocation.getY() + (elmSize.getHeight() / 2);
        location.put("x", Math.round(x));
        location.put("y", Math.round(y));
        resultingMetaObj.put("location", location);

        // Click elements can also have content
        String action = (String)step.get("action");
        if("click".equals(action)) {
            resultingMetaObj.put("content", element.getText());
        }

        return resultingMetaObj;
    }

    // Method that takes the updated meta information from the testresult and transfers it to the actual test
    // This is done to make sure we always have the most up-to-date meta information on the elements
    public Test transferMeta(Test test, TestResult testResult) {
        JSONArray testResultSteps = StepParser.parse(testResult.getSteps());
        JSONArray testSteps = StepParser.parse(test.getSteps());

        JSONArray updatedTestSteps = new JSONArray();

        for (int i = 0; i < testSteps.size(); i++) {
            JSONObject step = (JSONObject)testSteps.get(i);
            JSONObject testResultStep = (JSONObject)testResultSteps.get(i);
            JSONObject updatedMeta = (JSONObject)testResultStep.get("meta");

            step.put("meta", updatedMeta);

            updatedTestSteps.add(step);
        }

        String unescapedJson = StringEscapeUtils.unescapeJson(updatedTestSteps.toJSONString());
        test.setDecodedSteps(unescapedJson);

        return test;
    }
}
