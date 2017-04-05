package douglas.web.controller.v1;

import douglas.domain.Test;
import douglas.persistence.TestDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/rest/v1/tests")
public class TestController {

    private TestDao testDao;

    @Autowired
    public TestController(TestDao testDao) {
        this.testDao = testDao;
    }

    @Transactional
    @RequestMapping(path = {"/bySectionId/{sectionId}"}, method = RequestMethod.GET)
    public List<Test> getTestsBySectionId(@PathVariable String sectionId) {
        return testDao.allTestsBySection(sectionId);
    }

    @Transactional
    @RequestMapping(path = {"/run/{testId}"}, method = RequestMethod.GET)
    public void runTestById(@PathVariable String testId) {
        testDao.run(testId);
    }

    @Transactional
    @RequestMapping(path = {"/{testId}"}, method = RequestMethod.GET)
    public Test findTest(@PathVariable String testId) {
        return testDao.findById(testId);
    }

    @Transactional
    @RequestMapping(path = {"", "/"}, method = RequestMethod.GET)
    public List<Test> all() {
        return testDao.all();
    }

    @Transactional
    @RequestMapping(path = {"", "/"}, method = RequestMethod.POST)
    public void createTest(@RequestBody Test test) {
        testDao.save(test);
    }

    @Transactional
    @RequestMapping(path = {"", "/"}, method = RequestMethod.PUT)
    public void updateTest(@RequestBody Test test) {
        testDao.update(test);
    }

    @Transactional
    @RequestMapping(path = {"", "/"}, method = RequestMethod.DELETE)
    public void deleteTest(@RequestBody Test test) {
        testDao.delete(test);
    }

}
