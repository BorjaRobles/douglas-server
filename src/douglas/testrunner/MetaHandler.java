package douglas.testrunner;

import douglas.domain.TestStep;
import org.openqa.selenium.*;


public class MetaHandler {

    public Meta fetch(WebDriver driver, TestStep step, String newCssSelector) {
        Meta meta = new Meta();

        // Fetch metadata from an existing CSS selector or from a new CSS selector just generated
        // by the ElementLocaterEngine
        String selector = (newCssSelector == null) ? step.getPath() : newCssSelector;
        WebElement element = driver.findElement(By.cssSelector(selector));

        // Determining the center of the element
        Point elmLocation = element.getLocation();
        Dimension elmSize = element.getSize();
        float x = elmLocation.getX() + (elmSize.getWidth() / 2);
        float y = elmLocation.getY() + (elmSize.getHeight() / 2);
        meta.setLocationX(Long.valueOf(Math.round(x)));
        meta.setLocationY(Long.valueOf(Math.round(y)));

        // Click-elements can also have content
        if("ActionClick".equals(step.getAction())) {
            meta.setContent(element.getText());
        }
        
        return meta;
    }

}
