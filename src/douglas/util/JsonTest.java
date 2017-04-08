package douglas.util;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

// Used for parsing the received JSON into a Test.class when creating new tests
public class JsonTest {

    public String name;
    public String steps;
    public Long section;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSteps() {
        return steps;
    }

    public void setSteps(String steps) throws UnsupportedEncodingException {
        this.steps = new String(Base64.getDecoder().decode(steps), "UTF-8");
    }

    public Long getSection() {
        return section;
    }

    public void setSection(Long section) {
        this.section = section;
    }
}
