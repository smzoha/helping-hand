package ui_resource;

import javax.swing.*;
import java.awt.*;

public class EditPassPanel extends JPanel {

    /**
     * Create the panel.
     */
    public EditPassPanel() {
        setLayout(null);

        JLabel lblCurrPass = new JLabel("Current Password:");
        lblCurrPass.setFont(new Font("Tahoma", Font.PLAIN, 13));
        lblCurrPass.setBounds(10, 11, 113, 34);
        add(lblCurrPass);

        JLabel lblNewPassword = new JLabel("New Password:");
        lblNewPassword.setFont(new Font("Tahoma", Font.PLAIN, 13));
        lblNewPassword.setBounds(10, 56, 113, 34);
        add(lblNewPassword);

        JLabel lblConfirmNewPassword = new JLabel("Confirm New Password:");
        lblConfirmNewPassword.setFont(new Font("Tahoma", Font.PLAIN, 13));
        lblConfirmNewPassword.setBounds(10, 101, 138, 34);
        add(lblConfirmNewPassword);

    }
}
