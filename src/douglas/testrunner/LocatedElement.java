package douglas.testrunner;

import douglas.domain.TestStep;
import org.json.simple.JSONObject;
import org.openqa.selenium.WebElement;

public class LocatedElement {

    // Used by testrunner to pass around both the TestStep and the Selenium WebDriver element at the same time
    private TestStep step;
    private WebElement element;

    public LocatedElement(TestStep step, WebElement element) {
        this.step = step;
        this.element = element;
    }

    public TestStep getStep() {
        return step;
    }

    public void setStep(TestStep step) {
        this.step = step;
    }

    public WebElement getElement() {
        return element;
    }

    public void setElement(WebElement element) {
        this.element = element;
    }
}
