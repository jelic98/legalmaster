package com.ecloga.legalmaster;

import javax.swing.*;
import java.awt.*;

public class Predmeti {
    private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private int width = (int) (screenSize.getWidth() * 0.5);
    private int height = (int) (screenSize.getHeight() * 0.5);
    private JPanel panel;
    private String klijentIme;

    public Predmeti(String klijentIme) {
        this.klijentIme = klijentIme;
    }

    public void show() {
        JFrame frame = new JFrame();
        frame.setTitle(klijentIme + " - Predmeti");
        frame.setSize(new Dimension(width, height));
        frame.setLocation(screenSize.width / 2 - width / 2,screenSize.height / 2 - height / 2);
        frame.setContentPane(panel);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);
    }
}
