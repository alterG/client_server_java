import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
import java.util.Iterator;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Created by alterG on 12.01.2017.
 */
public class ToolsMenu extends JPanel implements Runnable {
    JList jListX;
    int width,height;
    DefaultListModel<Double> listModel; //model of JList component (determinate X coords)
    ButtonGroup buttonGroupY; //JRadioButtonGroup (for Y coords)
    JSlider jSliderR;
    Dot currentDot;
    ResourceBundle labels; //current names of labels according locale
    int length = 256; //size of datagrams


    public ToolsMenu(GraphInter p) {
        this.setPreferredSize(new Dimension(400,600));
        currentDot = new Dot(0,0);
        labels = ResourceBundle.getBundle("locale.Data", new Locale("eng"));
        //JList for X
        JLabel labelX = new JLabel((String)labels.getObject("chooseX")); //label for X
        listModel = new DefaultListModel<Double>(); //add JList model for X coords
        for (double i = 1; i <= 5; i++) {listModel.addElement(i);} //initialization listModel
        jListX = new JList(listModel);
        jListX.setLayoutOrientation(JList.VERTICAL);
        jListX.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jListX.setSelectedIndex(0);
        JScrollPane listScroller = new JScrollPane(jListX);


        //JRadioButtonGroup for Y
        JLabel labelY = new JLabel((String)labels.getObject("chooseY")); // label for Y
        buttonGroupY = new ButtonGroup();
        JRadioButton firstRadioButton = new JRadioButton("-5", true);
        JRadioButton secondRadioButton = new JRadioButton("-4", false);
        JRadioButton thirdRadioButton = new JRadioButton("0", false);
        JRadioButton forthRadioButton = new JRadioButton("1", false);
        JRadioButton fifthRadioButton = new JRadioButton("2", false);
        JRadioButton sixthRadioButton = new JRadioButton("3", false);
        JRadioButton seventhRadioButton = new JRadioButton("5", false);
        firstRadioButton.setActionCommand("-5");
        secondRadioButton.setActionCommand("-4");
        thirdRadioButton.setActionCommand("0");
        forthRadioButton.setActionCommand("1");
        fifthRadioButton.setActionCommand("2");
        sixthRadioButton.setActionCommand("3");
        seventhRadioButton.setActionCommand("5");
        buttonGroupY.add(firstRadioButton);
        buttonGroupY.add(secondRadioButton);
        buttonGroupY.add(thirdRadioButton);
        buttonGroupY.add(forthRadioButton);
        buttonGroupY.add(fifthRadioButton);
        buttonGroupY.add(sixthRadioButton);
        buttonGroupY.add(seventhRadioButton);
        JPanel radioButtonsPanel = new JPanel(new GridLayout(4,3)); //add buttons (Y coords)
        radioButtonsPanel.add(firstRadioButton);
        radioButtonsPanel.add(secondRadioButton);
        radioButtonsPanel.add(thirdRadioButton);
        radioButtonsPanel.add(forthRadioButton);
        radioButtonsPanel.add(fifthRadioButton);
        radioButtonsPanel.add(sixthRadioButton);
        radioButtonsPanel.add(seventhRadioButton);
        //radioButtonsPanel decoration
        firstRadioButton.setOpaque(false);
        secondRadioButton.setOpaque(false);
        thirdRadioButton.setOpaque(false);
        forthRadioButton.setOpaque(false);
        fifthRadioButton.setOpaque(false);
        sixthRadioButton.setOpaque(false);
        seventhRadioButton.setOpaque(false);
        radioButtonsPanel.setBackground(Color.white);


        //JSlider for R
        JLabel labelR = new JLabel((String)labels.getObject("chooseR")); //label for R
        jSliderR = new JSlider(JSlider.HORIZONTAL,1,10,5);
        jSliderR.setMajorTickSpacing(3);
        jSliderR.setMinorTickSpacing(1);
        jSliderR.setPaintTicks(true);
        jSliderR.setPaintLabels(true);

        //JButton for adding dot
        JButton addDotButton = new JButton((String)labels.getObject("addDot"));

        //JButton for removing dot
        JButton removeDotsButton = new JButton((String)labels.getObject("removeDot"));

        //add locale bar
        ImageIcon engIcon = new ImageIcon("images/engIcon.png");
        ImageIcon ukrIcon = new ImageIcon("images/ukrIcon.png");
        JButton localeEng = new JButton();
        localeEng.setIcon(engIcon);
        JButton localeUkr = new JButton();
        localeUkr.setIcon(ukrIcon);


        //Some position magic
        localeEng.setBounds(374,5,16,16);
        localeUkr.setBounds(356,5,16,16);
        labelY.setBounds(20,-20,230,80);
        radioButtonsPanel.setBounds(20,40,230,85);
        labelX.setBounds(20,-20,230,80);
        jListX.setBounds(20,40,220,100);
        labelR.setBounds(20,-20,230,80);
        jSliderR.setBounds(20,40,320,80);
        addDotButton.setBounds(20,20,120,40);
        removeDotsButton.setBounds(160,20,180,40);

        //Add components
        this.setLayout(null);

        JPanel iconBar = new JPanel(null);
        iconBar.add(localeEng);
        iconBar.add(localeUkr);
        iconBar.setBounds(0,0,400,20);
        this.add(iconBar);

        JPanel dotPickerPanel = new JPanel(new GridLayout(0,1));
        dotPickerPanel.setBounds(0, 30,400, 570);

        JPanel panelX = new JPanel(null);
        panelX.add(labelX);
        panelX.add(jListX);
        dotPickerPanel.add(panelX);

        JPanel panelY = new JPanel(null);
        panelY.add(labelY);
        panelY.add(radioButtonsPanel);
        dotPickerPanel.add(panelY);

        JPanel panelR = new JPanel(null);
        panelR.add(labelR);
        panelR.add(jSliderR);
        dotPickerPanel.add(panelR);

        JPanel panelButtons = new JPanel(null);
        panelButtons.add(addDotButton);
        panelButtons.add(removeDotsButton);
        dotPickerPanel.add(panelButtons);

        this.add(dotPickerPanel);

        //add ActionListener's
        addDotButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    currentDot.setX(Double.parseDouble(jListX.getSelectedValue().toString()));
                    currentDot.setY(Double.parseDouble(buttonGroupY.getSelection().getActionCommand()));
                    GraphInter.dotList.add(new Dot(currentDot.getX(), currentDot.getY()));
                    //sending new Dot to check for including
                try {
                    DatagramSocket socket = new DatagramSocket();
                    final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    final DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
                    dataOutputStream.writeFloat(GraphInter.dotList.size()-1); //index of next dot 1 11
                    dataOutputStream.writeFloat(Float.parseFloat(jListX.getSelectedValue().toString())); //x
                    dataOutputStream.writeFloat(Float.parseFloat(buttonGroupY.getSelection().getActionCommand())); //y
                    dataOutputStream.writeFloat((jSliderR.getValue())); // radius
                    dataOutputStream.close();
                    final byte[] bytesPacketOut = byteArrayOutputStream.toByteArray();
                    DatagramPacket packetOut = new DatagramPacket(bytesPacketOut, bytesPacketOut.length, InetAddress.getByName("helios.cs.ifmo.ru"), p.serverPort);
                    socket.send(packetOut);
                    System.out.println("Data has been sent. Dot index = "+(GraphInter .dotList.size()-1)+" to "+packetOut.getAddress()+" on port "+packetOut.getPort());

                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                    p.visual.dotCoords=String.format("Last dot x = %.2f y = %.2f",currentDot.getX(),currentDot.getY());
                    p.repaint();
            }
        });
        removeDotsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GraphInter.dotList.clear();
                p.repaint();

            }
        });

        jSliderR.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int index=0;
                p.visual.setRadius(jSliderR.getValue());
                try {
                    DatagramSocket socket = new DatagramSocket();
                    for (Iterator<Dot> iter = p.dotList.iterator(); iter.hasNext();) {
                        Dot buffDot = iter.next();
                        buffDot.setReceived(false);
                        buffDot.setLastIncludeStatus(buffDot.getCurrentIncludeStatus());
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
                        dataOutputStream.writeFloat(index); //index of next dot 1 11
                        dataOutputStream.writeFloat((float)buffDot.getX()); //x
                        dataOutputStream.writeFloat((float)buffDot.getY()); //y
                        dataOutputStream.writeFloat((jSliderR.getValue())); // radius
                        dataOutputStream.close();
                        final byte[] bytesPacketOut = byteArrayOutputStream.toByteArray();
                        DatagramPacket packetOut = new DatagramPacket(bytesPacketOut, bytesPacketOut.length, InetAddress.getByName("helios.cs.ifmo.ru"), p.serverPort);
                        socket.send(packetOut);
                        System.out.println("Data has been sent. Dot index = "+index+" x= "+buffDot.getX()+" y= "+buffDot.getY());
                        index++;
                    }
                } catch (IOException e1) {
                    System.out.println(e1+ " error message "+e1.getMessage());
                }
                p.repaint();
            }
        });

        localeEng.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                labels = ResourceBundle.getBundle("locale.Data", new Locale("eng"));
                labelX.setText((String)labels.getObject("chooseX"));
                labelY.setText((String)labels.getObject("chooseY"));
                labelR.setText((String)labels.getObject("chooseR"));
                addDotButton.setText((String)labels.getObject("addDot"));
                removeDotsButton.setText((String)labels.getObject("removeDot"));
            }
        });

        localeUkr.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                labels = ResourceBundle.getBundle("locale.Data", new Locale("ukr"));
                labelX.setText((String)labels.getObject("chooseX"));
                labelY.setText((String)labels.getObject("chooseY"));
                labelR.setText((String)labels.getObject("chooseR"));
                addDotButton.setText((String)labels.getObject("addDot"));
                removeDotsButton.setText((String)labels.getObject("removeDot"));
            }
        });


    }

    @Override
    public void run() {
        try {
            DatagramSocket socket = new DatagramSocket(7778, InetAddress.getByName("helios.cs.ifmo.ru"));
            System.out.println("Client-receiver is ready");
            for(;;) {
                DatagramPacket packetIn = new DatagramPacket(new byte[length], length);
                socket.receive(packetIn);
                final DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(packetIn.getData()));
                int index = dataInputStream.readInt();
                boolean isIncluded = (dataInputStream.readInt() == 1);
                //crutch for getting dot from LinkedHashSet(task condition) by index.
                Iterator<Dot> iter = GraphInter.dotList.iterator();
                for (int i = 0; i < index; i++) {iter.next();}
                Dot buffDot = iter.next();
                //For new dot current and last include statuses are same
                buffDot.setCurrentIncludeStatus(isIncluded);
                buffDot.setLastIncludeStatus(isIncluded);
                buffDot.setReceived(true);
                System.out.println("Data has been received. Dot index = "+index+" status "+isIncluded);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
