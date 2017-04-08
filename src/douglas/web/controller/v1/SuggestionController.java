package douglas.web.controller.v1;

import douglas.domain.Test;
import douglas.domain.TestStep;
import douglas.persistence.TestDao;
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

    @Autowired
    public SuggestionController(TestDao testDao) {
        this.testDao = testDao;
    }

    // The purpose of this endpoint is to grab the suggested new CSS selector and metadata
    // from the test and use that information to transition the step from "unstable"
    // to "passed"

    // Takes two parameters, the test ID and the ID of the step we
    // want to accept the suggestion from
    @Transactional
    @RequestMapping(path = {"/accept/{testId}/{stepId}"}, method = RequestMethod.GET)
    public void acceptSuggestion(@PathVariable String testId, @PathVariable String stepId) {

        Long testStepId = Long.parseLong(stepId);
        Test currentTest = testDao.findById(testId);

        // Move all data from the suggestion-property to the step of the result
        List<TestStep> testSteps = currentTest.getTestSteps();

        // We need the index when we wanna do manipulation on the testSteps list
        int intIndex = 0;
        for(int i = 0; i < testSteps.size(); i++) {
            if(testSteps.get(i).getId().equals(testStepId)) {
                intIndex = i;
                break;
            }
        }

        TestStep affectedStep = testSteps.get(intIndex);

        if (affectedStep == null) throw new RuntimeException("Couldn't find TestStep with Id: " + stepId);

        JSONParser parser = new JSONParser();
        JSONObject suggestion = new JSONObject();
        try {
            suggestion = (JSONObject)parser.parse(affectedStep.getSuggestion());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        affectedStep.setTestStepStatus(TestStep.Status.Passed);
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

        testSteps.remove(intIndex);
        testSteps.add(intIndex, affectedStep);
        currentTest.setTestSteps(testSteps);


        // We have now accepted the suggestion in the test meaning that the step
        // is now "passed" but it isn't enough that the test knows that things change
        // we perhaps also need to update the general status on the test


        boolean failed = false;
        boolean unstable = false;

        // We iterate over all the steps to check if the whole test now passes or
        // other unstable/failed steps still exists in the test
        for(TestStep step : testSteps) {

            if(TestStep.Status.Unstable.equals(step.getTestStepStatus())) {
                unstable = true;
            }

            if(TestStep.Status.Failed.equals(step.getTestStepStatus())) {
                failed = true;
            }
        }

        // Based on the what we just found in the loop above do we set the status of the
        // test
        if(failed) {
            currentTest.setTestStatus(Test.Status.Failed);
        } else if (unstable) {
            currentTest.setTestStatus(Test.Status.Unstable);
        } else {
            currentTest.setTestStatus(Test.Status.Passed);
        }

        testDao.save(currentTest);
    }

}