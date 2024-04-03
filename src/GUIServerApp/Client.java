package GUIServerApp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class Client extends JFrame implements ActionListener {

    String username;
    PrintWriter pw;
    BufferedReader br;
    JTextArea taMessages, taStatus;
    JTextField tfInput;
    JButton btnSend, btnExit, btnReconnect;
    Socket client;

    public Client(String username, String serverName) throws IOException {
        super(username);
        this.username = username;
        try {
            client = new Socket(serverName, 8081);
            br = new BufferedReader(new InputStreamReader(client.getInputStream()));
            pw = new PrintWriter(client.getOutputStream(), true);
            pw.println(username);
            Interface();
            new MessageThread().start();
        } catch (IOException e) {
            System.out.println("Error de conexión con el servidor: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Error de conexión con el servidor: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void Interface() {
        btnSend = new JButton("Enviar");
        btnExit = new JButton("Salir");
        btnReconnect = new JButton("Reconectar");

        taMessages = new JTextArea();
        taMessages.setEditable(false);

        taStatus = new JTextArea();
        taStatus.setEditable(false);
        taStatus.append("Conectado al servidor.\n");

        tfInput = new JTextField();
        tfInput.addActionListener(this); // Permitimos enviar mensajes presionando Enter

        JScrollPane spMessages = new JScrollPane(taMessages);
        JScrollPane spStatus = new JScrollPane(taStatus);

        JPanel bp = new JPanel();
        bp.setLayout(new BoxLayout(bp, BoxLayout.X_AXIS));
        bp.add(btnSend);
        bp.add(btnExit);
        bp.add(tfInput);

        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.add(spStatus, BorderLayout.CENTER);
        statusPanel.add(btnReconnect, BorderLayout.LINE_END);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(spMessages, BorderLayout.CENTER);
        getContentPane().add(bp, BorderLayout.SOUTH);
        getContentPane().add(statusPanel, BorderLayout.NORTH);

        btnSend.addActionListener(this);
        btnExit.addActionListener(this);
        btnReconnect.addActionListener(this);

        Font boldFont = new Font(Font.SANS_SERIF, Font.BOLD, 14);
        tfInput.setFont(boldFont);
        taMessages.setFont(boldFont);
        taStatus.setFont(boldFont);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setSize(600, 400);
        setLocationRelativeTo(null); // Centrar la ventana en la pantalla
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnSend || e.getSource() == tfInput) {
            String message = tfInput.getText();
            if (!message.isEmpty()) {
                pw.println(message);
                tfInput.setText("");
            }
        } else if (e.getSource() == btnExit) {
            pw.println("Salir");
            System.out.println("Desconectando...");
            System.exit(0);
        } else if (e.getSource() == btnReconnect) {
            try {
                client = new Socket("localhost", 8081);
                br = new BufferedReader(new InputStreamReader(client.getInputStream()));
                pw = new PrintWriter(client.getOutputStream(), true);
                pw.println(username);
                taStatus.setText("Conectado al servidor.\n");
                new MessageThread().start();
            } catch (IOException ex) {
                System.out.println("Error al intentar reconectar: " + ex.getMessage());
                taStatus.setText("Error al intentar reconectar: " + ex.getMessage());
            }
        } else {
                String message = tfInput.getText();
                if (!message.isEmpty()) {
                    pw.println(message);
                    tfInput.setText("");
                }
            }
        }

        public static void main(String[] args) {
            String name = JOptionPane.showInputDialog(null, "Ingresa tu usuario: ", "Username", JOptionPane.PLAIN_MESSAGE);
            String serverName = "localhost";

            try {
                new Client(name, serverName);
            } catch (Exception e) {
                System.out.println("Error --> " + e.getMessage());
            }
        }

        class MessageThread extends Thread {
            public void run() {
                String line;
                try {
                    while ((line = br.readLine()) != null) {
                        taMessages.append(line + "\n");
                    }
                } catch (IOException e) {
                    System.out.println("Se perdió la conexión con el servidor.");
                    taStatus.setText("Se perdió la conexión con el servidor.");
                }
            }
        }
    }
