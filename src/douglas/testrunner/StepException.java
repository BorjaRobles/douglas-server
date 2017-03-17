package douglas.testrunner;

import org.json.simple.JSONObject;

public class StepException extends Throwable {
    private JSONObject failedStep = new JSONObject();

    public StepException(String message) {
        super(message);
    }

    public StepException(JSONObject failedStep) {
        super();
        this.failedStep = failedStep;
    }

    public JSONObject getFailedStep() {
        return this.failedStep;
    }
}
