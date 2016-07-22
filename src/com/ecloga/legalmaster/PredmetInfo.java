package com.ecloga.legalmaster;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class PredmetInfo {
    private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private int width = (int) (screenSize.getWidth() * 0.25);
    private int height = (int) (screenSize.getHeight() * 0.5);
    private JFrame frame = new JFrame();
    private JPanel panel;
    private JTextField tfIme, tfSifra, tfCena, tfPlaceno;
    private JButton bSacuvaj;
    private JButton bOtvori;
    private String nazivForme, id;
    private HashMap<Integer, String> info = new HashMap<Integer, String>();

    public PredmetInfo(Predmeti predmet) {
        nazivForme = "Dodaj predmet";

        tfIme.setText("");
        tfCena.setText("");
        tfPlaceno.setText("");

        bSacuvaj.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(tfSifra.getText().isEmpty() || tfSifra.getText() == null) {
                    JOptionPane.showMessageDialog(null, "Sifra predmeta je neophodna", "Poruka", JOptionPane.INFORMATION_MESSAGE);
                }else {
                    info.put(0, "0");
                    info.put(1, tfIme.getText());
                    info.put(2, tfSifra.getText());
                    info.put(3, tfCena.getText());
                    info.put(4, tfPlaceno.getText());

                    Object[] obj = new Object[info.size() + 1];
                    int i = 0;

                    for(Integer key : info.keySet()) {
                        String value = info.get(key);
                        obj[i] = value;

                        i++;
                    }

                    predmet.add(obj);

                    predmet.infoShown = false;

                    frame.dispose();
                }
            }
        });

        bOtvori.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openMedia(Main.directoryName + File.separator + "media" + File.separator + "temp");
            }
        });
    }

    public PredmetInfo(Predmeti predmet, HashMap<Integer, String> info) {
        this.info = info;

        nazivForme = info.get(2) + " - Izmena";

        id = info.get(0);

        tfIme.setText(info.get(1));
        tfSifra.setText(info.get(2));
        tfCena.setText(info.get(3));
        tfPlaceno.setText(info.get(4));

        bSacuvaj.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(tfSifra.getText().isEmpty() || tfSifra.getText() == null) {
                    JOptionPane.showMessageDialog(null, "Sifra predmeta je neophodna", "Poruka", JOptionPane.INFORMATION_MESSAGE);
                }else {
                    info.put(0, id);
                    info.put(1, tfIme.getText());
                    info.put(2, tfSifra.getText());
                    info.put(3, tfCena.getText());
                    info.put(4, tfPlaceno.getText());

                    Object[] obj = new Object[info.size() + 1];
                    int i = 0;

                    for(Integer key : info.keySet()) {
                        String value = info.get(key);
                        obj[i] = value;

                        i++;
                    }

                    predmet.update(obj);

                    predmet.infoShown = false;

                    frame.dispose();
                }
            }
        });

        bOtvori.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openMedia(Main.directoryName + File.separator + "media" + File.separator + id);
            }
        });
    }

    private void openMedia(String dir) {
        try {
            Desktop.getDesktop().open(new File(dir));
        } catch (IOException e) {
            e.printStackTrace();
        }
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
