package com.ecloga.legalmaster;

//import statements
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

//create class
class DatePicker
{
    //define variables
    int month = java.util.Calendar.getInstance().get(java.util.Calendar.MONTH);
    int year = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    int width = (int) (screenSize.getWidth() * 0.5);
    int height = (int) (screenSize.getHeight() * 0.5);
    //create object of JLabel with alignment
    JLabel l = new JLabel("", JLabel.CENTER);
    //define variable
    String day = "";
    //declaration
    JDialog d;
    //create object of JButton
    JButton[] button = new JButton[49];

    public DatePicker()//create constructor
    {
        //create object
        d = new JDialog();
        //set modal true
        d.setModal(true);
        //define string
        String[] header = { "Nededlja", "Ponedeljak", "Utorak", "Sreda", "Cetvrtak", "Petak", "Subota" };
        //create JPanel object and set layout
        JPanel p1 = new JPanel(new GridLayout(7, 7));
        //set size
        p1.setPreferredSize(new Dimension(width, height));
        //for loop condition
        for (int x = 0; x < button.length; x++)
        {
            //define variable
            final int selection = x;
            //create object of JButton
            button[x] = new JButton();
            //set focus painted false
            button[x].setFocusPainted(false);
            //set background colour
            button[x].setBackground(Color.WHITE);
            //if loop condition
            if (x > 6)
                //add action listener
                button[x].addActionListener(new ActionListener()
                {
                    public void actionPerformed(ActionEvent ae)
                    {
                        day = button[selection].getActionCommand();
                        System.out.println(day + " " + month + " " + year);
                    }
                });
            if (x < 7)//if loop condition
            {
                button[x].setText(header[x]);
                //set fore ground colour
                button[x].setForeground(Color.BLUE);
            }
            p1.add(button[x]);//add button
        }
        //create JPanel object with grid layout
        JPanel p2 = new JPanel(new GridLayout(1, 3));

        //create object of button for previous month
        JButton previous = new JButton("<");
        //add action command
        previous.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent ae)
            {
                //decrement month by 1
                month--;
                //call method
                displayDate();
            }
        });
        p2.add(previous);//add button
        p2.add(l);//add label
        //create object of button for next month
        JButton next = new JButton(">");
        //add action command
        next.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent ae)
            {
                //increment month by 1
                month++;
                //call method
                displayDate();
            }
        });
        p2.add(next);// add next button
        //set border alignment
        d.add(p1, BorderLayout.CENTER);
        d.add(p2, BorderLayout.SOUTH);
        d.pack();
        //call method
        displayDate();
        //set visible true
        d.setVisible(true);
    }

    public void displayDate()
    {
        for (int x = 7; x < button.length; x++)//for loop
            button[x].setText("");//set text
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd-MM-yyyy");
        //create object of SimpleDateFormat
        java.util.Calendar cal = java.util.Calendar.getInstance();
        //create object of java.util.Calendar
        cal.set(year, month, 1); //set year, month and date
        //define variables
        int dayOfWeek = cal.get(java.util.Calendar.DAY_OF_WEEK);
        int daysInMonth = cal.getActualMaximum(java.util.Calendar.DAY_OF_MONTH);
        //condition
        for (int x = 6 + dayOfWeek, day = 1; day <= daysInMonth; x++, day++)
            //set text
            button[x].setText("" + day);
        l.setText(sdf.format(cal.getTime()));
        //set title
        d.setTitle("Kalendar");
    }

    public String setPickedDate()
    {
        //if condition
        if (day.equals(""))
            return day;
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd-MM-yyyy");
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.set(year, month, Integer.parseInt(day));
        return sdf.format(cal.getTime());
    }
}
