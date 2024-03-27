package tcpClientServer;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private static final int PORT = 8081;

    public static void main(String[] args) {
        try {
            InetAddress host = InetAddress.getLocalHost();
            accessServer(host);
        } catch (IOException e) {
            System.out.println("ERROR: " + e.getMessage());
            System.exit(1);
        }
    }

    private static void accessServer(InetAddress host) {
        try (Socket link = new Socket(host, PORT);
             Scanner input = new Scanner(link.getInputStream());
             PrintWriter output = new PrintWriter(link.getOutputStream(), true);
             Scanner userEntry = new Scanner(System.in)) {

            String message, response;
            do {
                System.out.println("INGRESA UN MENSAJE : ");
                message = userEntry.nextLine();
                output.println(message);
                response = input.nextLine();
                System.out.println("\nSERVER > " + response);
            } while (!message.equals("***CLOSE***"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
