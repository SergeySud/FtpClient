import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsonParser {
    private static final String EOL =
            System.getProperty("line.separator");

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
        StringBuilder json = new StringBuilder("{\n" +
                "\t\"students\": [\n");
        for (Map.Entry<Integer, String> student : students.entrySet()
        ) {
            json.append("\t\t{\n" + "\t\t\t\"id\": ").append(student.getKey()).append(",\n")
                    .append("\t\t\t\"name\": \"").append(student.getValue()).append("\"\n").append("\t\t},\n");
        }
        json = new StringBuilder(json.substring(0, json.length() - 2));
        json.append("\n\t]\n" + "}\n");
        return json.toString();
    }
}
