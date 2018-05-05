package ui_resource;

import javax.swing.*;
import java.awt.*;

public class EditInfoPanel extends JPanel {

    /**
     * Create the panel.
     */
    public EditInfoPanel() {
        setLayout(null);

        JLabel lblName = new JLabel("Name:");
        lblName.setFont(new Font("Tahoma", Font.PLAIN, 13));
        lblName.setBounds(10, 11, 54, 34);
        add(lblName);

        JLabel lblAge = new JLabel("Age:");
        lblAge.setFont(new Font("Tahoma", Font.PLAIN, 13));
        lblAge.setBounds(10, 64, 37, 34);
        add(lblAge);

        JLabel lblGender = new JLabel("Gender:");
        lblGender.setFont(new Font("Tahoma", Font.PLAIN, 13));
        lblGender.setBounds(10, 114, 54, 34);
        add(lblGender);
    }
}
