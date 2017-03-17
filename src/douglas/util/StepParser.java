package douglas.util;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class StepParser {

    // Helper class to parse JSON-string into typed JSONArray
    public static JSONArray parse(String stringifiedSteps) {
        JSONParser parser = new JSONParser();
        JSONArray steps = new JSONArray();
        try {
            steps = (JSONArray)parser.parse(stringifiedSteps);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return steps;
    }
}
