import java.io.*;
import java.util.Map;
import java.util.Scanner;

public class ClientMain {


    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose connection mode: 1 - active(not working, unable to establish connection with a listening socket), 2 - passive:");
        String input = scanner.next();
        Map<Integer, String> students = ConnectionHandler.connect(input);
        boolean running = true;
        while (running) {
            System.out.println("Choose action: 1 - print a list of students, 2 - get a student's information by id," +
                    " 3 - add a student, 4 - remove a student by id, 5 - upload changes, 6 - exit:");
            input = scanner.next();
            switch (input) {
                case ("1"): {System.out.println(StudentActions.printStudents(students)); break;}
                case ("2"): {System.out.println(StudentActions.getStudentById(students)); break;}
                case ("3"): {System.out.println(StudentActions.addStudent(students)); break;}
                case ("4"): {System.out.println(StudentActions.removeStudent(students)); break;}
                case ("5"): {System.out.println(StudentActions.upload(students)); break;}
                default: {running = false; break;}
            }

        }


    }


}


