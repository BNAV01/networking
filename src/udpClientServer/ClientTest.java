package udpClientServer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ClientTest {

    public static void main(String[] args) throws IOException {

        // STEP 1 Create a Diagram Socket Object
        DatagramSocket datagramSocket = new DatagramSocket(8082);                // STEP 1
        // STEP 2 Create a buffer for incoming diagrams
        byte[] buffer = new byte[256];                                               //STEP 2
        // STEP 3 Create a DatagramPacket for the incoming datagrams
        DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length); //STEP 3
        // STEP 4 Accept an incoming datagram
        datagramSocket.receive(datagramPacket);                                    //STEP 4
        // STEP 5 Accept the sender's address and port from the packet
        InetAddress clientAddress = datagramSocket.getInetAddress();               //STEP 5
        // STEP 6 Retrieve the data from the buffer
        String message = new String(datagramPacket.getData(), 0, datagramPacket.getLength());  // STEP 6
        // STEP 7 Create the response datagram
        DatagramPacket outPacket = new DatagramPacket(message.getBytes(), message.length(), clientAddress, datagramPacket.getPort());  // STEP 7
        // STEP 8 Send the response datagram
        datagramSocket.send(outPacket);  // STEP 8
        // STEP 9 Close the DATAGRAMSOCKET !!
        datagramSocket.close();


    }
}