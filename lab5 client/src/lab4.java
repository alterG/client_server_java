import java.io.*;
import java.net.*;
import java.util.*;


/**
 * Created by alterG on 24.12.2016.
 */
public class lab4 {
    static int clientPort=7881;
    private final static int length = 256;

    public static void main (String[] args) throws IOException {
        GraphInter mainFrame  = new GraphInter();
        new Thread(new FrameRepainter(mainFrame)).start();
        try {
            System.out.println("Client is ready...");
            DatagramSocket datagramSocket = new DatagramSocket(clientPort);

            while (true) {
                synchronized (datagramSocket) {
                    DatagramPacket packetIn = new DatagramPacket(new byte[length], length);
                    datagramSocket.receive(packetIn);
                    new Thread(new ClientThread(packetIn, mainFrame.getDotList())).start(); }
            }
        } catch (Exception e) {
            System.out.println(e+" message error: "+e.getMessage());
        }

    }

    enum Menu {
        typeR("type radius"),
        checkDots("check dots"),
        help("help");

        private String value;
        private Menu (String value) {
            this.value=value;
        }
        public static Menu getType(String value) {
            for (Menu menu : Menu.values()) {
                if (menu.value.equals(value)) {
                    return menu;
                }
            }
            return help;

        }
    }

    public static void printHelp() {
        System.out.println("List of commands:\ntype radius\ncheck dots\nhelp");
    }
}
