import java.io.*;
import java.net.*;
import java.util.*;

/**
 * Created by alterG on 31.01.2017.
 */
public class ClientThread implements Runnable {
    LinkedHashSet<Dot> dotList;
    DataInputStream dataStreamPacketIn;

    public ClientThread(DatagramPacket packetIn, LinkedHashSet<Dot> dotList) {
        dataStreamPacketIn = new DataInputStream(new ByteArrayInputStream(packetIn.getData()));
        this.dotList=dotList;
    }

    @Override
    public void run() {
        try {
            int index = dataStreamPacketIn.readInt();
            int response = dataStreamPacketIn.readInt();
            //crutch for getting dot from LinkedHashSet(task condition) by index.
            Iterator<Dot> iter = GraphInter.dotList.iterator();
            for (int i = 0; i < index; i++) {iter.next();}
            Dot buffDot = iter.next();
            buffDot.setReceived(true);
            buffDot.setCurrentIncludeStatus(response==1);
            if (buffDot.isNew()) buffDot.setNew(false);
            else if (buffDot.getCurrentIncludeStatus()!=buffDot.getLastIncludeStatus()) {VisualPanel.setFlagAnimate(true);
                System.out.println("animation!");}
            System.out.println("Dot index "+index+" included?"+response);
        } catch (Exception e) {
            System.out.println(e+" error message: "+e.getMessage());
        }

    }
}

