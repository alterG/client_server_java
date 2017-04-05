import javax.swing.*;
import java.awt.*;
import java.util.*;

/**
 * Created by alterG on 24.12.2016.
 */
public class GraphInter extends JFrame {
    VisualPanel visual;
    ToolsMenu tools;
    int serverPort = 7780;
    int clientPort = 7881;
    static LinkedHashSet<Dot> dotList= new LinkedHashSet<>();;

    public static LinkedHashSet<Dot> getDotList() {
        return dotList;
    }

    public GraphInter() {
        getContentPane().setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(250,250,220));
        setSize(900,600);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setTitle("Homework #5. Shchipanov Igor P3202");
        visual = new VisualPanel(this);
        tools = new ToolsMenu(this);
        add(visual,BorderLayout.WEST);
        add(tools,BorderLayout.CENTER);
        setVisible(true);
        setResizable(false);
    }

}


