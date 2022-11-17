package OCR;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Display {
    public TextPane text;
    public StatTextPane stattext;
    public JFrame frame;
    public JLabel head;
    public JLabel data;

    public void setText(String txt) {
        this.head.setText(txt);
        refresh();
    }

    public Display(String txt, Color color) {
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(
                            UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException | InstantiationException
                        | IllegalAccessException
                        | UnsupportedLookAndFeelException ex) {
                    ex.printStackTrace();
                }

                frame = new JFrame(txt);
                frame.setPreferredSize(new Dimension(370, 600));

                // frame.setUndecorated(true);
                // frame.setBackground(new Color(0, 0, 0, 0));
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                text = new TextPane(txt,color);
                frame.add(text);
                frame.pack();
                // frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }

    public Display(String txt, Color color, boolean small) {
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(
                            UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException | InstantiationException
                        | IllegalAccessException
                        | UnsupportedLookAndFeelException ex) {
                    ex.printStackTrace();
                }

                frame = new JFrame(txt);
                frame.setPreferredSize(new Dimension(700, 250));

                // frame.setUndecorated(true);
                // frame.setBackground(new Color(0, 0, 0, 0));
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                stattext = new StatTextPane(txt,color);
                frame.add(stattext);
                frame.pack();
                // frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }

    public class TextPane extends JPanel {

        public TextPane(String txt,Color color) {
            // setOpaque(false);
            // setLayout(new GridBagLayout());
            head = new JLabel(txt);
            head.setFont(head.getFont().deriveFont(Font.BOLD, 64));
            head.setForeground(color);
            add(head);

            data = new JLabel("");
            data.setFont(head.getFont().deriveFont(Font.BOLD, 32));
            data.setForeground(color);
            add(data);
        }

    }
    
    public class StatTextPane extends JPanel {

        public StatTextPane(String txt,Color color) {
            // setOpaque(false);
            // setLayout(new GridBagLayout());
            head = new JLabel(txt);
            head.setFont(head.getFont().deriveFont(Font.BOLD, 36));
            head.setForeground(color);
            add(head);
        }

    }

    public void setData(HashMap<String, Integer> indata) {
        String text="<html>";
        for (Map.Entry<String, Integer> entry : indata.entrySet()) {
            text+=entry.getKey()+"("+entry.getValue()+")<br>";
        }
        text+="</html>";
        data.setText(text);
        refresh();
    }

    public void refresh() {
        try {
        SwingUtilities.updateComponentTreeUI(frame);
        }catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

}