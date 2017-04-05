import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.net.*;
import java.io.*;
import java.util.ArrayList;

/**
 * Created by alterG on 10.01.2017.
 */
public class VisualPanel extends JPanel{
    private int width;
    private int height;
    double scale;
    GraphInter graphInter;
    int dotWidthAnimate=10;
    int dotHeightAnimate =10;
    boolean syncFlag; // crutch for blocking multiclicking (same dots send to server)
    static boolean flagAnimate; // play animation if true
    Thread animateThread;

    public void setRadius(double radius) {
        this.radius = radius;
    }
    public static void setFlagAnimate(boolean flagAnimate) {
        VisualPanel.flagAnimate = flagAnimate;
    }

    double radius=5;
    int renderRadius;
    String dotCoords="";

    public VisualPanel(GraphInter graphInter) {
        this.setPreferredSize(new Dimension(500,600));
        this.graphInter=graphInter;
        ArrayList<Thread> animationThreads = new ArrayList<>();
        this.repaint();
    }

    public void paint(Graphics g) {
        scale = 200/radius;
        Graphics2D graph = (Graphics2D) g;
        graph.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        width = this.getSize().width;
        height = this.getSize().height;
        renderRadius = (int) Math.floor(radius*scale);
        graph.setColor(new Color(0,0, 255));
        graph.fillArc(width/2-renderRadius/2, height/2-renderRadius/2, renderRadius, renderRadius, 0, -90); // draw part of circle
        graph.fillRect(width/2-renderRadius/2, height/2-renderRadius, renderRadius/2, renderRadius); // draw rectangle
        int[] dotX = {width/2,width/2+renderRadius,width/2};
        int[] dotY = {height/2-renderRadius, height/2, height/2};
        graph.fillPolygon(dotX, dotY, dotX.length); //draw triangle
        // draw axis
        graph.setColor(new Color(0,0, 0));
        graph.drawLine(width/2, height,width/2,0);
        graph.drawLine(0,height/2,width, height/2);
        //draw horizontal arrow
        int[] dotX1 = {width-10,width,width-10};
        int[] dotY1 = {height/2+5, height/2, height/2-5};
        graph.fillPolygon(dotX1, dotY1, dotX1.length);
        graph.drawString("x", width-20, height/2+20);
        //draw vertical arrow
        int[] dotX2 = {width/2-5,width/2,width/2+5};
        int[] dotY2 = {10, 0, 10};
        graph.fillPolygon(dotX2, dotY2, dotX1.length);
        graph.drawString("y", width/2+20, 20);
        //draw horizontal scale
        graph.drawLine(width/2-renderRadius, height/2-8,width/2-renderRadius, height/2+8);
        graph.drawLine(width/2-renderRadius/2, height/2-8,width/2-renderRadius/2, height/2+8);
        graph.drawLine(width/2+renderRadius, height/2-8,width/2+renderRadius, height/2+8);
        graph.drawLine(width/2+renderRadius/2, height/2-8,width/2+renderRadius/2, height/2+8);
        graph.drawString("-"+radius,width/2-renderRadius-10,height/2+20);
        graph.drawString("-"+radius/2,width/2-renderRadius/2-10,height/2+20);
        graph.drawString(""+radius,width/2+renderRadius-10,height/2+20);
        graph.drawString(""+radius/2,width/2+renderRadius/2-10,height/2+20);
        //draw vertical scale
        graph.drawLine(width/2-8, height/2-renderRadius,width/2+8, height/2-renderRadius);
        graph.drawLine(width/2-8, height/2-renderRadius/2,width/2+8, height/2-renderRadius/2);
        graph.drawLine(width/2-8, height/2+renderRadius,width/2+8, height/2+renderRadius);
        graph.drawLine(width/2-8, height/2+renderRadius/2,width/2+8, height/2+renderRadius/2);
        graph.drawString("-"+radius, width/2-30, height/2+renderRadius);
        graph.drawString("-"+radius/2, width/2-30, height/2+renderRadius/2);
        graph.drawString(""+radius, width/2-30, height/2-renderRadius);
        graph.drawString(""+radius/2, width/2-30, height/2-renderRadius/2);
        //draw last dot's coordinates
        graph.drawString(dotCoords, width-150, 30);
        //new points
            for (Dot dot : GraphInter.dotList) {
                if (!dot.getReceived()) graph.setColor(Color.gray);
                else if (dot.getCurrentIncludeStatus()) graph.setColor(Color.green);
                     else graph.setColor(Color.red);
                graph.fillOval((int)(width/2-dotWidthAnimate/2+scale*dot.getX()), (int)(height/2-dotWidthAnimate/2-scale*dot.getY()), dotWidthAnimate, dotHeightAnimate);
            }
        syncFlag=true;

            this.addMouseListener(new MouseInputAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (syncFlag) {
                        syncFlag=!syncFlag; //block multiclicking
                        float coordXBuf = (float) ((e.getX() - width / 2) / scale);
                        float coordYBuf = (float) ((height / 2 - e.getY()) / scale);
                        GraphInter.dotList.add(new Dot(coordXBuf, coordYBuf));
                        try {
                            DatagramSocket socket = new DatagramSocket();
                            final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                            final DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
                            dataOutputStream.writeFloat(GraphInter.dotList.size() - 1); //index of next dot 1 11
                            dataOutputStream.writeFloat(coordXBuf); //x
                            dataOutputStream.writeFloat(coordYBuf); //y
                            dataOutputStream.writeFloat((float) radius); // radius
                            dataOutputStream.close();
                            final byte[] bytesPacketOut = byteArrayOutputStream.toByteArray();
                            DatagramPacket packetOut = new DatagramPacket(bytesPacketOut, bytesPacketOut.length, InetAddress.getByName("helios.cs.ifmo.ru"), graphInter.serverPort);
                            socket.send(packetOut);
                            System.out.println("Data has been sent. Dot index = " + (GraphInter.dotList.size() - 1));

                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                        dotCoords = String.format("Last dot x = %.2f y = %.2f", coordXBuf, coordYBuf);
                        graphInter.repaint();
                    }
                }
            });
            if (flagAnimate) {
                flagAnimate=false;
                if (animateThread!=null) animateThread.interrupt();
                animateThread = new Thread(new animateThread());
                animateThread.start();
                }
    }

    class animateThread implements Runnable {
        @Override
        public void run() {
            try {
                for (int i = 0; i <= 20; i++) {
                    dotHeightAnimate = i;
                    dotWidthAnimate = i;
                    graphInter.repaint();
                    graphInter.repaint();
                    Thread.sleep(20);
                }
                for (int i = 20; i >= 0; i--) {
                    dotHeightAnimate = i;
                    dotWidthAnimate = i;
                    graphInter.repaint();
                    graphInter.repaint();
                    Thread.sleep(20);
                }
                for (int i = 0; i <= 20; i++) {
                    dotHeightAnimate = i;
                    dotWidthAnimate = i;
                    graphInter.repaint();
                    graphInter.repaint();
                    Thread.sleep(20);
                }
                for (int i = 20; i >= 10; i--) {
                    dotHeightAnimate = i;
                    dotWidthAnimate = i;
                    graphInter.repaint();
                    graphInter.repaint();
                    Thread.sleep(20);
                }
            }
            catch (Exception e) {

            }
        }
    }
}
