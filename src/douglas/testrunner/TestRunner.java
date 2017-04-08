package douglas.testrunner;

import douglas.domain.Test;
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

    public static Test run(Test test) {

        List<TestStep> steps = test.getTestSteps();

        WebDriver driver = new ChromeDriver();

        List<TestStep> resultingSteps = new ArrayList<>();
        boolean unstable = false;

        try {
            driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);

            try {
                // Iterate over steps in test
                for(TestStep step : steps) {
                    TestStep resultingStep = ActionDispatcher.dispatch(driver, step);

                    // In case one of the steps is unstable, remember it until
                    // the other steps are done
                    if(TestStep.Status.Unstable.equals(resultingStep.getTestStepStatus())) {
                        unstable = true;
                    }

                    resultingSteps.add(resultingStep);
                }

                // If one of the steps is unstable, mark the whole test
                if(unstable) {
                    test.setTestStatus(Test.Status.Unstable);
                } else {
                    test.setTestStatus(Test.Status.Passed);
                }

            // If we catch the TestException, the test have failed
            } catch(TestException exception) {
                resultingSteps.add(exception.getFailedStep());

                // Iterating over the last remaining steps and adding them to the resulting steps-array
                int numberOfProcessedSteps = resultingSteps.size();
                for(int i = numberOfProcessedSteps; i < steps.size(); i++) {
                    resultingSteps.add(steps.get(i));
                }
                test.setTestStatus(Test.Status.Failed);
            }

            // Catching all exceptions in order to clean up Selenium resources in unhandled cases
            // This is necessary as some rare cases can cause Chrome instances to hang and accumulate
        } catch(Exception e) {
            logger.error("DOUGLAS ERROR - UNHANDLED EXCEPTION - ", e);
        } finally {
            driver.quit();
        }

        test.setTestSteps(resultingSteps);

        return test;
    }
}