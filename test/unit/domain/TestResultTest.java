package unit.domain;

import douglas.domain.Test;
import douglas.domain.TestResult;
import junit.framework.Assert;

public class TestResultTest {

    @org.junit.Test
    public void test() {
        TestResult testResult = new TestResult();
        testResult.setId((long) 123);
        testResult.setTest((long) 1);
        testResult.setTestResultStatus(Test.Status.Failed);
        testResult.setName("Davs");
        testResult.setDescription("Davsdavs");

        Assert.assertEquals((long)testResult.getId(), 123);
        Assert.assertEquals((long)testResult.getTest(), 1);
        Assert.assertEquals(testResult.getTestResultStatus(), Test.Status.Failed);
        Assert.assertEquals(testResult.getName(), "Davs");
        Assert.assertEquals(testResult.getDescription(), "Davsdavs");
    }
}
