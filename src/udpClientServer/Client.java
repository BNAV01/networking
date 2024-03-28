package udpClientServer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {

    private static final int PORT = 8082;

    public static void main(String[] args) {
        try {
            InetAddress host = InetAddress.getLocalHost();
            accessServer(host);
        } catch (UnknownHostException unknownHostException) {
            System.out.println("ERROR : " + unknownHostException.getMessage());
            System.exit(1);
        }
    }

    private static void accessServer(InetAddress host) {
        try (DatagramSocket datagramSocket = new DatagramSocket()) {
            Scanner userEntry = new Scanner(System.in);
            String message = "", response = "";
            do {
                System.out.println("Ingresa Mensaje : ");
                message = userEntry.nextLine();
                if (!message.equals("***CLOSE***")) {
                    byte[] buffer = message.getBytes();
                    DatagramPacket outPacket = new DatagramPacket(buffer, buffer.length, host, PORT);
                    datagramSocket.send(outPacket);

                    buffer = new byte[256];
                    DatagramPacket inPacket = new DatagramPacket(buffer, buffer.length);
                    datagramSocket.receive(inPacket);

                    response = new String(inPacket.getData(), 0, inPacket.getLength());
                    System.out.println("\nRespuesta del servidor > " + response);
                }
            } while (!message.equals("***CLOSE***"));
        } catch (IOException ioException) {
            ioException.printStackTrace();
            System.out.println("ERROR: " + ioException.getMessage());
        } finally {
            System.out.println("\n* Terminando Conexion ... *");
        }
    }
}
