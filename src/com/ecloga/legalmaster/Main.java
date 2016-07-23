package com.ecloga.legalmaster;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Main {
    public static String directoryName = System.getProperty("user.home") + File.separator + "LegalMaster";
    public static final String DRIVER  = "org.apache.derby.jdbc.EmbeddedDriver";
    public static final String CREATE_URL  = "jdbc:derby:legalmaster;create=true";
    public static final String EXECUTE_URL  = "jdbc:derby:legalmaster";
    public static ArrayList<String> config = new ArrayList<String>();
    public static Connection c;
    public static Statement s;

    public static void main(String[] args) {
        writeDirectory(directoryName);
        writeDirectory(directoryName + File.separator + "media");
        writeDirectory(directoryName + File.separator + "media" + File.separator + "temp");

        showSplashScreen();

        if(fileExists(directoryName + File.separator + "config.lm")) {
            config = readFile(directoryName + File.separator + "config.lm");
        }else {
            config.add("started");
            writeFile(directoryName + File.separator + "config.lm", config);
        }

        if(!config.contains("activated")) {
            activate();
        }else {
            startUp();
        }
    }

    public static void startUp() {
        if(!config.contains("dbcreated")) {
            createDB();

            config.add("dbcreated");
            writeFile(directoryName + File.separator + "config.lm", config);
        }

        createStatement(EXECUTE_URL);

        if(config.contains("started") && config.contains("activated") && config.contains("dbcreated")) {
            Klijenti klijenti = new Klijenti();
            klijenti.show();
        }else {
            JOptionPane.showMessageDialog(null, "Program se ne moze pokrenuti", "Greska", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void createStatement(String url) {
        try {
            c = DriverManager.getConnection(CREATE_URL);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            s = c.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void createDB() {
        try {
            Class.forName(DRIVER);

            createStatement(CREATE_URL);

            s.execute("CREATE TABLE klijenti (id INTEGER not NULL, ime VARCHAR(50), broj VARCHAR(25), email VARCHAR(50), adresa VARCHAR(100), PRIMARY KEY (id))");
            s.execute("CREATE TABLE predmeti (id INTEGER not NULL, sifra VARCHAR(25), ime VARCHAR(100), cena VARCHAR(10), placeno VARCHAR(10), klijent INTEGER, PRIMARY KEY (id))");
            s.execute("CREATE TABLE tok (id INTEGER not NULL, ime VARCHAR(100), datum VARCHAR(10), vreme VARCHAR(10), predmet INTEGER, PRIMARY KEY (id))");
        }catch(ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public static void executeDB(String cmd) {
        try {
            Class.forName(DRIVER);

            createStatement(EXECUTE_URL);

            s.execute(cmd);
        }catch(ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public static void showSplashScreen() {
        ImageIcon img = new ImageIcon(Main.class.getResource("/justicia.png"));

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = 250;
        int height = 250;

        JWindow window = new JWindow();

        window.getContentPane().add(new JLabel(img));
        window.getContentPane().setBackground(Color.decode("#ecf0f1"));
        window.getContentPane().setLayout(new GridBagLayout());
        window.setSize(new Dimension(width, height));
        window.setLocation(screenSize.width / 2 - width / 2, screenSize.height / 2 - height / 2);

        window.setVisible(true);

        try {
            Thread.sleep(3000);
        }catch(InterruptedException e) {
            e.printStackTrace();
        }

        window.setVisible(false);
        window.dispose();
    }

    public static void writeDirectory(String name) {
        if(!Files.exists(Paths.get(name))) {
            try {
                File file = new File(name);
                boolean created = file.mkdir();

                if(!created) {
                    JOptionPane.showMessageDialog(null, name.toUpperCase() + " direktorijum ne moze biti kreiran", "Greska", JOptionPane.ERROR_MESSAGE);
                }
            }catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void writeFile(String dir, ArrayList<String> content) {
        try {
            PrintWriter w = new PrintWriter(dir, "UTF-8");

            for(String line : content) {
                w.println(line);
            }

            w.close();
        }catch(FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<String> readFile(String dir) {
        ArrayList<String> content = new ArrayList<String>();

        try{
            FileInputStream fis = new FileInputStream(dir);
            DataInputStream in = new DataInputStream(fis);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line;
            int i = 0;

            while((line = br.readLine()) != null) {
                i++;
                content.add(line);
            }

            in.close();
        }catch (Exception e){
            e.printStackTrace();
        }

        return content;
    }

    public static boolean fileExists(String name) {
        File f = new File(name);

        if(f.exists() && !f.isDirectory()) {
            return true;
        }else {
            return false;
        }
    }

    public static void activate() {
        Aktivacija aktivacija = new Aktivacija();
        aktivacija.show();
    }
}
