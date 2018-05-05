package ui_resource;

import javax.swing.*;
import java.awt.*;

public class AddMedRecPanel extends JPanel {
    private JTextField txtMedNameMDB;
    private JTextField txtMedManuNameMDB;
    private JTextField txtMedGenNameMDB;
    private JTextField txtMedDPPMDB;
    private JTextField txtMedTempMDB;
    private JTextField txtMedSTMDB;

    /**
     * Create the panel.
     */
    public AddMedRecPanel() {
        setLayout(null);

        JLabel lblPrompt = new JLabel("<html>Please fill out the following form to register into the system.<br><center>The item marked with '*' are mandatory.</html></center>");
        lblPrompt.setBounds(49, 0, 349, 48);
        lblPrompt.setFont(new Font("Tahoma", Font.PLAIN, 13));
        lblPrompt.setVisible(true);
        add(lblPrompt);

        JLabel lblMedicineName = new JLabel("Medicine Name:*");
        lblMedicineName.setFont(new Font("Tahoma", Font.PLAIN, 13));
        lblMedicineName.setBounds(10, 48, 100, 31);
        add(lblMedicineName);

        JLabel lblManufacturer = new JLabel("Manufacturer:*");
        lblManufacturer.setFont(new Font("Tahoma", Font.PLAIN, 13));
        lblManufacturer.setBounds(10, 92, 100, 31);
        add(lblManufacturer);

        JLabel lblGenName = new JLabel("Generic Name:*");
        lblGenName.setFont(new Font("Tahoma", Font.PLAIN, 13));
        lblGenName.setBounds(10, 134, 100, 31);
        add(lblGenName);

        JLabel lblDosePerDay = new JLabel("Dose Per Day:");
        lblDosePerDay.setFont(new Font("Tahoma", Font.PLAIN, 13));
        lblDosePerDay.setBounds(10, 176, 100, 31);
        add(lblDosePerDay);

        JLabel lblQuantity = new JLabel("Quantity:");
        lblQuantity.setFont(new Font("Tahoma", Font.PLAIN, 13));
        lblQuantity.setBounds(10, 218, 121, 31);
        add(lblQuantity);

        JLabel lblStorageTemperature = new JLabel("Storage Temperature:");
        lblStorageTemperature.setFont(new Font("Tahoma", Font.PLAIN, 13));
        lblStorageTemperature.setBounds(10, 260, 139, 31);
        add(lblStorageTemperature);
    }
}
