package douglas.web.controller.v1;

import douglas.domain.Test;
import douglas.domain.TestResult;
import douglas.domain.TestStep;
import douglas.persistence.TestDao;
import douglas.persistence.TestResultDao;
import douglas.testrunner.ActionDispatcher;
import douglas.util.StepParser;
import org.apache.commons.lang3.StringEscapeUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/*
The SuggestionController isn't tied to a domain file like the other controller files
Instead do it simply have one endpoint, the accept endpoint
*/

@RestController
@RequestMapping(path = "/rest/v1/suggestions")
public class SuggestionController {

    private TestDao testDao;
    private TestResultDao testResultDao;

    @Autowired
    public SuggestionController(TestDao testDao, TestResultDao testResultDao) {
        this.testDao = testDao;
        this.testResultDao = testResultDao;
    }

    // The purpose of this endpoint is to grab the suggested new CSS selector and metadata
    // from the test result and use that information to transition the step from "unstable"
    // to "passed"

    // Takes two parameters, the testresult ID and the index of the step we
    // want to accept the suggestion from
    @Transactional
    @RequestMapping(path = {"/accept/{testResultId}/{index}"}, method = RequestMethod.GET)
    public void acceptSuggestion(@PathVariable String testResultId, @PathVariable String index) {


        // BRUG DA ID FRA ENTITETEN


        int intIndex = Integer.parseInt(index);
        TestResult currentResult = testResultDao.findById(testResultId);
        Test currentTest = testDao.findById(Long.toString(currentResult.getTest()));

        // Move all data from the suggestion-property to the step of the result
        List<TestStep> resultSteps = currentResult.getTestSteps();
        List<TestStep> testSteps = currentTest.getTestSteps();

        TestStep affectedStep = resultSteps.get(intIndex);

        JSONParser parser = new JSONParser();
        JSONObject suggestion = new JSONObject();
        try {
            suggestion = (JSONObject)parser.parse(affectedStep.getSuggestion());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        affectedStep.setTestStepStatus(TestStep.TestStepStatus.Passed);
        affectedStep.setPath((String)suggestion.get("path"));
        affectedStep.setMetaLocationX((Long)suggestion.get("metaLocationX"));
        affectedStep.setMetaLocationY((Long)suggestion.get("metaLocationY"));
        affectedStep.setMetaContent((String)suggestion.get("metaContent"));

        // Try to extract value-property from suggestion object
        String content = (String)suggestion.get("metaContent");
        if(content != null) {
            affectedStep.setValue(content);
        }

        // The property is not longer needed as the user accepted the suggestion
        affectedStep.setSuggestion(null);

        resultSteps.remove(intIndex);
        resultSteps.add(intIndex, affectedStep);
        currentResult.setTestSteps(resultSteps);


        // We have now accepted the suggestion in the test result meaning that the step
        // is now "passed" but it isn't enough that the test result knows the accepted
        // suggestion, we also need to add the new CSS selecter and meta data to the
        // actual test


        // Also apply that updated step to the actual test case

        // Remove unnecessary keys
        TestStep strippedAffectedStep = new TestStep(
                currentTest.getId(),
                affectedStep.getAction(),
                affectedStep.getPath(),
                affectedStep.getValue(),
                affectedStep.getMetaLocationX(),
                affectedStep.getMetaLocationY(),
                affectedStep.getMetaContent());

        testSteps.remove(intIndex);
        testSteps.add(intIndex, strippedAffectedStep);
        currentTest.setTestSteps(testSteps);


        boolean failed = false;
        boolean unstable = false;

        // We iterate over all the steps to check if the whole test/testresult now passes or
        // other unstable/failed steps still exists in the test/testresult
        for(TestStep step : resultSteps) {

            if(TestStep.TestStepStatus.Unstable.name().equals(step.getTestStepStatus())) {
                unstable = true;
            }

            if(TestStep.TestStepStatus.Failed.name().equals(step.getTestStepStatus())) {
                failed = true;
            }
        }

        // Based on the what we just found in the loop above do we set the status of the
        // test and test result
        if(failed) {
            currentTest.setTestStatus(Test.TestStatus.Failed);
            currentResult.setTestResultStatus(TestResult.TestResultStatus.Failed);
        } else if (unstable) {
            currentTest.setTestStatus(Test.TestStatus.Unstable);
            currentResult.setTestResultStatus(TestResult.TestResultStatus.Unstable);
        } else {
            currentTest.setTestStatus(Test.TestStatus.Passed);
            currentResult.setTestResultStatus(TestResult.TestResultStatus.Passed);
        }

        testDao.save(currentTest);
        testResultDao.save(currentResult);
    }

}