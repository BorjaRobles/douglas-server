package douglas.testrunner;

import org.json.simple.JSONObject;

public class TestException extends Throwable {
    private JSONObject failedStep = new JSONObject();

    public TestException(String message) {
        super(message);
    }

    public TestException(JSONObject failedStep) {
        super();
        this.failedStep = failedStep;
    }

    public JSONObject getFailedStep() {
        return this.failedStep;
    }
}
