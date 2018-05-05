package ui_resource;

import modules.Alarm;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AlarmPanel extends JPanel {

    SimpleDateFormat frmt;
    private JLabel lblName;
    private JLabel lblDesc;
    private JLabel lblTime;
    private JLabel lblPic;

    public AlarmPanel() {

        frmt = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");

        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));

        ImageIcon mainIcon = new ImageIcon(getClass().getResource("/resources/candy_clock_48.png"));
        lblPic = new JLabel();
        lblPic.setIcon(mainIcon);
        lblPic.setHorizontalAlignment(SwingConstants.CENTER);
        lblPic.setVerticalAlignment(SwingConstants.CENTER);
        add(lblPic);
        add(Box.createHorizontalStrut(15));

        JPanel pnlinfo = new JPanel();
        pnlinfo.setOpaque(false);
        pnlinfo.setLayout(new BoxLayout(pnlinfo, BoxLayout.PAGE_AXIS));

        Font fname = new Font("Arial", Font.BOLD, 16);
        lblName = new JLabel();
        lblName.setFont(fname);
        lblName.setOpaque(false);

        lblDesc = new JLabel();
        lblDesc.setOpaque(false);

        lblTime = new JLabel();
        lblTime.setOpaque(false);

        pnlinfo.add(lblName);
        pnlinfo.add(lblTime);
        pnlinfo.add(lblDesc);

        add(pnlinfo);
    }


    public void setAlarmShowData(Alarm alm) {

        lblName.setText(alm.getAlarmName());
        lblDesc.setText(alm.getAlarmDescription());

        String almTime = frmt.format(new Date(alm.getTime()));
        lblTime.setText(almTime);

    }

    public final void setColorAsSelected(Color selColor) {
        //txtMessage.setForeground(Color.white);
        setBackground(selColor);
        lblName.setForeground(Color.white);
        lblDesc.setForeground(Color.white);
        lblTime.setForeground(Color.white);
    }

    public final void setColorAsNotSelected() {
        //txtMessage.setForeground(Color.black);
        setBackground(null);
        lblName.setForeground(null);
        lblDesc.setForeground(null);
        lblTime.setForeground(null);
    }
}
