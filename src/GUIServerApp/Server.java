package GUIServerApp;

import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    private final Vector<String> users = new Vector<>();
    private final Vector<HandleClient> clients = new Vector<>();

    public void process() {
        try (ServerSocket server = new ServerSocket(8081, 10)) {
            System.out.println("Server Started...");
            while (true) {
                Socket client = server.accept();
                HandleClient c = new HandleClient(client);
                clients.add(c);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Server().process();
    }

    public void broadcast(String user, String message) {
        for (HandleClient c : clients) {
            c.sendMessage(user, message); // Envía el mensaje a todos los clientes
        }
    }


    class HandleClient extends Thread {
        private String name;
        private final BufferedReader input;
        private final PrintWriter output;

        public HandleClient(Socket client) throws IOException {
            input = new BufferedReader(new InputStreamReader(client.getInputStream()));
            output = new PrintWriter(client.getOutputStream(), true);
            name = input.readLine();
            users.add(name);
            start();
        }

        public void sendMessage(String userName, String message) {
            output.println(userName + "--> " + message);
        }

        public String getUsername() {
            return name;
        }

        public void run() {
            String line;
            try {
                while ((line = input.readLine()) != null) {
                    if (line.equals("Finalizar")) {
                        clients.remove(this);
                        users.remove(name);
                        System.out.println("Cliente " + name + " desconectado.");
                        break;
                    } else if (line.equals("Salir")) {
                        clients.remove(this);
                        users.remove(name);
                        System.out.println("Cliente " + name + " desconectado.");
                        break;
                    }
                    broadcast(name, line);
                }
            } catch (IOException e) {
                // Error de conexión reseteada, eliminar al cliente de la lista
                clients.remove(this);
                users.remove(name);
                System.out.println("Cliente " + name + " desconectado forzosamente.");
            } finally {
                try {
                    input.close();
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    }
}
