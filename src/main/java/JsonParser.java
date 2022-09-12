import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class JsonParser {
    static Map<Integer, String> parseJson(String json) {
        Pattern patternId = Pattern.compile("(?<=\"id\": )(.*)(?=,(\\n|\\r\\n))");
        Matcher matcherId = patternId.matcher(json);

        Pattern patternName = Pattern.compile("(?<=\"name\": \")(.*?)(?=\"(\\n|\\r\\n))");
        Matcher matcherName = patternName.matcher(json);

        Map<Integer, String> students = new HashMap<>();

        while (matcherId.find() && matcherName.find()) {
            int studentId = Integer.parseInt(json.substring(matcherId.start(), matcherId.end()));
            String studentName = json.substring(matcherName.start(), matcherName.end());

            students.put(studentId, studentName);
        }

        return students;
    }

    static String parseMap(Map<Integer, String> students) {
        String json = "{\n" +
                "\t\"students\": [\n";
        for (Map.Entry<Integer, String> student : students.entrySet()
        ) {
            json += "\t\t{\n" +
                    "\t\t\t\"id\": " + student.getKey() + ",\n" +
                    "\t\t\t\"name\": \"" + student.getValue() + "\"\n" +
                    "\t\t},\n";
        }
        json = json.substring(0, json.length() - 2);
        json += "\n\t]\n" +
                "}\n";
        return json;
    }
}
