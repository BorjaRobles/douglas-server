package douglas.testrunner;

import douglas.domain.Test;
import douglas.domain.TestResult;
import douglas.domain.TestStep;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class TestRunner {

    final static Logger logger = Logger.getLogger(TestRunner.class);

    public TestRunner() {}

    public static TestResult run(Test test) {

        List<TestStep> steps = test.getTestSteps();


        TestResult result = new TestResult();
        WebDriver driver = new ChromeDriver();

        List<TestStep> resultingSteps = new ArrayList<>();
        boolean unstable = false;

        try {
            driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);

            try {
                // Iterate over steps in test
                for(TestStep step : steps) {
                    TestStep resultingStep = ActionDispatcher.dispatch(driver, step);

                    // In case one of the steps in unstable, mark it as such (unless it of course fails at a later point)
                    if(TestStep.TestStepStatus.Unstable.name().equals(resultingStep.getTestStepStatus())) {
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
            logger.error("DOUGLAS ERROR - UNHANDLED EXCEPTION - ", e);
        } finally {
            driver.quit();
        }


        // Setting Id and reference to parent to NULL in order to create new entities
        for(TestStep testStep : resultingSteps) {
            testStep.setId(null);
            testStep.setParent(null);
        }

        // VIRKER DET HER OVENFOR??????????

        result.setTestSteps(resultingSteps);
        result.setTest(test.getId());
        result.setName(test.getName());
        result.setDescription(test.getDescription());

        return result;
    }
}