import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ConnectionHandler {

    static int localPort;
    static String address;
    static String[] credentials;

    public static Map<Integer, String> connect(String mode) throws IOException {
        Scanner scanner = ClientMain.scanner;
        System.out.println("Enter server address:");
        address = scanner.next();
        credentials = credentials();
        Map<Integer, String> students = new HashMap<>();
        {
            {


                switch (mode) {
                    case ("1"): {
                        activeMode();
                        break;
                    }
                    case ("2"): {
                        passiveMode();
                        break;
                    }
                    default: {
                        throw new IOException("Invalid input");
                    }
                }
            }
        }

        return students;

    }

    public static String[] credentials() {
        Scanner scanner = ClientMain.scanner;
        String[] credentials = new String[]{"", ""};
        System.out.println("Enter username:");
        credentials[0] = scanner.next();
        System.out.println("Enter password:");
        credentials[1] = scanner.next();

        return credentials;

    }

    public static Map<Integer, String> activeMode() throws IOException {
        Socket socket = new Socket(address, 21);
        InputStream is = socket.getInputStream();
        OutputStream os = socket.getOutputStream();
        localPort = socket.getLocalPort() + 1;
        //Прослушиваем порт
//        Thread thread = new Thread(() -> {
//            Socket clientSocket;
//            PrintWriter out;
//            BufferedReader in;
//            System.out.println("Thread Running");
//            try {
//                ServerSocket serverSocket = new ServerSocket(localPort);
//                clientSocket = serverSocket.accept();
//                out = new PrintWriter(clientSocket.getOutputStream(), true);
//                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
//                serverSocket.accept();
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        });
//        thread.start();
        byte[] buffer = new byte[10000];
        int length = is.read(buffer);
        String s = new String(buffer, 0, length);
        //        s = new String(buffer, 0, length);
        // Отправляем имя пользователя
        String str = "USER user\n";
        os.write(str.getBytes());
        s = new String(buffer, 0, length);
        length = is.read(buffer);
        System.out.println(s);
        // Отправляем пароль
        str = "PASS testovpass\n";
        os.write(str.getBytes());
        s = new String(buffer, 0, length);
        length = is.read(buffer);
        System.out.println(s);
        // Отправляем номер порта
        str = "PORT 192,0,0,1," + localPort / 256 + "," + localPort % 256 + "\n";
        os.write(str.getBytes());
        s = new String(buffer, 0, length);
        length = is.read(buffer);
        //Отправляем команду на копирование файла
        str = "TYPE A\n";
        os.write(str.getBytes());
        System.out.println(s);
        str = "RETR 1.txt\n";
        os.write(str.getBytes());
        s = new String(buffer, 0, length);
        length = is.read(buffer);
        System.out.println(s);
        is.close();
        os.close();
//        thread.stop();
        return passiveMode();
    }

    public static Map<Integer, String> passiveMode() throws IOException {
        URL url = new URL("ftp://" + credentials[0] + ":" + credentials[1] + "@" + address + ":21/1.txt");
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

    private static boolean available(int port) {
        try (Socket ignored = new Socket("localhost", port)) {
            return false;
        } catch (IOException ignored) {
            return true;
        }
    }
}