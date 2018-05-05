package ui_resource;

import javax.swing.*;
import java.awt.*;


public class RegPanel extends JPanel {

    /**
     * Create the panel.
     */
    public RegPanel() {
        setLayout(null);

        JLabel lblPrompt = new JLabel("<html>Please fill out the following form to register into the system.<br><center>The item marked with '*' are mandatory.</html></center>");
        lblPrompt.setFont(new Font("Tahoma", Font.PLAIN, 13));
        lblPrompt.setBounds(29, 0, 346, 60);
        add(lblPrompt);

        JLabel lblUsrName = new JLabel("User Name:*");
        lblUsrName.setFont(new Font("Tahoma", Font.PLAIN, 13));
        lblUsrName.setBounds(10, 61, 83, 31);
        add(lblUsrName);

        JLabel lblPassword = new JLabel("Password:*");
        lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 13));
        lblPassword.setBounds(10, 102, 68, 31);
        add(lblPassword);

        JLabel lblName = new JLabel("Name:*");
        lblName.setFont(new Font("Tahoma", Font.PLAIN, 13));
        lblName.setBounds(10, 144, 83, 31);
        add(lblName);

        JLabel lblAge = new JLabel("Age:");
        lblAge.setFont(new Font("Tahoma", Font.PLAIN, 13));
        lblAge.setBounds(10, 186, 51, 31);
        add(lblAge);

        JLabel lblGender = new JLabel("Gender:");
        lblGender.setFont(new Font("Tahoma", Font.PLAIN, 13));
        lblGender.setBounds(10, 228, 51, 31);
        add(lblGender);
    }
}
