package com.ecloga.legalmaster;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;


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
                    if(activate(tfKod.getText()).equals("true")) {
                        frame.dispose();
                        Main.config.add("activated");
                        Main.writeFile(Main.directoryName + File.separator + "config.lm", Main.config);

                        JOptionPane.showMessageDialog(null, "Program je uspesno aktiviran", "Poruka", JOptionPane.INFORMATION_MESSAGE);

                        Main.startUp();
                    }else {
                        JOptionPane.showMessageDialog(null, "Aktivacija programa nije uspela", "Poruka", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        });
    }

    public String activate(String kod) {
        String url = "http://www.ecloga.org/legal/activate.php?kod=" + kod;
        String response = "false";

        //todo make request and get response to url

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
