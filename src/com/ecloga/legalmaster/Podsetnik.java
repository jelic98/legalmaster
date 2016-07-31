package com.ecloga.legalmaster;

import com.sun.codemodel.internal.JOp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;

public class Podsetnik {
    private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private int width = (int) (screenSize.getWidth() * 0.3);
    private int height = (int) (screenSize.getHeight() * 0.3);
    private JPanel panel, listPanel, menuPanel;
    private JScrollPane scrollPane;
    private JButton bDodaj, bUkloni;
    private JList list;
    private JFrame frame = new JFrame();
    private ArrayList<String> lines = new ArrayList<String>();

    public Podsetnik() {
        DefaultListModel model = new DefaultListModel();

        list.setModel(model);
        list.setVisibleRowCount(10);

        File f = new File(Main.directoryName + File.separator + "rem.lm");

        try {
            FileInputStream fs = new FileInputStream(f);
            DataInputStream in = new DataInputStream(fs);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line;
            lines.clear();

            while((line = br.readLine()) != null) {
                model.addElement(line);
                list.setModel(model);

                lines.add(line);
            }

            in.close();
        }catch(IOException e) {
            e.printStackTrace();
        }

        list.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                JList list = (JList) evt.getSource();

                if(evt.getClickCount() == 2) {
                    int selectedIndex = list.locationToIndex(evt.getPoint());
                    String line = String.valueOf(model.getElementAt(selectedIndex));

                    JOptionPane.showMessageDialog(null, line, "Podsetnik", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        bDodaj.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String field = JOptionPane.showInputDialog(null, "Dodaj podsetnik");

                if(field != null && !field.isEmpty()) {
                    model.addElement(field);
                    list.setModel(model);

                    lines.add(field);

                    Main.writeFile(Main.directoryName + File.separator + "rem.lm", lines);
                }
            }
        });

        bUkloni.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = list.getSelectedIndex();

                if(selectedIndex != -1) {
                    String line = list.getSelectedValue().toString();

                    model.removeElement(line);
                    list.setModel(model);

                    lines.remove(line);

                    Main.writeFile(Main.directoryName + File.separator + "rem.lm", lines);
                }else {
                    JOptionPane.showMessageDialog(null, "Podsetnik nije selektovan", "Poruka", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
    }

    public void show() {
        frame.setVisible(true);
        frame.setAlwaysOnTop(true);
        frame.toFront();
        frame.requestFocus();
        frame.setAlwaysOnTop(false);

        frame.setTitle("Podsetnik");
        frame.setSize(new Dimension(width, height));
        frame.setLocation(screenSize.width / 2 - width / 2,screenSize.height / 2 - height / 2);
        frame.setContentPane(panel);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                Klijenti.podsetnikShown = false;
            }
        });
    }
}
