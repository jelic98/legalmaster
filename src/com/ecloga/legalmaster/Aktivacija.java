package com.ecloga.legalmaster;

import org.apache.commons.io.IOUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;


public class Aktivacija {
    private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private int width = (int) (screenSize.getWidth() * 0.25);
    private int height = (int) (screenSize.getHeight() * 0.25);
    private JFrame frame = new JFrame();
    private JPanel panel;
    private JTextField tfKod;
    private JButton bAktiviraj;

    public Aktivacija() {
        bAktiviraj.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!tfKod.getText().isEmpty() && tfKod.getText() != null) {
                    if(hasInternetAccess("google.com")) {
                        if(activate(tfKod.getText()).equals("true")) {
                            frame.dispose();

                            JOptionPane.showMessageDialog(null, "Program je uspesno aktiviran", "Poruka", JOptionPane.INFORMATION_MESSAGE);

                            Main.config.add("activated");
                            Main.writeFile(Main.directoryName + File.separator + "config.lm", Main.config);
                            Main.startUp();
                        }else {
                            JOptionPane.showMessageDialog(null, "Aktivacija programa nije uspela", "Poruka", JOptionPane.INFORMATION_MESSAGE);
                        }
                    }else {
                        JOptionPane.showMessageDialog(null, "Aktivacija zahteva pristup internetu", "Poruka", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        });
    }

    public static boolean hasInternetAccess(String site) {
        Socket sock = new Socket();
        InetSocketAddress addr = new InetSocketAddress(site, 80);

        try {
            sock.connect(addr,3000);
            sock.close();
            return true;
        }catch(IOException e) {
            return false;
        }
    }

    public String activate(String kod) {
        URL url = null;
        String response = "false";

        try {
            url = new URL("https://lazarjelic.com/ecloga/projects/legal/activate.php?kod=" + kod);
            URLConnection con = url.openConnection();
            InputStream in = con.getInputStream();
            String encoding = con.getContentEncoding();
            encoding = encoding == null ? "UTF-8" : encoding;
            response = IOUtils.toString(in, encoding);
        }catch(IOException e) {
            e.printStackTrace();
        }

        return response;
    }

    public void show() {
        frame.setTitle("Aktivacija");
        frame.setSize(new Dimension(width, height));
        frame.setLocation(screenSize.width / 2 - width / 2,screenSize.height / 2 - height / 2);
        frame.setContentPane(panel);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);
    }
}
