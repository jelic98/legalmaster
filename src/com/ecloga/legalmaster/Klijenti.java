package com.ecloga.legalmaster;

import org.apache.commons.io.FileUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class Klijenti {
    private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private int width = (int) (screenSize.getWidth() * 0.75);
    private int height = (int) (screenSize.getHeight() * 0.75);
    private JPanel panel, tablePanel, menuPanel;
    private static DefaultTableModel model;
    private static JTable table;
    private JLabel lEcloga;
    private JScrollPane scrollPane;
    private JButton bDodaj, bUkloni, bIzmeni, bPredmeti, bKalendar;
    private static ArrayList<String> klijenti = new ArrayList<String>();
    private static int maxID = 0;
    public static boolean infoShown = false;
    public static boolean kalendarShown = false;

    public Klijenti() {
        panel = new JPanel();
        tablePanel = new JPanel();
        menuPanel = new JPanel();

        lEcloga = new JLabel("Copyright © Ecloga Apps");
        lEcloga.setForeground(Color.GRAY);

        model = new DefaultTableModel();

        table = new JTable(model) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        String[] columns = {"ID", "Ime i prezime", "Broj telefona", "Email", "Adresa"};

        for(String value : columns) {
            model.addColumn(value);
        }

        table.setPreferredScrollableViewportSize(new Dimension(width, (int) (height * 0.8)));
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

        refresh();

        scrollPane = new JScrollPane(table);

        bDodaj = new JButton("Dodaj");
        bDodaj.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!infoShown) {
                    infoShown = true;

                    KlijentInfo dodaj = new KlijentInfo();
                    dodaj.show();
                }else {
                    JOptionPane.showMessageDialog(null, "Prozor je aktivan", "Poruka", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        bUkloni = new JButton("Ukloni");
        bUkloni.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = table.getSelectedRow();

                if(selectedIndex != -1) {
                    String id = String.valueOf(table.getValueAt(selectedIndex, 0));

                    model.removeRow(selectedIndex);
                    klijenti.remove(id);

                    String cmd = "SELECT * FROM predmeti";

                    ResultSet rs = null;

                    try {
                        rs = Main.s.executeQuery(cmd);

                        while(rs.next()){
                            if(String.valueOf(rs.getInt("klijent")).equals(id)) {
                                File media = new File(Main.directoryName + File.separator + "media" + File.separator + rs.getInt("id"));

                                try {
                                    FileUtils.deleteDirectory(media);
                                } catch (IOException e1) {
                                    e1.printStackTrace();
                                }

                                Main.executeDB("DELETE FROM tok WHERE predmet=" + rs.getInt("id"));
                                Main.executeDB("DELETE FROM cenovnik WHERE predmet=" + rs.getInt("id"));
                            }
                        }

                        rs.close();
                    }catch(SQLException e1) {
                        e1.printStackTrace();
                    }

                    Main.executeDB("DELETE FROM klijenti WHERE id=" + id);
                    Main.executeDB("DELETE FROM predmeti WHERE klijent=" + id);
                }else {
                    JOptionPane.showMessageDialog(null, "Klijent nije selektovan", "Poruka", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        bIzmeni = new JButton("Izmeni");
        bIzmeni.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!infoShown) {
                    int selectedIndex = table.getSelectedRow();

                    if(selectedIndex != -1) {
                        infoShown = true;

                        HashMap<Integer, String> info = new HashMap<Integer, String>();

                        for(int i = 0; i < table.getColumnCount(); i++) {
                            info.put(i, String.valueOf(table.getValueAt(selectedIndex, i)));
                        }

                        KlijentInfo izmeni = new KlijentInfo(info);
                        izmeni.show();
                    }else {
                        JOptionPane.showMessageDialog(null, "Klijent nije selektovan", "Poruka", JOptionPane.INFORMATION_MESSAGE);
                    }
                }else {
                    JOptionPane.showMessageDialog(null, "Prozor je aktivan", "Poruka", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        bPredmeti = new JButton("Predmeti");
        bPredmeti.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = table.getSelectedRow();

                if(selectedIndex != -1) {
                    String ime = String.valueOf(table.getValueAt(selectedIndex, 1));

                    Predmeti predmeti = new Predmeti(ime);
                    predmeti.show();
                }else {
                    JOptionPane.showMessageDialog(null, "Klijent nije selektovan", "Poruka", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        bKalendar = new JButton("Kalendar");
        bKalendar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Kalendar kalendar = new Kalendar();
                kalendar.show();

                kalendarShown = true;
            }
        });
    }

    public static void add(Object[] row) {
        maxID++;
        String id = String.valueOf(maxID);
        row[0] = id;
        Main.executeDB("INSERT INTO klijenti VALUES (" + row[0] + ", '" + row[1] + "', '" + row[2] + "', '" + row[3] + "', '" + row[4] + "')");
        addRow(row);
    }

    public static void addRow(Object[] row) {
        model.addRow(row);
        table.setModel(model);
        klijenti.add(String.valueOf(row[0]));
    }

    public static void update(Object[] row) {
        String id = String.valueOf(row[0]);
        Main.executeDB("UPDATE klijenti SET ime='" + row[1] + "', broj='" + row[2] + "', email='" + row[3] + "', adresa='" + row[4] + "' WHERE id=" + id);
        refresh();
    }

    public static void refresh() {
        model.setRowCount(0);
        table.setModel(model);

        String cmd = "SELECT * FROM klijenti";

        ResultSet rs = null;

        try {
            rs = Main.s.executeQuery(cmd);

            while(rs.next()){
                addRow(new Object[] {rs.getInt("id"), rs.getString("ime"), rs.getString("broj"), rs.getString("email"), rs.getString("adresa")});

                if(rs.getInt("id") > maxID) {
                    maxID = rs.getInt("id");
                }
            }

            rs.close();
        }catch(SQLException e) {
            e.printStackTrace();
        }
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
        panel.add(lEcloga);

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
