package com.ecloga.legalmaster;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

public class Klijenti {
    private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private int width = (int) (screenSize.getWidth() * 0.75);
    private int height = (int) (screenSize.getHeight() * 0.75);
    private JPanel panel, tablePanel, menuPanel;
    private static DefaultTableModel model;
    private static JTable table;
    private JScrollPane scrollPane;
    private JButton bDodaj, bUkloni, bIzmeni, bPredmeti, bKalendar;
    private static ArrayList<String> klijenti = new ArrayList<String>();
    private static int maxID = 0;
    public static boolean infoShown = false;

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

        String[] columns = {"ID", "Ime i prezime", "Broj telefona", "Email", "Adresa"};

        for(String value : columns) {
            model.addColumn(value);
        }

        table.setPreferredScrollableViewportSize(new Dimension(width, height));
        table.setFillsViewportHeight(true);
        table.getTableHeader().setReorderingAllowed(false);

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                if(!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.LIGHT_GRAY : Color.WHITE);
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

                    Main.executeDB("DELETE FROM klijenti WHERE id=" + id);
                    model.removeRow(selectedIndex);
                    klijenti.remove(id);
                    //todo delete klijent from db, delete all predmeti of klijent from db and directory
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
                //todo launch kalendar window
            }
        });
    }

    public static void add(Object[] row) {
        maxID++;
        String id = String.valueOf(maxID);
        row[0] = id;
        //todo insert row in klijenti db table
        //Main.executeDB("INSERT INTO klijenti..." + id);
        model.addRow(row);
        table.setModel(model);
        klijenti.add(id);
    }

    public static void update(Object[] row) {
        String id = String.valueOf(row[0]);
        //todo update row in klijenti db table
        //Main.executeDB(UPDATE klijenti SET... WHERE id=" + id);
        refresh();
    }

    public static void refresh() {
        //todo get rows from klijenti table in db and populate table
        //todo get maxID
        //todo popualte klijenti arraylist
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
