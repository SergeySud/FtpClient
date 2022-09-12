import org.testng.Assert;
import org.testng.annotations.*;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class TestCases {
    private static final String EOL =
            System.getProperty("line.separator");
    private PrintStream console;

    private InputStream standardInput;
    private ByteArrayOutputStream bytes;

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


    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        bytes = new ByteArrayOutputStream();
        standardInput = System.in;
        console = System.out;
        System.setOut(new PrintStream(bytes));

    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() throws IOException {
        System.in.read(new byte[System.in.available()]);
        System.setOut(console);
        System.setIn(standardInput);
    }

    @Test
    public void attemptToLogInActiveMode() throws IOException {
        String input = "1" + EOL + address + EOL + user + EOL + pass + EOL;
        setIn(input);
        try {
            ClientMain.app();
        } catch (NoSuchElementException e) {
            String[] responses = (bytes.toString().split(EOL));
            String response = responses[responses.length - 7];
            Assert.assertEquals(response, "230 User user logged in.");
        }
    }

    @Test
    public void testListOfStudents() throws IOException {
        String input = "2" + EOL + address + EOL + user + EOL + pass + EOL + "1" + EOL + "6" + EOL;
        setIn(input);
        ClientMain.app();
        String[] responses = (bytes.toString().split(EOL));
        String students = responses[responses.length - 2];
        Assert.assertEquals(students, StudentActions.printStudents(expectedStudents).toString());

    }

    @Test
    public void addStudent() throws IOException {
        byte[] array = new byte[7]; // length is bounded by 7
        new Random().nextBytes(array);
        String generatedString = new String(array, StandardCharsets.UTF_8);
        String input = "2" + EOL + address + EOL + user + EOL + pass + EOL + "3" + EOL
                + generatedString + EOL + "5" + EOL + "6" + EOL;
        setIn(input);
        ClientMain.app();
        String[] responses = (bytes.toString().split(EOL));
        String addedMessage = responses[responses.length - 4];
        Pattern p = Pattern.compile("(?<=Added by id )+(\\d*)");
        Matcher m = p.matcher(addedMessage);
        String idString = null;
        if (m.find()) {
            idString = addedMessage.substring(m.start(), m.end());  // The matched substring
        }

        Assert.assertEquals(addedMessage, "Added by id " + idString);
    }


    public Map<Integer, String> quickData() throws IOException {
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

    public static void setIn(String input) throws IOException {
        System.in.read(new byte[System.in.available()]);
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
    }
}

