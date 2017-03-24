package unit.domain;

import douglas.domain.TestResult;
import junit.framework.Assert;

public class TestResultTest {

    @org.junit.Test
    public void test() {
        TestResult testResult = new TestResult();
        testResult.setSteps("[{\"hej\":\"davs\"},{\"hej{\":\"davs\"}]");
        testResult.setId((long) 123);
        testResult.setTest((long) 1);
        testResult.setTestResultStatus(TestResult.TestResultStatus.Failed);
        testResult.setName("Davs");
        testResult.setDescription("Davsdavs");

        Assert.assertEquals(testResult.getSteps(), "[{\"hej\":\"davs\"},{\"hej{\":\"davs\"}]");
        Assert.assertEquals((long)testResult.getId(), 123);
        Assert.assertEquals((long)testResult.getTest(), 1);
        Assert.assertEquals(testResult.getTestResultStatus(), TestResult.TestResultStatus.Failed);
        Assert.assertEquals(testResult.getName(), "Davs");
        Assert.assertEquals(testResult.getDescription(), "Davsdavs");
    }
}
