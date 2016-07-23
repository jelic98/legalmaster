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
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class Predmeti {
    private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private int width = (int) (screenSize.getWidth() * 0.5);
    private int height = (int) (screenSize.getHeight() * 0.5);
    private JPanel panel, tablePanel, menuPanel;
    private DefaultTableModel model;
    private JTable table;
    private JScrollPane scrollPane;
    private JButton bDodaj, bUkloni, bIzmeni, bTok;
    private ArrayList<String> predmeti = new ArrayList<String>();
    private String klijentIme;
    private int maxID = 0;
    public boolean infoShown = false;
    private int klijentId;

    public Predmeti(String klijentIme) {
        this.klijentIme = klijentIme;

        panel = new JPanel();
        tablePanel = new JPanel();
        menuPanel = new JPanel();

        model = new DefaultTableModel();

        table = new JTable(model) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        String[] columns = {"ID", "Sifra", "Ime", "Cena", "Placeno"};

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

        refresh();

        scrollPane = new JScrollPane(table);

        bDodaj = new JButton("Dodaj");
        bDodaj.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!infoShown) {
                    PredmetInfo dodaj = new PredmetInfo(Predmeti.this);
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
                if(!infoShown) {
                    int selectedIndex = table.getSelectedRow();

                    if(selectedIndex != -1) {
                        String id = String.valueOf(table.getValueAt(selectedIndex, 0));

                        Main.executeDB("DELETE FROM predmeti WHERE id=" + id);
                        Main.executeDB("DELETE FROM tok WHERE predmet=" + id);

                        model.removeRow(selectedIndex);
                        predmeti.remove(id);

                        File media = new File(Main.directoryName + File.separator + "media" + File.separator + id);

                        try {
                            FileUtils.deleteDirectory(media);
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }else {
                        JOptionPane.showMessageDialog(null, "Predmet nije selektovan", "Poruka", JOptionPane.INFORMATION_MESSAGE);
                    }
                }else {
                    JOptionPane.showMessageDialog(null, "Prozor je aktivan", "Poruka", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        bIzmeni = new JButton("Izmeni");
        bIzmeni.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int selectedIndex = table.getSelectedRow();

                if(selectedIndex != -1) {
                    HashMap<Integer, String> info = new HashMap<Integer, String>();

                    for(int i = 0; i < table.getColumnCount(); i++) {
                        info.put(i, String.valueOf(table.getValueAt(selectedIndex, i)));
                    }

                    PredmetInfo izmeni = new PredmetInfo(Predmeti.this, info);
                    izmeni.show();
                }else {
                    JOptionPane.showMessageDialog(null, "Predmet nije selektovan", "Poruka", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        bTok = new JButton("Tok predmeta");
        bTok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = table.getSelectedRow();

                if(selectedIndex != -1) {
                    String sifra = String.valueOf(table.getValueAt(selectedIndex, 1));

                    Tok tok = new Tok(sifra);
                    tok.show();
                }else {
                    JOptionPane.showMessageDialog(null, "Predmet nije selektovan", "Poruka", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
    }

    public void add(Object[] row) {
        maxID++;
        String id = String.valueOf(maxID);
        row[0] = id;

        File srcDir = new File(Main.directoryName + File.separator + "media" + File.separator + "temp");
        String destName = Main.directoryName + File.separator + "media" + File.separator + id;
        File destDir = new File(destName);

        try {
            if(Files.exists(Paths.get(destName))) {
                FileUtils.deleteDirectory(destDir);
            }

            FileUtils.copyDirectory(srcDir, destDir);
            FileUtils.cleanDirectory(srcDir);
        }catch(IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Medija se ne moze premestiti", "Error", JOptionPane.ERROR_MESSAGE);
        }

        Main.executeDB("INSERT INTO predmeti VALUES (" + row[0] + ", '" + row[1] + "', '" + row[2] + "', '" + row[3] + "', '" + row[4] + "', "+ getId() + ")");
        addRow(row);
    }

    private void addRow(Object[] row) {
        model.addRow(row);
        table.setModel(model);
        predmeti.add(String.valueOf(row[0]));
    }

    public void update(Object[] row) {
        String id = String.valueOf(row[0]);
        Main.executeDB("UPDATE predmeti SET sifra='" + row[1] + "', ime='" + row[2] + "', cena='" + row[3] + "', placeno='" + row[4] + "' WHERE id=" + id);
        refresh();
    }

    private void refresh() {
        model.setRowCount(0);
        table.setModel(model);

        ResultSet rs = null;

        String cmd = "SELECT * FROM predmeti";

        klijentId = getId();

        try {
            rs = Main.s.executeQuery(cmd);

            while(rs.next()){
                if(rs.getInt("klijent") == klijentId) {
                    addRow(new Object[] {rs.getInt("id"), rs.getString("sifra"), rs.getString("ime"), rs.getString("cena"), rs.getString("placeno")});
                }

                if(rs.getInt("id") > maxID) {
                    maxID = rs.getInt("id");
                }
            }

            rs.close();
        }catch(SQLException e) {
            e.printStackTrace();
        }
    }

    private int getId() {
        ResultSet rs = null;

        String cmd = "SELECT * FROM klijenti WHERE ime='" + klijentIme + "'";

        int klijentId = 0;

        try {
            rs = Main.s.executeQuery(cmd);

            while(rs.next()){
                klijentId = rs.getInt("id");
            }

            rs.close();
        }catch(SQLException e) {
            e.printStackTrace();
        }

        return klijentId;
    }

    public void show() {
        menuPanel.add(bDodaj);
        menuPanel.add(bUkloni);
        menuPanel.add(bIzmeni);
        menuPanel.add(bTok);

        tablePanel.add(scrollPane);

        panel.add(menuPanel);
        panel.add(tablePanel);

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
