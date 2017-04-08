package douglas.testrunner;

import douglas.domain.TestStep;

public class TestException extends Throwable {
    private TestStep failedStep = new TestStep();

    public TestException(String message) {
        super(message);
    }

    public TestException(TestStep failedStep) {
        super();
        this.failedStep = failedStep;
    }

    public TestStep getFailedStep() {
        return this.failedStep;
    }
}
