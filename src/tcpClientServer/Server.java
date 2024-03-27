package tcpClientServer;

import java.io.*;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.Scanner;
import java.net.ServerSocket;
import java.util.logging.*;

public class Server {
    private static final int PORT = 8081;
    private static final ExecutorService executor = Executors.newCachedThreadPool();
    private static final Logger logger = Logger.getLogger(Server.class.getName());

    public static void main(String[] args) {
        System.out.println("ABRIENDO PUERTO: " + PORT + "\n");

        boolean serverRunning = true;

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (serverRunning) {
                Socket clientSocket = serverSocket.accept();
                executor.submit(new ClientHandler(clientSocket));
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error al abrir el puerto: " + e.getMessage(), e);
            System.exit(1);
        }
    }

    private static class ClientHandler implements Runnable {
        private final Socket clientSocket;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try (Socket socket = clientSocket;
                 Scanner input = new Scanner(socket.getInputStream());
                 PrintWriter output = new PrintWriter(socket.getOutputStream(), true)) {


                InetAddress clientAddress = socket.getInetAddress();
                String clientIpAddress = clientAddress.getHostAddress(); // Get IPv4 address
                System.out.println("Cliente conectado desde la dirección IP: " + clientIpAddress);

                int numMessages = 0;
                while (input.hasNextLine()) {
                    String message = input.nextLine();
                    if (message.equals("***CLOSE***")) {
                        break;
                    }
                    System.out.println("MENSAJE RECIBIDO.");
                    numMessages++;
                    output.println("MENSAJE NUM " + numMessages + ": " + message);
                }
                output.println(numMessages + " mensajes recibidos");
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Error de E/S: " + e.getMessage(), e);
                // Get client's IP address if available
                InetAddress clientAddress = clientSocket.getInetAddress();
                String clientIpAddress = (clientAddress != null) ? clientAddress.getHostAddress() : "Unknown";
                logger.log(Level.INFO, "Cliente desconectado desde la dirección IP: " + clientIpAddress);
            }
        }
    }
}
