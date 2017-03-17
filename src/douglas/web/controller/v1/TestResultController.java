package douglas.web.controller.v1;

import douglas.domain.Test;
import douglas.domain.TestResult;
import douglas.persistence.TestDao;
import douglas.persistence.TestResultDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/rest/v1/testresults")
public class TestResultController {

    private TestResultDao testResultDao;

    @Autowired
    public TestResultController(TestResultDao testResultDao) {
        this.testResultDao = testResultDao;
    }

    @Transactional
    @RequestMapping(path = {"/{testResultId}"}, method = RequestMethod.GET)
    public TestResult findTestResult(@PathVariable String testResultId) {
        return testResultDao.findById(testResultId);
    }

    @Transactional
    @RequestMapping(path = {"", "/"}, method = RequestMethod.GET)
    public List<TestResult> all() {
        return testResultDao.all();
    }

    @Transactional
    @RequestMapping(path = {"", "/"}, method = RequestMethod.POST)
    public void createTestResult(@RequestBody TestResult testResult) {
        testResultDao.save(testResult);
    }

    @Transactional
    @RequestMapping(path = {"", "/"}, method = RequestMethod.PUT)
    public void updateTestResult(@RequestBody TestResult testResult) {
        testResultDao.update(testResult);
    }

    @Transactional
    @RequestMapping(path = {"", "/"}, method = RequestMethod.DELETE)
    public void deleteTestResult(@RequestBody TestResult testResult) {
        testResultDao.delete(testResult);
    }

}
