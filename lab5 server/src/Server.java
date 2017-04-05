import java.net.*;

/**
 * Created by alterG on 30.01.2017.
 */
public class Server {
    private static int serverPort = 7780;
    private static int clientPort = 7881;
    private final static int length = 256;


    public static void main(String[] args) {
        try {
            System.out.println("Server is ready...");
            DatagramSocket datagramSocket = new DatagramSocket(serverPort);
//            DatagramPacket packetIn = new DatagramPacket(new byte[length], length);
            while (true) {
                synchronized (datagramSocket) {
                DatagramPacket packetIn = new DatagramPacket(new byte[length], length);
                datagramSocket.receive(packetIn);
                new Thread(new ServerThread(datagramSocket, packetIn, clientPort)).start();}
            }
        } catch (Exception e) {
            System.out.println(e+" message error: "+e.getMessage());
        }
    }
}
