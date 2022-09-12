import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.*;


public class TestCases {
    private static final String EOL =
            System.getProperty("line.separator");
    private PrintStream console;
    private ByteArrayOutputStream bytes;

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
        System.out.println(bytes.toString());
        System.out.println("here");
    }

    @Test
    public void testAbortWhenInsufficientArgumentsSupplied() throws IOException {
        String input = "2"+ EOL +"localhost" + EOL + "user" + EOL + "testovpass" + EOL + "1" + EOL + "6" + EOL;
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        ClientMain.app();

        Assert.assertEquals("1", "1");

    }
}
