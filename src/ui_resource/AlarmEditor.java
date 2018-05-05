package ui_resource;

import modules.Alarm;
import modules.AlarmManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import java.util.Date;

public class AlarmEditor {

    private JDialog dialog;
    private JPanel container;

    private JTextField txtAlarmName;
    private JTextField txtAlarmDesc;
    private JSpinner spnDate;

    private JButton btnOK;
    private JButton btnCancel;

    private ReminderViewer parent;

    public AlarmEditor(ReminderViewer parent) {

        container = new JPanel();
        container.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        container.setLayout(new BorderLayout());

        JPanel pnledit = new JPanel();
        pnledit.setLayout(new GridBagLayout());

        JLabel lbl1 = new JLabel("Alarm Name:");
        GridBagConstraints gd = new GridBagConstraints();
        gd.gridx = 0;
        gd.gridy = 0;
        gd.gridwidth = gd.gridheight = 1;
        gd.fill = GridBagConstraints.BOTH;
        gd.insets = new Insets(10, 10, 10, 10);
        pnledit.add(lbl1, gd);

        txtAlarmName = new JTextField();
        gd = new GridBagConstraints();
        gd.gridx = 1;
        gd.gridy = 0;
        gd.fill = GridBagConstraints.HORIZONTAL;
        gd.weightx = 1.0f;
        gd.weighty = 0.0f;
        gd.fill = GridBagConstraints.BOTH;
        pnledit.add(txtAlarmName, gd);

        JLabel lbl2 = new JLabel("Alarm Time:");
        gd = new GridBagConstraints();
        gd.gridx = 0;
        gd.gridy = 1;
        gd.gridwidth = gd.gridheight = 1;
        gd.fill = GridBagConstraints.BOTH;
        gd.insets = new Insets(10, 10, 10, 10);
        pnledit.add(lbl2, gd);

        SpinnerDateModel dateModel = new SpinnerDateModel();
        spnDate = new JSpinner(dateModel);
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(spnDate, "dd-MMM-yyyy hh:mm:ss a");
        spnDate.setEditor(dateEditor);
        spnDate.setValue(new Date());
        gd = new GridBagConstraints();
        gd.gridx = 1;
        gd.gridy = 1;
        gd.weightx = 1.0f;
        gd.weighty = 0.0f;
        gd.fill = GridBagConstraints.BOTH;
        pnledit.add(spnDate, gd);

        JLabel lbl3 = new JLabel("User Name:");
        gd = new GridBagConstraints();
        gd.gridx = 0;
        gd.gridy = 2;
        gd.gridwidth = gd.gridheight = 1;
        gd.insets = new Insets(10, 10, 10, 10);
        pnledit.add(lbl3, gd);

        txtAlarmDesc = new JTextField();
        gd = new GridBagConstraints();
        gd.gridx = 1;
        gd.gridy = 2;
        gd.fill = GridBagConstraints.BOTH;
        gd.weightx = 1.0f;
        gd.weighty = 0.0f;
        pnledit.add(txtAlarmDesc, gd);

        JLabel lb = new JLabel();
        gd = new GridBagConstraints();
        gd.gridx = 0;
        gd.gridy = 3;
        gd.gridwidth = 2;
        gd.weightx = gd.weighty = 1.0f;
        pnledit.add(lb, gd);

        container.add(pnledit, BorderLayout.CENTER);

        JPanel pnlcmd = new JPanel();
        pnlcmd.setLayout(new FlowLayout());

        btnOK = new JButton("OK");
        btnOK.addMouseListener(new OKHandler());
        pnlcmd.add(btnOK);

        btnCancel = new JButton("Cancel");
        btnCancel.addMouseListener(new CancelHandler());
        pnlcmd.add(btnCancel);

        container.add(pnlcmd, BorderLayout.PAGE_END);
        this.parent = parent;
    }

    public void show(JFrame parent) {


        dialog = new JDialog(parent);
        dialog.setContentPane(container);
        dialog.setTitle("Add a new Alarm");
        dialog.setPreferredSize(new Dimension(350, 250));
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);

    }

    private class OKHandler extends MouseAdapter {


        public void mouseClicked(MouseEvent evt) {

            Alarm alm = new Alarm();

            try {
                spnDate.commitEdit();
            } catch (ParseException ex) {
                ex.printStackTrace();
            }

            Date dt = (Date) spnDate.getValue();

            alm.setalarmName(txtAlarmName.getText());
            alm.setalarmDescription(txtAlarmDesc.getText());
            alm.setTime(dt.getTime());

            parent.addAlarmListUpdateGUI(alm);
            AlarmManager am = AlarmManager.getInstance();
            am.sendAlarmsToDispatcher();

            dialog.dispose();
        }
    }

    private class CancelHandler extends MouseAdapter {
        public void mouseClicked(MouseEvent evt) {
            dialog.dispose();
        }
    }

}