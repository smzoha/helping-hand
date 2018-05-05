package ui_resource;

import modules.Alarm;

import javax.sound.sampled.Clip;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AlarmPopup {

    SimpleDateFormat frmt;
    private JDialog dialog;
    private JPanel container;
    private JButton btnClose;
    private Clip audio;
    private JLabel lblName;
    private JLabel lblTime;
    private JLabel lblDesc;

    public AlarmPopup() {

        frmt = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
        Font f = new Font("Arial", Font.BOLD, 28);
        Font f_small = f.deriveFont(23f);

        container = new JPanel();
        container.setLayout(new BorderLayout(0, 10));
        container.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel bigpnl = new JPanel();
        bigpnl.setLayout(new BoxLayout(bigpnl, BoxLayout.PAGE_AXIS));
        bigpnl.setBackground(Color.white);
        bigpnl.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));

        ImageIcon icon = new ImageIcon(getClass().getResource("/resources/candy_clock_48.png"));
        JLabel lblicon = new JLabel(icon);
        lblicon.setOpaque(false);
        lblicon.setAlignmentX(Component.CENTER_ALIGNMENT);
        bigpnl.add(Box.createVerticalGlue());
        bigpnl.add(lblicon);

        bigpnl.add(Box.createVerticalStrut(10));

        lblName = new JLabel();
        lblName.setFont(f);
        lblName.setOpaque(false);
        lblName.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblName.setHorizontalTextPosition(JLabel.CENTER);
        bigpnl.add(lblName);

        lblTime = new JLabel();
        lblTime.setFont(f_small);
        lblTime.setOpaque(false);
        lblTime.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTime.setHorizontalTextPosition(JLabel.CENTER);
        bigpnl.add(Box.createVerticalStrut(6));
        bigpnl.add(lblTime);

        lblDesc = new JLabel();
        lblDesc.setFont(f_small);
        lblDesc.setOpaque(false);
        lblDesc.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblDesc.setHorizontalTextPosition(JLabel.CENTER);
        bigpnl.add(Box.createVerticalStrut(6));
        bigpnl.add(lblDesc);
        bigpnl.add(Box.createVerticalGlue());

        container.add(bigpnl, BorderLayout.CENTER);

        JPanel pnlcmd = new JPanel();
        pnlcmd.setLayout(new BoxLayout(pnlcmd, BoxLayout.LINE_AXIS));

        pnlcmd.add(Box.createHorizontalGlue());

        btnClose = new JButton("Close");
        btnClose.setFocusPainted(false);
        btnClose.addMouseListener(new OKClick());
        pnlcmd.add(btnClose);
        pnlcmd.add(Box.createHorizontalGlue());

        container.add(pnlcmd, BorderLayout.PAGE_END);

    }

    public void setAlarmTone(Clip p) {
        audio = p;
    }

    public void setAlarmInfo(Alarm alm) {

        lblName.setText(alm.getAlarmName());
        lblDesc.setText(alm.getAlarmDescription());

        String almTime = frmt.format(new Date(alm.getTime()));
        lblTime.setText(almTime);

    }

    public void show(JFrame parent) {

        dialog = new JDialog(parent);
        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        dialog.setTitle("Medicine Time - Alarm Ringing");
        dialog.setContentPane(container);
        dialog.setPreferredSize(new Dimension(380, 330));
        dialog.addWindowListener(new WindowHandler());
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
        audio.loop(Clip.LOOP_CONTINUOUSLY);
    }

    private class OKClick extends MouseAdapter {

        public void mouseClicked(MouseEvent evt) {

            audio.loop(0);
            dialog.dispose();
        }
    }

    private class WindowHandler extends WindowAdapter {

        public void windowClosing(WindowEvent evt) {

            audio.loop(0);
            dialog.dispose();
        }
    }

}
