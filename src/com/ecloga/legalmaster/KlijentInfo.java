package com.ecloga.legalmaster;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;

public class KlijentInfo {
    private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private int width = (int) (screenSize.getWidth() * 0.25);
    private int height = (int) (screenSize.getHeight() * 0.5);
    private JFrame frame = new JFrame();
    private JPanel panel;
    private JTextField tfIme, tfBroj, tfEmail, tfAdresa;
    private JButton bSacuvaj;
    private String nazivForme, id;
    private HashMap<Integer, String> info = new HashMap<Integer, String>();

    public KlijentInfo() {
        nazivForme = "Dodaj klijenta";

        tfBroj.setText("");
        tfEmail.setText("");
        tfAdresa.setText("");

        bSacuvaj.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(tfIme.getText().isEmpty() || tfIme.getText() == null) {
                    JOptionPane.showMessageDialog(null, "Ime klijenta je neophodno", "Poruka", JOptionPane.INFORMATION_MESSAGE);
                }else {
                    info.put(0, "0");
                    info.put(1, tfIme.getText());
                    info.put(2, tfBroj.getText());
                    info.put(3, tfEmail.getText());
                    info.put(4, tfAdresa.getText());

                    Object[] obj = new Object[info.size() + 1];
                    int i = 0;

                    for(Integer key : info.keySet()) {
                        String value = info.get(key);
                        obj[i] = value;

                        i++;
                    }

                    Klijenti.add(obj);

                    Klijenti.infoShown = false;

                    frame.dispose();
                }
            }
        });
    }

    public KlijentInfo(HashMap<Integer, String> info) {
        this.info = info;

        nazivForme = info.get(1) + " - Izmena";

        id = info.get(0);

        tfIme.setText(info.get(1));
        tfBroj.setText(info.get(2));
        tfEmail.setText(info.get(3));
        tfAdresa.setText(info.get(4));

        bSacuvaj.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(tfIme.getText().isEmpty() || tfIme.getText() == null) {
                    JOptionPane.showMessageDialog(null, "Ime klijenta je neophodno", "Poruka", JOptionPane.INFORMATION_MESSAGE);
                }else {
                    info.put(0, id);
                    info.put(1, tfIme.getText());
                    info.put(2, tfBroj.getText());
                    info.put(3, tfEmail.getText());
                    info.put(4, tfAdresa.getText());

                    Object[] obj = new Object[info.size() + 1];
                    int i = 0;

                    for(Integer key : info.keySet()) {
                        String value = info.get(key);
                        obj[i] = value;

                        i++;
                    }

                    Klijenti.update(obj);

                    Klijenti.infoShown = false;

                    frame.dispose();
                }
            }
        });
    }

    public void show() {
        frame.setTitle(nazivForme);
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
                Klijenti.infoShown = false;
            }
        });
    }
}
