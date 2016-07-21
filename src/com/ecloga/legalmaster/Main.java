package com.ecloga.legalmaster;

import javax.swing.*;
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
    public static final String URL  = "jdbc:derby:legalmaster;create=true";

    public static void main(String[] args) {
        writeDirectory(directoryName);
        writeDirectory(directoryName + File.separator + "predmeti");

        ArrayList<String> config = new ArrayList<String>();

        if(fileExists(directoryName + File.separator + "config.lm")) {
            config = readFile(directoryName + File.separator + "config.lm");

            if(!config.contains("dbcreated")) {
                createDatabase();
            }
        }else {
            createDatabase();

            config.add("dbcreated");
            writeFile(directoryName + File.separator + "config.lm", config);
        }

        Klijenti klijenti = new Klijenti();
        klijenti.show();
    }

    public static void createDatabase() {
        try {
            Class.forName(DRIVER);

            Connection c = DriverManager.getConnection(URL);
            Statement s = c.createStatement();
            s.execute("CREATE TABLE klijenti(ime varchar(50), broj varchar(50))");
        }catch(ClassNotFoundException e) {
            e.printStackTrace();
        }catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public static void writeDirectory(String name) {
        if(!Files.exists(Paths.get(name))) {
            try {
                File file = new File(name);
                boolean created = file.mkdir();

                if(!created) {
                    JOptionPane.showMessageDialog(null, "Directory could not be created", "Error", JOptionPane.ERROR_MESSAGE);
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
}
