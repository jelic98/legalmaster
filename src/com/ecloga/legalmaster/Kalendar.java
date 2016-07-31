package com.ecloga.legalmaster;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Kalendar {
    private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private int width = (int) (screenSize.getWidth() * 0.25);
    private int height = (int) (screenSize.getHeight() * 0.5);
    private JPanel panel, tablePanel, menuPanel;
    private JButton bNazad, bZatvori;
    private DefaultTableModel model;
    private JTable table;
    private JScrollPane scrollPane;
    private JFrame frame = new JFrame();

    public Kalendar() {
        panel = new JPanel();
        tablePanel = new JPanel();
        menuPanel = new JPanel();

        model = new DefaultTableModel();

        table = new JTable(model) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        String[] columns = {"Faza", "Vreme", "Predmet", "Klijent"};

        for(String value : columns) {
            model.addColumn(value);
        }

        table.setPreferredScrollableViewportSize(new Dimension(width, height));
        table.setFillsViewportHeight(true);
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setResizingAllowed(true);

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                if(!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.decode("#ecf0f1") : Color.WHITE);
                }

                return c;
            }
        });

        scrollPane = new JScrollPane(table);

        bNazad = new JButton("Nazad");
        bNazad.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pick();

                frame.dispose();
            }
        });

        bZatvori = new JButton("Zatvori");
        bZatvori.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });
    }

    public void pick() {
        new DatePicker().setPickedDate();
    }

    public void show(String datum) {
        menuPanel.add(bNazad);
        menuPanel.add(bZatvori);

        tablePanel.add(scrollPane);

        panel.add(menuPanel);
        panel.add(tablePanel);

        frame.setVisible(true);
        frame.setAlwaysOnTop(true);
        frame.toFront();
        frame.requestFocus();
        frame.setAlwaysOnTop(false);

        frame.setTitle(datum);
        frame.setSize(new Dimension(width, height));
        frame.setLocation(screenSize.width / 2 - width / 2,screenSize.height / 2 - height / 2);
        frame.setContentPane(panel);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);

        populateTable(datum);
    }

    private void populateTable(String datum) {
        model.setRowCount(0);
        table.setModel(model);

        String cmd = "SELECT * FROM tok WHERE datum='" + datum + "'";

        ResultSet rs = null;

        try {
            rs = Main.s.executeQuery(cmd);

            while(rs.next()) {
                String predmet = "";
                String klijent = "";

                String innerCmd = "SELECT * FROM predmeti WHERE id=" + rs.getInt("predmet");

                ResultSet innerRs = null;

                try {
                    innerRs = Main.c.createStatement().executeQuery(innerCmd);

                    while(innerRs.next()) {
                        predmet = innerRs.getString("sifra");
                        klijent = innerRs.getString("klijent");

                        String doubleInnerCmd = "SELECT * FROM klijenti WHERE id=" + klijent;

                        ResultSet doubleInnerRs = null;

                        try {
                            doubleInnerRs = Main.c.createStatement().executeQuery(doubleInnerCmd);

                            while(doubleInnerRs.next()) {
                                klijent = doubleInnerRs.getString("ime");
                            }

                            doubleInnerRs.close();
                        }catch(SQLException e) {
                            e.printStackTrace();
                        }
                    }

                    innerRs.close();
                }catch(SQLException e) {
                    e.printStackTrace();
                }

                model.addRow(new Object[] {rs.getString("ime"), rs.getString("vreme"), predmet, klijent});
                table.setModel(model);
            }

            rs.close();
        }catch(SQLException e) {
            e.printStackTrace();
        }
    }
}