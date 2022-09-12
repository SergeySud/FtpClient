import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.Map;
import java.util.NoSuchElementException;


public class TestCases {
    private static final String EOL =
            System.getProperty("line.separator");
    private PrintStream console;
    private ByteArrayOutputStream bytes;

//    String expectedStudents =

    String address = "localhost";

    String user = "user";

    String pass = "testovpass";

    Map<Integer, String> expectedStudents;

    {
        try {
            expectedStudents = quickData();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @BeforeTest
    public void setUp() {
        bytes = new ByteArrayOutputStream();
        console = System.out;
        System.setOut(new PrintStream(bytes));

    }

    @AfterTest
    public void tearDown() {
        System.setOut(console);
        System.setIn(System.in);
    }

    @Test
    public void attemptToLogInActiveMode() throws IOException {
        String input = "1" + EOL + address + EOL + user + EOL + pass + EOL;
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        try {
            ClientMain.app();
        } catch (NoSuchElementException e) {
            String[] responses = (bytes.toString().split(EOL));
            String response = responses[responses.length - 7];
            Assert.assertEquals(response, "230 User user logged in.");
        }}

        @Test
        public void testListOfStudents () throws IOException {
            String input = "2" + EOL + address + EOL + user + EOL + pass + EOL + "1" + EOL + "6" + EOL;
            InputStream in = new ByteArrayInputStream(input.getBytes());
            System.setIn(in);
            ClientMain.app();
            String[] responses = (bytes.toString().split(EOL));
            String students = responses[responses.length - 2];
            Assert.assertEquals(students, StudentActions.printStudents(expectedStudents).toString());

        }


        public Map<Integer, String> quickData () throws IOException {
            URL url = new URL("ftp://" + user + ":" + pass + "@" + address + ":21/1.txt");
            URLConnection conn = url.openConnection();
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            ByteArrayOutputStream buf = new ByteArrayOutputStream();
            for (int result = bis.read(); result != -1; result = bis.read()) {
                buf.write((byte) result);
            }
            String result = buf.toString();
            is.close();
            bis.close();
            buf.close();
            return JsonParser.parseJson(result);
        }
    }
