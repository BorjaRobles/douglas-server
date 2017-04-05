package unit.testrunner;

import douglas.domain.Test;
import douglas.domain.TestResult;
import douglas.testrunner.MetaHandler;
import douglas.util.StepParser;
import junit.framework.Assert;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class MetaHandlerTest {

    /*@org.junit.Test
    public void transfersMetaData() {
        Test test = new Test();
        String decodedSteps = "[\n" +
                "    {\n" +
                "        \"action\": \"click\",\n" +
                "        \"meta\": {\n" +
                "            \"content\": \"davs\"\n" +
                "        }\n" +
                "    },\n" +
                "    {\n" +
                "        \"action\": \"click\",\n" +
                "        \"meta\": {\n" +
                "            \"content\": \"davs2\"\n" +
                "        }\n" +
                "    }\n" +
                "]";
        test.setDecodedSteps(decodedSteps);
        TestResult testResult = new TestResult();
        String decodedSteps2 = "[\n" +
                "    {\n" +
                "        \"action\": \"click\",\n" +
                "        \"meta\": {\n" +
                "            \"content\": \"hejsa\"\n" +
                "        }\n" +
                "    },\n" +
                "    {\n" +
                "        \"action\": \"click\",\n" +
                "        \"meta\": {\n" +
                "            \"content\": \"hejsa2\"\n" +
                "        }\n" +
                "    }\n" +
                "]";
        testResult.setSteps(decodedSteps2);
        Test test2 = new MetaHandler().transferMeta(test, testResult);
        JSONArray steps = StepParser.parse(test2.getSteps());
        JSONObject step1 = (JSONObject)steps.get(0);
        JSONObject meta1 = (JSONObject)step1.get("meta");
        String content = (String)meta1.get("content");
        Assert.assertTrue("hejsa".equals(content));

        JSONObject step2 = (JSONObject)steps.get(1);
        JSONObject meta2 = (JSONObject)step2.get("meta");
        String content2 = (String)meta2.get("content");
        Assert.assertTrue("hejsa2".equals(content2));
    }*/
}