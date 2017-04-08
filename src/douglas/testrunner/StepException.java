package douglas.testrunner;

import douglas.domain.TestStep;

public class StepException extends Throwable {
    private TestStep failedStep = new TestStep();

    public StepException(String message) {
        super(message);
    }

    public StepException(TestStep failedStep) {
        super();
        this.failedStep = failedStep;
    }

    public TestStep getFailedStep() {
        return this.failedStep;
    }
}
