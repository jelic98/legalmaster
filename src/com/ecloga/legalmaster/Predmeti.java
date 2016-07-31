package com.ecloga.legalmaster;

import org.apache.commons.io.FileUtils;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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
    private int width = (int) (screenSize.getWidth() * 0.75);
    private int height = (int) (screenSize.getHeight() * 0.75);
    private JPanel panel, tablePanel, menuPanel;
    private DefaultTableModel model;
    private JTable table;
    private JScrollPane scrollPane;
    private JButton bDodaj, bUkloni, bIzmeni, bTok, bCenovnik, bMedija, bTrazi;
    private JTextField tfTrazi;
    private ArrayList<String> predmeti = new ArrayList<String>();
    private String klijentIme;
    private int maxID = 0;
    public boolean infoShown = false;
    private int klijentId;
    private JFrame frame = new JFrame();
    public ArrayList<String> tokShown = new ArrayList<String>();
    public ArrayList<String> cenovnikShown = new ArrayList<String>();
    private boolean searchShown = false;

    public Predmeti(String klijentIme) {
        this.klijentIme = klijentIme;

        panel = new JPanel();
        tablePanel = new JPanel();
        menuPanel = new JPanel();

        tfTrazi = new JTextField();
        tfTrazi.setColumns(10);

        model = new DefaultTableModel();

        table = new JTable(model) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        String[] columns = {"ID", "Sifra", "Ime", "Suprotna strana", "Sud", "Sudija", "Napomena", "Placeno"};

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

                if(column == 0) {
                    this.setHorizontalAlignment(JLabel.CENTER);
                }else {
                    this.setHorizontalAlignment(JLabel.LEFT);
                }

                if(column < 2) {
                    c.setFont(c.getFont().deriveFont(Font.BOLD));
                } else {
                    c.setFont(c.getFont().deriveFont(Font.PLAIN));
                }

                table.getColumnModel().getColumn(0).setCellRenderer(this);

                return c;
            }
        });

        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(0).setMaxWidth(75);
        columnModel.getColumn(1).setPreferredWidth(100);
        columnModel.getColumn(2).setPreferredWidth(200);
        columnModel.getColumn(3).setPreferredWidth(200);
        columnModel.getColumn(4).setPreferredWidth(200);
        columnModel.getColumn(5).setPreferredWidth(200);
        columnModel.getColumn(6).setPreferredWidth(300);
        columnModel.getColumn(7).setMinWidth(100);

        refresh();

        scrollPane = new JScrollPane(table);

        bDodaj = new JButton("Dodaj");
        bDodaj.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!infoShown) {
                    infoShown = true;

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

                        Main.executeDB("DELETE FROM tok WHERE predmet=" + id);
                        Main.executeDB("DELETE FROM cenovnik WHERE predmet=" + id);

                        model.removeRow(selectedIndex);
                        predmeti.remove(id);

                        File media = new File(Main.directoryName + File.separator + "media" + File.separator + id);

                        try {
                            FileUtils.deleteDirectory(media);
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }

                        Main.executeDB("DELETE FROM predmeti WHERE id=" + id);
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

                    infoShown = true;

                    PredmetInfo izmeni = new PredmetInfo(Predmeti.this, info);
                    izmeni.show();
                }else {
                    JOptionPane.showMessageDialog(null, "Predmet nije selektovan", "Poruka", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        bTok = new JButton("Tok");
        bTok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = table.getSelectedRow();

                if(selectedIndex != -1) {
                    String sifra = String.valueOf(table.getValueAt(selectedIndex, 1));

                    if(!tokShown.contains(sifra)) {
                        Tok tok = new Tok(Predmeti.this, sifra);
                        tok.show();

                        tokShown.add(sifra);
                    }else {
                        JOptionPane.showMessageDialog(null, "Tok ovog predmeta je prikazan", "Poruka", JOptionPane.INFORMATION_MESSAGE);
                    }
                }else {
                    JOptionPane.showMessageDialog(null, "Predmet nije selektovan", "Poruka", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        bCenovnik = new JButton("Cenovnik");
        bCenovnik.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = table.getSelectedRow();

                if(selectedIndex != -1) {
                    String sifra = String.valueOf(table.getValueAt(selectedIndex, 1));

                    if(!cenovnikShown.contains(sifra)) {
                        Cenovnik cenovnik = new Cenovnik(Predmeti.this, sifra);
                        cenovnik.show();

                        cenovnikShown.add(sifra);
                    }else {
                        JOptionPane.showMessageDialog(null, "Cenovnik ovog predmeta je prikazan", "Poruka", JOptionPane.INFORMATION_MESSAGE);
                    }
                }else {
                    JOptionPane.showMessageDialog(null, "Predmet nije selektovan", "Poruka", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        bMedija = new JButton("Medija");
        bMedija.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = table.getSelectedRow();

                if(selectedIndex != -1) {
                    String id = String.valueOf(table.getValueAt(selectedIndex, 0));

                    openMedia(Main.directoryName + File.separator + "media" + File.separator + id);
                }else {
                    JOptionPane.showMessageDialog(null, "Predmet nije selektovan", "Poruka", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        bTrazi = new JButton("Trazi");
        bTrazi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(tfTrazi.getText().isEmpty() || tfTrazi.getText() == null) {
                    JOptionPane.showMessageDialog(null, "Kriterijum pretrage je neophodan", "Poruka", JOptionPane.INFORMATION_MESSAGE);
                }else {
                    if(!searchShown) {
                        search(tfTrazi.getText());

                        bTrazi.setText("Nazad");

                        tfTrazi.setVisible(false);

                        frame.setTitle("Pretraga");

                        searchShown = true;
                    }else {
                        refresh();

                        bTrazi.setText("Trazi");

                        tfTrazi.setVisible(true);

                        frame.setTitle(klijentIme + " - Predmeti");

                        searchShown = false;
                    }
                }
            }
        });

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                Klijenti.predmetiShown.remove(klijentIme);
            }
        });
    }

    private void openMedia(String dir) {
        try {
            Desktop.getDesktop().open(new File(dir));
        }catch(IOException e) {
            e.printStackTrace();
        }
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
        Main.executeDB("INSERT INTO predmeti VALUES (" + row[0] + ", '" + row[1] + "', '" + row[2] + "', '"  + row[3] + "', '"  + row[4] + "', '"  + row[5] + "', '"  + row[6] + "', " + getId() + ")");
        addRow(row);
    }

    private void addRow(Object[] row) {
        model.addRow(row);
        table.setModel(model);
        predmeti.add(String.valueOf(row[0]));
    }

    public void update(Object[] row) {
        String id = String.valueOf(row[0]);
        Main.executeDB("UPDATE predmeti SET sifra='" + row[1] + "', ime='" + row[2] + "', strana='" + row[3] + "', sud='" + row[4] + "', sudija='" + row[5] + "', napomena='" + row[3] + "' WHERE id=" + id);
        refresh();
    }

    public void refresh() {
        model.setRowCount(0);
        table.setModel(model);

        ResultSet rs = null;

        String cmd = "SELECT * FROM predmeti";

        klijentId = getId();

        try {
            rs = Main.s.executeQuery(cmd);

            while(rs.next()){
                if(rs.getInt("klijent") == klijentId) {
                    addRow(new Object[] {rs.getInt("id"), rs.getString("sifra"), rs.getString("ime"), rs.getString("strana"), rs.getString("sud"), rs.getString("sudija"), rs.getString("napomena"), "0/0"});
                }

                if(rs.getInt("id") > maxID) {
                    maxID = rs.getInt("id");
                }
            }

            rs.close();
        }catch(SQLException e) {
            e.printStackTrace();
        }

        for(int i = 0; i < table.getRowCount(); i++) {
            int cena = 0;
            int placeno = 0;

            String id = String.valueOf(table.getValueAt(i, 0));

            cmd = "SELECT * FROM cenovnik WHERE predmet=" + id;

            try {
                rs = Main.s.executeQuery(cmd);

                while(rs.next()) {
                    cena += rs.getInt("cena");
                    placeno += rs.getInt("placeno");
                }

                table.setValueAt(placeno + "/" + cena, i, 7);

                rs.close();
            }catch(SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void search(String keyword) {
        model.setRowCount(0);
        table.setModel(model);

        int counter = 0;

        String cmd = "SELECT * FROM predmeti WHERE sifra LIKE '%" + keyword + "%'";

        ResultSet rs = null;

        try {
            rs = Main.s.executeQuery(cmd);

            while(rs.next()){
                addRow(new Object[] {rs.getInt("id"), rs.getString("sifra"), rs.getString("ime"), rs.getString("strana"), rs.getString("sud"), rs.getString("sudija"), rs.getString("napomena"), "0/0"});

                counter++;
            }

            rs.close();
        }catch(SQLException e) {
            e.printStackTrace();
        }

        for(int i = 0; i < table.getRowCount(); i++) {
            int cena = 0;
            int placeno = 0;

            String id = String.valueOf(table.getValueAt(i, 0));

            cmd = "SELECT * FROM cenovnik WHERE predmet=" + id;

            try {
                rs = Main.s.executeQuery(cmd);

                while(rs.next()) {
                    cena += rs.getInt("cena");
                    placeno += rs.getInt("placeno");
                }

                table.setValueAt(placeno + "/" + cena, i, 7);

                rs.close();
            }catch(SQLException e) {
                e.printStackTrace();
            }
        }

        if(counter == 0) {
            JOptionPane.showMessageDialog(null, "Nema rezultata pretrage", "Poruka", JOptionPane.INFORMATION_MESSAGE);
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
        menuPanel.add(bCenovnik);
        menuPanel.add(bMedija);
        menuPanel.add(tfTrazi);
        menuPanel.add(bTrazi);

        tablePanel.add(scrollPane);

        panel.add(menuPanel);
        panel.add(tablePanel);

        frame.setVisible(true);
        frame.setAlwaysOnTop(true);
        frame.toFront();
        frame.requestFocus();
        frame.setAlwaysOnTop(false);

        frame.setTitle(klijentIme + " - Predmeti");
        frame.setSize(new Dimension(width, height));
        frame.setLocation(screenSize.width / 2 - width / 2,screenSize.height / 2 - height / 2);
        frame.setContentPane(panel);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);
    }
}
