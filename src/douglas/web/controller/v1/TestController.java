package douglas.web.controller.v1;

import douglas.domain.Test;
import douglas.domain.TestStep;
import douglas.persistence.TestDao;
import douglas.util.JsonTest;
import douglas.util.StepParser;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
    public void createTest(@RequestBody JsonTest jsonTest) {
        Test test = new Test(jsonTest.getName(), jsonTest.getSection());
        // Parsing the JSON from the recorder
        JSONArray steps = StepParser.parse(jsonTest.getSteps());

        testDao.save(test);
        List<TestStep> stepList = new ArrayList<>();
        // Creating new TestStep entities
        for(Object objStep : steps) {
            JSONObject step = (JSONObject) objStep;
            TestStep testStep = new TestStep(
                    test.getId(),
                    (String)step.get("action"),
                    (String)step.get("path"),
                    (String)step.get("value"),
                    (Long)step.get("metaLocationX"),
                    (Long)step.get("metaLocationY"),
                    (String)step.get("metaContent")
            );
            stepList.add(testStep);
        }

        test.setTestSteps(stepList);
        // Hibernate can't automatically set up the reference between Test and TestStep as TestStep entities
        // also can reference a TestResult
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