import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Scanner;

public class ConnectionHandler {

    public static Map<Integer, String> connect(String mode) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter server address");
        String address = scanner.next();
        String[] credentials = credentials();


        return switch (mode) {
            case ("1") -> activeMode(address);
            case ("2") -> passiveMode(address, credentials);
            default -> passiveMode(address, credentials);
        };

    }

    public static String[] credentials() {
        Scanner scanner = new Scanner(System.in);
        String[] credentials = new String[]{"", ""};
        System.out.println("Enter username");
        credentials[0] = scanner.next();
        System.out.println("Enter password");
        credentials[1] = scanner.next();

        return credentials;

    }

    public static Map<Integer, String> activeMode(String address) throws IOException {
        Socket socket = new Socket(address, 21);
        InputStream is = socket.getInputStream();
        OutputStream os = socket.getOutputStream();
        int localPort = socket.getLocalPort() + 1;
        //Прослушиваем порт
        Thread thread = new Thread(() -> {
            System.out.println("Thread Running");
            try {
                ServerSocket serverSocket = new ServerSocket(localPort);
                serverSocket.accept();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        thread.start();
//
//            }
//            else { localPort += 1;}
//        }
        byte[] buffer = new byte[10000];
        int length = is.read(buffer);
        String s = new String(buffer, 0, length);
        //        s = new String(buffer, 0, length);
        //length = is.read(buffer);
        // Отправляем имя пользователя
        String str = "USER user\n";
        os.write(str.getBytes());
        // Отправляем пароль
        str = "PASS testovpass\n";
        os.write(str.getBytes());
        str = "LIST\n";
        os.write(str.getBytes());
        // Отправляем номер порта
        str = "PORT 192,0,0,1," + localPort / 256 + "," + localPort % 256 + "\n";
        os.write(str.getBytes());
        //Отправляем команду на копирование файла
        str = "TYPE A\n";
        os.write(str.getBytes());
        System.out.println(s);
        str = "RETR 1.txt\n";
        os.write(str.getBytes());
        return passiveMode(address, credentials());
    }

    public static Map<Integer, String> passiveMode(String address, String[] credentials) throws IOException {
        URL url = new URL("ftp://" + credentials[0] + ":" + credentials[1] + "@" + address + ":21/1.txt");
        URLConnection conn = url.openConnection();
        InputStream is = conn.getInputStream();
        BufferedInputStream bis = new BufferedInputStream(is);
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        for (int result = bis.read(); result != -1; result = bis.read()) {
            buf.write((byte) result);
        }
        String result = buf.toString(StandardCharsets.UTF_8);
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

class ShowList extends Thread {
    public int port = 0;

    public void run() {
        try {
            Socket socket = new Socket("192.168.0.1", this.port);
            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();
            byte[] buffer = new byte[10000];
            int length = is.read(buffer);
            String s = new String(buffer, 0, length);
            System.out.println(s);
// Закрываем ссылку
            is.close();
            os.close();
            socket.close();
        } catch (Exception ex) {
        }
    }
}