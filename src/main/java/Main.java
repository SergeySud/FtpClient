import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {


    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose connection mode: 1 - active(not working, unable to establish connection with a listening socket), 2 - passive:");
        String input = scanner.next();
        Map<Integer, String> students = ConnectionHandler.connect(input);
        boolean running = true;
        while (running) {
            System.out.println("Choose action: 1 - print list of students, 2 - get student's information by id," +
                    " 3 - add student, 4 - remove student by id, 5 - upload changes, 6 - exit:");
            input = scanner.next();
            switch (input) {
                case ("1") -> System.out.println(StudentActions.printStudents(students));
                case ("2") -> System.out.println(StudentActions.getStudentById(students));
                case ("3") -> System.out.println(StudentActions.addStudent(students));
                case ("4") -> System.out.println(StudentActions.removeStudent(students));
                case ("5") -> System.out.println(StudentActions.upload(students));
                default -> running = false;
            }

        }


    }


}


