package ui_resource;

import javax.swing.*;
import java.awt.*;


public class UsrAuthPanel extends JPanel {

    /**
     * Create the panel.
     */
    public UsrAuthPanel() {
        setLayout(null);

        JLabel lblPleaseEnterUser = new JLabel("Please enter user name and password to continue");
        lblPleaseEnterUser.setBounds(46, 11, 297, 16);
        lblPleaseEnterUser.setFont(new Font("Tahoma", Font.PLAIN, 13));
        add(lblPleaseEnterUser);

        JLabel lblUserName = new JLabel("User Name:");
        lblUserName.setFont(new Font("Tahoma", Font.PLAIN, 13));
        lblUserName.setBounds(10, 52, 79, 23);
        add(lblUserName);

        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 13));
        lblPassword.setBounds(10, 86, 79, 23);
        add(lblPassword);
    }

}
