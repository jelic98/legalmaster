package com.ecloga.legalmaster;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
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

    public static void main(String[] args) {
        writeDirectory(directoryName);
        writeDirectory(directoryName + File.separator + "media");
        writeDirectory(directoryName + File.separator + "media" + File.separator + "temp");

        Image img = new ImageIcon(Main.class.getResource("/justicia.png")).getImage();

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = 500;
        int height = 500;

        JWindow window = new JWindow();

        window.getContentPane().add(new JLabel(new ImageIcon(img)));
        window.getContentPane().setBackground(Color.decode("#ecf0f1"));
        window.getContentPane().setLayout(new GridBagLayout());
        window.setSize(new Dimension(width, height));
        window.setLocation(screenSize.width / 2 - width / 2, screenSize.height / 2 - height / 2);

        //todo UNCOMMEN SNIPPIET LINE BELOW BEORE RELEASE
        //window.setVisible(true);

//        try {
//            Thread.sleep(2500);
//        }catch(InterruptedException e) {
//            e.printStackTrace();
//        }

        window.setVisible(false);
        window.dispose();

        if(fileExists(directoryName + File.separator + "config.lm")) {
            config.add("started");
            writeFile(directoryName + File.separator + "config.lm", config);
        }

        config = readFile(directoryName + File.separator + "config.lm");

        //todo DELETE LINE BELOW BEORE RELEASE
        config.add("activated");

        while(!config.contains("activated")) {
            activate();
        }

        if(!config.contains("dbcreated")) {
            createDB();

            config.add("dbcreated");
            writeFile(directoryName + File.separator + "config.lm", config);
        }

        if(config.contains("started") && config.contains("activated") && config.contains("dbcreated")) {
            Klijenti klijenti = new Klijenti();
            klijenti.show();
        }else {
            JOptionPane.showMessageDialog(null, "Program se ne moze pokrenuti", "Greska", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void createDB() {
        try {
            Class.forName(DRIVER);

            Connection c = DriverManager.getConnection(CREATE_URL);
            Statement s = c.createStatement();
            //todo create klijenti, predmeti and tok tables
            //s.execute("CREATE TABLE klijenti(ime varchar(50), broj varchar(50), email varchar(100))");
        }catch(ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public static void executeDB(String cmd) {
        try {
            Class.forName(DRIVER);

            Connection c = DriverManager.getConnection(EXECUTE_URL);
            Statement s = c.createStatement();
            s.execute(cmd);
        }catch(ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
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
        //todo activation process
    }
}
