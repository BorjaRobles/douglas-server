package unit.util;

import douglas.util.StepParser;
import org.json.simple.JSONArray;
import org.junit.Assert;
import org.junit.Test;

public class StepParserTest {

    @Test
    public void parsesStringToJsonArray() {
        String testString = "[{\"hej\":\"davs\"},{\"hej{\":\"davs\"}]";
        JSONArray array = StepParser.parse(testString);
        Assert.assertTrue(array.size() == 2);
    }

}
