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

    static int addStudent(Map<Integer, String> students){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter student's name");
        String name = scanner.next();
        int newMax = students.keySet().stream().max(Integer::compareTo).orElse(1);
        students.put(newMax, name);
        return newMax;
    }

    static void removeStudent(Map<Integer, String> students) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter student's id to remove");
        int id = scanner.nextInt();
        students.remove(id);
    }
}
