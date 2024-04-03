package udpClientServer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class Server {

    private static final int PORT = 8082;

    public static void main(String[] args) {
        System.out.println("ABRIENDO PUERTO: " + PORT + "\n");
        try (DatagramSocket datagramSocket = new DatagramSocket(PORT)) {
            handleClient(datagramSocket);
        } catch (SocketException socketException) {
            System.out.println("IMPOSIBLE ACCEDER AL PUERTO");
            System.out.println(socketException.getMessage());
            System.exit(1);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public static void handleClient(DatagramSocket datagramSocket) throws IOException {
        byte[] buffer = new byte[256];
        int numMessages = 0;

        while (true) {
            DatagramPacket inPacket = new DatagramPacket(buffer, buffer.length);
            datagramSocket.receive(inPacket);

            String messageIn = new String(inPacket.getData(), 0, inPacket.getLength());
            System.out.println("Mensaje recibido");
            numMessages++;

            String messageOut = "Mensaje " + numMessages + " : " + messageIn;
            DatagramPacket outPacket = new DatagramPacket(messageOut.getBytes(), messageOut.length(),
                    inPacket.getAddress(), inPacket.getPort());
            datagramSocket.send(outPacket);
            System.out.println("Mensaje : " + messageOut);
        }
    }
}
