package douglas.testrunner;

import douglas.domain.TestStep;
import douglas.testrunner.actions.*;
import org.openqa.selenium.WebDriver;

public class ActionDispatcher {

    public static TestStep dispatch(WebDriver driver, TestStep step) throws TestException {

        TestStep stepResult = new TestStep();
        Action action = null;

        // Switch between the different actions by using Java reflection
        try {
            action = (Action)Class.forName("douglas.testrunner.actions." + step.getAction()).newInstance();
        } catch(ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("We should never reach this - No type specified for step: " + step.toString());
        }

        try {
            stepResult = action.execute(driver, step);

            // In case the step hasn't been deemed unstable by the element locater we set it as passed
            if(!TestStep.Status.Unstable.equals(stepResult.getTestStepStatus())) {
                stepResult.setTestStepStatus(TestStep.Status.Passed);
            }

        // If a step fails, we fail the test by throwing the TestException
        } catch(StepException e) {
            step.setTestStepStatus(TestStep.Status.Failed);

            throw new TestException(step);
        }

        return stepResult;
    }
}