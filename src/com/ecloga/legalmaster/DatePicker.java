package com.ecloga.legalmaster;

import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.*;

public class DatePicker {
    private int month = java.util.Calendar.getInstance().get(java.util.Calendar.MONTH);
    private int year = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);
    private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private int width = (int) (screenSize.getWidth() * 0.5);
    private int height = (int) (screenSize.getHeight() * 0.5);
    private JLabel l = new JLabel("", JLabel.CENTER);
    private String day = "";
    private JDialog d = new JDialog();
    private JButton[] button = new JButton[49];

    public DatePicker() {
        String[] header = {"Nedelja", "Ponedeljak", "Utorak", "Sreda", "Cetvrtak", "Petak", "Subota"};
        JPanel p1 = new JPanel(new GridLayout(7, 7));
        p1.setPreferredSize(new Dimension(width, height));

        for (int x = 0; x < button.length; x++) {
            final int selection = x;

            button[x] = new JButton();
            button[x].setFocusPainted(false);
            button[x].setBackground(Color.WHITE);

            if(x > 6) {
                button[x].addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent ae) {
                        day = button[selection].getActionCommand();

                        if(day.length() == 1) {
                            day = "0" + day;
                        }

                        if(!day.isEmpty() && button[selection].getForeground() == Color.RED) {
                            Kalendar kalendar = new Kalendar();
                            kalendar.show(day + "-" + l.getText());

                            d.dispose();
                        }
                    }
                });
            }

            if (x < 7) {
                button[x].setText(header[x]);
                button[x].setForeground(Color.BLUE);
            }

            p1.add(button[x]);
        }

        JPanel p2 = new JPanel(new GridLayout(1, 3));

        JButton previous = new JButton("<");
        previous.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                month--;
                displayDate();
            }
        });

        p2.add(previous);

        p2.add(l);

        JButton next = new JButton(">");
        next.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                month++;
                displayDate();
            }
        });

        p2.add(next);

        displayDate();

        d.setModal(true);
        d.setTitle("Kalendar");
        d.setLocation(screenSize.width / 2 - width / 2, screenSize.height / 2 - height / 2);
        d.add(p1, BorderLayout.CENTER);
        d.add(p2, BorderLayout.SOUTH);
        d.pack();
        d.setVisible(true);
    }

    public String setPickedDate() {
        if (day.equals("")) {
            return day;
        }

        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd-MM-yyyy");
        java.util.Calendar cal = java.util.Calendar.getInstance();

        cal.set(year, month, Integer.parseInt(day));

        return sdf.format(cal.getTime());
    }

    private void displayDate() {
        for(int x = 7; x < button.length; x++) {
            button[x].setText("");
        }

        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MM-yyyy");
        java.util.Calendar cal = java.util.Calendar.getInstance();

        cal.set(year, month, 1);

        l.setText(sdf.format(cal.getTime()));

        int dayOfWeek = cal.get(java.util.Calendar.DAY_OF_WEEK);
        int daysInMonth = cal.getActualMaximum(java.util.Calendar.DAY_OF_MONTH);

        for(int x = 6 + dayOfWeek, day = 1; day <= daysInMonth; x++, day++) {
            button[x].setText(String.valueOf(day));

            String sDay;

            if(String.valueOf(day).length() == 1) {
                sDay = "0" + String.valueOf(day);
            }else {
                sDay = String.valueOf(day);
            }

            String datum = sDay + "-" + l.getText();

            if(!String.valueOf(day).isEmpty() && checkDay(datum)) {
                button[x].setForeground(Color.RED);
                button[x].setBorder(BorderFactory.createLineBorder(Color.RED, 2));
            }else {
                button[x].setForeground(Color.BLACK);
                button[x].setBorder(button[0].getBorder());
            }
        }
    }

    private boolean checkDay(String datum) {
        int counter = 0;

        String cmd = "SELECT * FROM tok";

        ResultSet rs = null;

        try {
            rs = Main.s.executeQuery(cmd);

            while(rs.next()) {
                if(rs.getString("datum").equals(datum)) {
                    counter++;
                }
            }

            rs.close();
        }catch(SQLException e) {
            e.printStackTrace();
        }

        if(counter > 0) {
            return true;
        }else {
            return false;
        }
    }
}
