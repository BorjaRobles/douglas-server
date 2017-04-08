package douglas.testrunner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import douglas.domain.Test;
import douglas.domain.TestResult;


public class ResultGenerator {

    // Generates a report of the previously executed test. The generated JSON is only used for showing
    // the results in the frontend
    public TestResult generate(Test test) {
        String json = new String();

        ObjectWriter ow = new ObjectMapper().writer();
        try {
            // Use Jackson to create a serialized representation of the executed test
            json = ow.writeValueAsString(test.getTestSteps());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return new TestResult(
                test.getName(),
                test.getDescription(),
                json,
                test.getTestStatus(),
                test.getId()
        );
    }
}
