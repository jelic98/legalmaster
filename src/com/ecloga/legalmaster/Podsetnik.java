package com.ecloga.legalmaster;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.ArrayList;

public class Podsetnik {
    private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private int width = (int) (screenSize.getWidth() * 0.3);
    private int height = (int) (screenSize.getHeight() * 0.75);
    private JPanel panel, listPanel, menuPanel;
    private JScrollPane scrollPane;
    private JButton bDodaj, bUkloni;
    private JFrame frame = new JFrame();
    private ArrayList<String> lines = new ArrayList<String>();

    public Podsetnik() {
        panel = new JPanel();
        listPanel = new JPanel();
        menuPanel = new JPanel();

        JList list = new JList();

        DefaultListModel model = new DefaultListModel();

        list.setModel(model);

        scrollPane = new JScrollPane(list);

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

        bDodaj = new JButton("Dodaj");
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

        bUkloni = new JButton("Ukloni");
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
        menuPanel.add(bDodaj);
        menuPanel.add(bUkloni);

        listPanel.add(scrollPane);

        panel.add(menuPanel);
        panel.add(listPanel);

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
