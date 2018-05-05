package ui_resource;

import javax.swing.*;
import java.awt.*;

public class AddPtntRecPanel extends JPanel {

    /**
     * Create the panel.
     */
    public AddPtntRecPanel() {
        setLayout(null);

        JLabel lblPrompt = new JLabel("<html>Please fill out the following form to register into the system.<br><center>The item marked with '*' are mandatory.</html></center>");
        lblPrompt.setBounds(47, 5, 369, 32);
        lblPrompt.setFont(new Font("Tahoma", Font.PLAIN, 13));
        lblPrompt.setVisible(true);
        add(lblPrompt);

        JLabel lblMedicineName = new JLabel("Medicine Name:*");
        lblMedicineName.setFont(new Font("Tahoma", Font.PLAIN, 13));
        lblMedicineName.setBounds(10, 48, 100, 31);
        add(lblMedicineName);

        JLabel lblComplication = new JLabel("Complication:");
        lblComplication.setFont(new Font("Tahoma", Font.PLAIN, 13));
        lblComplication.setBounds(10, 134, 100, 31);
        add(lblComplication);

        JLabel lblStartDateAnd = new JLabel("Start Date and Time:");
        lblStartDateAnd.setFont(new Font("Tahoma", Font.PLAIN, 13));
        lblStartDateAnd.setBounds(10, 176, 121, 31);
        add(lblStartDateAnd);

        JLabel lblDosePerDay = new JLabel("Dose Per Day:");
        lblDosePerDay.setFont(new Font("Tahoma", Font.PLAIN, 13));
        lblDosePerDay.setBounds(10, 260, 100, 31);
        add(lblDosePerDay);

        JLabel lblQuantity = new JLabel("Quantity:*");
        lblQuantity.setFont(new Font("Tahoma", Font.PLAIN, 13));
        lblQuantity.setBounds(10, 92, 68, 31);
        add(lblQuantity);

        JLabel lblEndDateAnd = new JLabel("End Date and Time:");
        lblEndDateAnd.setFont(new Font("Tahoma", Font.PLAIN, 13));
        lblEndDateAnd.setBounds(10, 218, 121, 31);
        add(lblEndDateAnd);


    }
}
