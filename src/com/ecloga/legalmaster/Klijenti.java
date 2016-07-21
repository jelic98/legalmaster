package com.ecloga.legalmaster;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Klijenti {
    private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private int width = (int) (screenSize.getWidth() * 0.75);
    private int height = (int) (screenSize.getHeight() * 0.75);
    private JPanel panel, tablePanel, menuPanel;
    private DefaultTableModel model;
    private JTable table;
    private JScrollPane scrollPane;
    private JButton bDodaj, bUkloni, bIzmeni, bPredmeti, bKalendar;
    private ArrayList<Integer> klijenti = new ArrayList<Integer>();

    public Klijenti() {
        panel = new JPanel();
        tablePanel = new JPanel();
        menuPanel = new JPanel();

        model = new DefaultTableModel();

        table = new JTable(model) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        String[] columns = {"ID", "Ime i prezime", "Broj telefona", "Email", "Adresa", "Racun"};

        for(String value : columns) {
            model.addColumn(value);
        }

        table.setPreferredScrollableViewportSize(new Dimension(width, height));
        table.setFillsViewportHeight(true);
        table.getTableHeader().setReorderingAllowed(false);

        scrollPane = new JScrollPane(table);

        bDodaj = new JButton("Dodaj");
        bDodaj.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        bUkloni = new JButton("Ukloni");
        bUkloni.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = table.getSelectedRow();

                String id = String.valueOf(table.getValueAt(selectedIndex, 0));

                if(selectedIndex != -1) {
                    model.removeRow(selectedIndex);
                    klijenti.remove(id);

                    //todo delete klijent from db, delete all predmeti or klijent from db and directory
                }
            }
        });

        bIzmeni = new JButton("Izmeni");
        bIzmeni.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        bPredmeti = new JButton("Predmeti");
        bPredmeti.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        bKalendar = new JButton("Kalendar");
        bKalendar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }

    public void show() {
        menuPanel.add(bDodaj);
        menuPanel.add(bUkloni);
        menuPanel.add(bIzmeni);
        menuPanel.add(bPredmeti);
        menuPanel.add(bKalendar);

        tablePanel.add(scrollPane);

        panel.add(menuPanel);
        panel.add(tablePanel);

        JFrame frame = new JFrame();
        frame.setTitle("LegalMaster");
        frame.setSize(new Dimension(width, height));
        frame.setLocation(screenSize.width / 2 - width / 2,screenSize.height / 2 - height / 2);
        frame.setContentPane(panel);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);
    }
}
