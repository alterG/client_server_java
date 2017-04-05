import java.io.*;
import java.net.*;


/**
 * Created by alterG on 25.01.2017.
 */
public class ServerThread implements Runnable  {

    private InetAddress inetAddress;
    private DatagramSocket datagramSocket;
    private int clientPort;
    DataInputStream dataStreamPacketIn;

     public  ServerThread(DatagramSocket datagramSocket, DatagramPacket packetIn, int clientPort) {
        dataStreamPacketIn = new DataInputStream(new ByteArrayInputStream(packetIn.getData()));
        this.datagramSocket=datagramSocket;
        this.clientPort=clientPort;
        inetAddress = packetIn.getAddress();
        }

    @Override
    public void run() {
        try {
            float index = dataStreamPacketIn.readFloat();
            float x = dataStreamPacketIn.readFloat();
            float y = dataStreamPacketIn.readFloat();
            float r = dataStreamPacketIn.readFloat();
            System.out.println("Get Dot index "+(int)index+" x="+x+" y="+y+ " r="+r);
            Figure.setR(r);
            int response = Figure.isIncluded(x,y);
            ByteArrayOutputStream byteArrayPacketOut = new ByteArrayOutputStream();
            DataOutputStream dataStreamPacketOut = new DataOutputStream(byteArrayPacketOut);
            dataStreamPacketOut.writeInt((int)index);
            dataStreamPacketOut.writeInt(response);
            dataStreamPacketOut.close();
            byte[] bytesPacketOut = byteArrayPacketOut.toByteArray();
            DatagramPacket packetOut = new DatagramPacket(bytesPacketOut, bytesPacketOut.length, inetAddress, clientPort);
            datagramSocket.send(packetOut);
            System.out.println("Dot index "+(int)index+" x="+x+" y="+y+ " r="+r+" included?"+response);
        } catch (Exception e) {
            System.out.println(e+" error message: "+e.getMessage());
        }

    }
}
