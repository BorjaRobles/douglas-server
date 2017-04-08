package unit.domain;

import douglas.domain.Test;
import junit.framework.Assert;

public class TestTest {

    @org.junit.Test
    public void test() {
        Test test = new Test();
        test.setId((long) 123);
        test.setSection((long) 1);
        test.setTestStatus(Test.Status.Failed);
        test.setName("Davs");
        test.setDescription("Davsdavs");

        Assert.assertEquals((long)test.getId(), 123);
        Assert.assertEquals((long)test.getSection(), 1);
        Assert.assertEquals(test.getTestStatus(), Test.Status.Failed);
        Assert.assertEquals(test.getName(), "Davs");
        Assert.assertEquals(test.getDescription(), "Davsdavs");
    }
}
