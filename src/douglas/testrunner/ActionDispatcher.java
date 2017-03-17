package douglas.testrunner;

import douglas.testrunner.actions.*;
import org.json.simple.JSONObject;
import org.openqa.selenium.WebDriver;

public class ActionDispatcher {

    public enum TestResultStepStatus {
        Passed, Unstable, Failed
    }

    public static JSONObject dispatch(WebDriver driver, JSONObject step) throws TestException {

        String action = (String)step.get("action");

        JSONObject stepResult = new JSONObject();

        // Switch between the different actions
        try {
            switch (action) {
                case "url":
                    stepResult = new ActionUrl().execute(driver, step);
                    break;
                case "click":
                    stepResult = new ActionClick().execute(driver, step);
                    break;
                case "type":
                    stepResult = new ActionType().execute(driver, step);
                    break;
                case "assertContainsText":
                    stepResult = new ActionAssertContainsText().execute(driver, step);
                    break;
                default:
                    throw new RuntimeException("We should never reach this - No type specified for step: " + step.toJSONString());
            }

            // In case the step hasn't been deemed unstable by the element locater we set it as passed
            String stepStatus = (String)stepResult.get("status");
            if(!TestResultStepStatus.Unstable.name().toLowerCase().equals(stepStatus)) {
                stepResult.put("status", TestResultStepStatus.Passed.name().toLowerCase());
            }

        // If a step fails, we fail the test by throwing the TestException
        } catch(StepException e) {
            step.put("status", TestResultStepStatus.Failed.name().toLowerCase());

            throw new TestException(step);
        }

        return stepResult;
    }
}