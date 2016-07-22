package com.ecloga.legalmaster;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;

public class TokInfo {
    private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private int width = (int) (screenSize.getWidth() * 0.25);
    private int height = (int) (screenSize.getHeight() * 0.5);
    private JFrame frame = new JFrame();
    private JPanel panel;
    private JTextField tfIme, tfDatum, tfVreme;
    private JButton bSacuvaj;
    private String nazivForme, id;
    private HashMap<Integer, String> info = new HashMap<Integer, String>();
    public boolean infoShown = false;

    public TokInfo(Tok tok) {
        nazivForme = "Dodaj fazu";

        tfDatum.setText("");
        tfVreme.setText("");

        bSacuvaj.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(tfIme.getText().isEmpty() || tfIme.getText() == null) {
                    JOptionPane.showMessageDialog(null, "Ime faze je neophodno", "Poruka", JOptionPane.INFORMATION_MESSAGE);
                }else {
                    info.put(0, "0");
                    info.put(1, tfIme.getText());
                    info.put(2, tfDatum.getText());
                    info.put(3, tfVreme.getText());

                    Object[] obj = new Object[info.size() + 1];
                    int i = 0;

                    for(Integer key : info.keySet()) {
                        String value = info.get(key);
                        obj[i] = value;

                        i++;
                    }

                    tok.add(obj);

                    tok.infoShown = false;

                    frame.dispose();
                }
            }
        });
    }

    public TokInfo(Tok tok, HashMap<Integer, String> info) {
        this.info = info;

        nazivForme = "Izmeni fazu";

        id = info.get(0);

        tfIme.setText(info.get(1));
        tfDatum.setText(info.get(2));
        tfVreme.setText(info.get(3));

        bSacuvaj.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(tfIme.getText().isEmpty() || tfIme.getText() == null) {
                    JOptionPane.showMessageDialog(null, "Ime faze je neophodno", "Poruka", JOptionPane.INFORMATION_MESSAGE);
                }else {
                    info.put(0, id);
                    info.put(1, tfIme.getText());
                    info.put(2, tfDatum.getText());
                    info.put(3, tfVreme.getText());

                    Object[] obj = new Object[info.size() + 1];
                    int i = 0;

                    for(Integer key : info.keySet()) {
                        String value = info.get(key);
                        obj[i] = value;

                        i++;
                    }

                    tok.add(obj);

                    tok.infoShown = false;

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
