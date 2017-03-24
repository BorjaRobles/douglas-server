package unit.domain;

import douglas.domain.Test;
import junit.framework.Assert;

public class TestTest {

    @org.junit.Test
    public void test() {
        Test test = new Test();
        test.setDecodedSteps("[{\"hej\":\"davs\"},{\"hej{\":\"davs\"}]");
        test.setId((long) 123);
        test.setSection((long) 1);
        test.setTestStatus(Test.TestStatus.Failed);
        test.setName("Davs");
        test.setDescription("Davsdavs");

        Assert.assertEquals(test.getSteps(), "[{\"hej\":\"davs\"},{\"hej{\":\"davs\"}]");
        Assert.assertEquals((long)test.getId(), 123);
        Assert.assertEquals((long)test.getSection(), 1);
        Assert.assertEquals(test.getTestStatus(), Test.TestStatus.Failed);
        Assert.assertEquals(test.getName(), "Davs");
        Assert.assertEquals(test.getDescription(), "Davsdavs");
    }
}
