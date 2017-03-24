package douglas.testrunner;

import org.json.simple.JSONObject;
import org.openqa.selenium.WebElement;

public class LocatedElement {

    // Used by testrunner to pass around both the teststep and the Selenium WebDriver element at the same time
    private JSONObject step;
    private WebElement element;

    public LocatedElement(JSONObject step, WebElement element) {
        this.step = step;
        this.element = element;
    }

    public JSONObject getStep() {
        return step;
    }

    public void setStep(JSONObject step) {
        this.step = step;
    }

    public WebElement getElement() {
        return element;
    }

    public void setElement(WebElement element) {
        this.element = element;
    }
}
