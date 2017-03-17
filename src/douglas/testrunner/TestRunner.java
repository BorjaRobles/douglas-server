package douglas.testrunner;

import douglas.domain.Test;
import douglas.domain.TestResult;
import douglas.util.StepParser;
import org.apache.commons.lang3.StringEscapeUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.concurrent.TimeUnit;


public class TestRunner {


    public TestRunner() {}

    public static TestResult run(Test test) {

        JSONArray steps = StepParser.parse(test.getSteps());


        TestResult result = new TestResult();
        WebDriver driver = new ChromeDriver();

        JSONArray resultingSteps = new JSONArray();
        boolean unstable = false;

        try {
            driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);

            try {
                // Iterate over steps in test
                for(Object stepObj : steps) {
                    JSONObject step = (JSONObject)stepObj;
                    JSONObject resultingStep = ActionDispatcher.dispatch(driver, step);

                    // In case one of the steps in unstable, mark it as such (unless it of course fails at a later point)
                    if(TestResult.TestResultStatus.Unstable.name().toLowerCase().equals(resultingStep.get("status"))) {
                        unstable = true;
                    }
                    resultingSteps.add(resultingStep);
                }

                // If one of the steps is unstable, mark the whole test
                if(unstable) {
                    result.setTestResultStatus(TestResult.TestResultStatus.Unstable);
                } else {
                    result.setTestResultStatus(TestResult.TestResultStatus.Passed);
                }

            // If we catch the TestException, the test have failed
            } catch(TestException exception) {
                resultingSteps.add(exception.getFailedStep());

                // Iterating over the last remaining steps and adding them to the resulting steps-array
                int numberOfProcessedSteps = resultingSteps.size();
                for(int i = numberOfProcessedSteps; i < steps.size(); i++) {
                    resultingSteps.add(steps.get(i));
                }
                result.setTestResultStatus(TestResult.TestResultStatus.Failed);
            }

            // Catching all exceptions in order to clean up selenium resources in unhandled cases
            // This is necessary as some rare cases can cause Chrome instances to hang and accumulate
        } catch(Exception e) {
            System.out.println("DOUGLAS ERROR - UNHANDLED EXCEPTION");
            e.printStackTrace();
        } finally {
            driver.quit();
        }

        String unescapedJson = StringEscapeUtils.unescapeJson(resultingSteps.toJSONString());
        result.setSteps(unescapedJson);
        result.setTest(test.getId());
        result.setName(test.getName());
        result.setDescription(test.getDescription());

        return result;
    }
}