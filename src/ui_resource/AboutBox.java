package ui_resource;

import javax.swing.*;
import java.awt.*;


public class AboutBox extends JPanel {

    /**
     * Create the panel.
     */
    public AboutBox() {
        setLayout(null);

        final JLabel lblName = new JLabel("<html><b><p align=\"right\">Helping Hand v1.0 </b></p></html>");
        lblName.setHorizontalAlignment(SwingConstants.RIGHT);
        lblName.setFont(new Font("Tahoma", Font.PLAIN, 29));
        lblName.setBounds(184, 11, 352, 34);
        add(lblName);

        final JLabel lblTagLine = new JLabel("Your handbook and companion for medication management ");
        lblTagLine.setHorizontalAlignment(SwingConstants.RIGHT);
        lblTagLine.setFont(new Font("Tahoma", Font.ITALIC, 13));
        lblTagLine.setBounds(183, 40, 353, 34);
        add(lblTagLine);

        final JLabel lblTeamName = new JLabel("<html>Developed by <b>Team Hexa</b></html>");
        lblTeamName.setHorizontalAlignment(SwingConstants.RIGHT);
        lblTeamName.setFont(new Font("Tahoma", Font.ITALIC, 13));
        lblTeamName.setBounds(183, 69, 353, 34);
        add(lblTeamName);

        final JLabel lblMembers = new JLabel("<html><p align = \"right\"><b>Members</b><br>Shamah Mahbub Zoha - 11301007 - shamah1992@gmail.com<br>Ashis Kumar Das - 11301002 - akd.bracu@gmail.com<br>Zarin Irtiza - 11101070 - zarinirtiza@gmail.com<br>Irin Nahar - 10201026 - irincse.bu@gmail.com<br>Abdullah Al Samir - 13101293 - samir1244@gmail.com<br>Shameer Azmi - 15141014 - a.sameer91@gmail.com</p></html>");
        lblMembers.setHorizontalAlignment(SwingConstants.RIGHT);
        lblMembers.setFont(new Font("Tahoma", Font.PLAIN, 13));
        lblMembers.setBounds(159, 116, 377, 126);
        add(lblMembers);

        final JLabel lblDisclaimer = new JLabel("<html><i><p align = \"right\">This application uses Xerial SQLite JDBC Driver, developed by Taro L. Saito, and is compliant with the Apache License version 2.0 that it follows. No code from the plugin was modified, but it was extensively used to connect to the database in use. All rights and credits of the source code of the plugins go to where applicable.</i></align></html>");
        lblDisclaimer.setHorizontalAlignment(SwingConstants.RIGHT);
        lblDisclaimer.setBounds(10, 276, 526, 68);
        add(lblDisclaimer);

        final JButton btnEgg = new JButton("");
        btnEgg.setIcon(new ImageIcon(AboutBox.class.getResource("/resources/HHLogoLarge.png")));
        btnEgg.setBounds(10, 40, 163, 159);
        btnEgg.setBorderPainted(false);
        btnEgg.setContentAreaFilled(false);
        btnEgg.setFocusPainted(false);
        btnEgg.setOpaque(false);
        add(btnEgg);
    }
}
