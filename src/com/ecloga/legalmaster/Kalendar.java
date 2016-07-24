package com.ecloga.legalmaster;

import javax.swing.*;

public class Kalendar {
    public static void pick() {
        new DatePicker().setPickedDate();
    }

    public static void show(String datum) {
        JOptionPane.showMessageDialog(null, datum, datum, JOptionPane.INFORMATION_MESSAGE);
    }
}