import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

public class StudentActions {

    static List<String> printStudents(Map<Integer, String> students) {
        List<String> studentNamesSorted = students.values().stream()
                .sorted().collect(Collectors.toList());

        return studentNamesSorted;

    }

    static String getStudentById(Map<Integer, String> students) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter student's id");
        int id = scanner.nextInt();
        return students.get(id);
    }

    static String addStudent(Map<Integer, String> students) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter student's name");
        String name = scanner.next();
        int newMax = students.keySet().stream().max(Integer::compareTo).orElse(1);
        students.put(newMax, name);
        return "Added by id " + newMax;
    }

    static String removeStudent(Map<Integer, String> students) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter student's id to remove");
        int id = scanner.nextInt();
        if (students.containsKey(id)) {
            students.remove(id);
            return "Deleted";
        } else {
            return "The student is not found.";
        }
    }

    static String upload(Map<Integer, String> students) throws IOException {
        URL url = new URL("ftp://" + ConnectionHandler.credentials[0] + ":" + ConnectionHandler.credentials[1] + "@" + ConnectionHandler.address + ":21/1.txt");
        URLConnection connection = url.openConnection();
        connection.setDoOutput(true);
        OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
        out.write(JsonParser.parseMap(students));
        out.close();
        return "Uploaded successfully";
    }
}
