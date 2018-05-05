package ui_resource;

import modules.Alarm;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class ReminderViewer {

    AlarmListModel mdl;
    private JFrame frame;
    private JPanel container;
    private JList alarmList;
    private JButton btnReload;
    private JButton btnAdd;
    private JButton btnRemove;
    private JButton btnOK;
    private JButton btnCancel;

    public ReminderViewer(List<Alarm> lst) {

        container = new JPanel();
        container.setLayout(new BorderLayout());
        container.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        Font t_fnt = new Font("Arial", Font.BOLD, 18);
        JLabel lblHeader = new JLabel("Reminder Controller");
        lblHeader.setHorizontalTextPosition(JLabel.RIGHT);
        lblHeader.setFont(t_fnt);
        lblHeader.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        container.add(lblHeader, BorderLayout.PAGE_START);

        JPanel pnllstop = new JPanel();
        pnllstop.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        pnllstop.setLayout(new BoxLayout(pnllstop, BoxLayout.LINE_AXIS));

        btnReload = new JButton("Reload");
        btnReload.setFocusPainted(false);
        pnllstop.add(btnReload);
        pnllstop.add(Box.createHorizontalGlue());
        btnAdd = new JButton("Add Alarm");
        btnAdd.setFocusPainted(false);
        btnAdd.addMouseListener(new AlarmCommands(null));
        pnllstop.add(btnAdd);
        pnllstop.add(Box.createHorizontalStrut(5));
        btnRemove = new JButton("Remove Alarm");
        btnRemove.setFocusPainted(false);
        btnRemove.addMouseListener(new AlarmCommands(null));
        pnllstop.add(btnRemove);

        JPanel pnllst = new JPanel(new BorderLayout());

        mdl = new AlarmListModel(lst);

        alarmList = new JList(mdl);
        alarmList.setCellRenderer(new AlarmCellRenderer());
        alarmList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        alarmList.setBorder(null);
        JScrollPane scr = new JScrollPane(alarmList);
        scr.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));

        pnllst.add(scr, BorderLayout.CENTER);
        pnllst.add(pnllstop, BorderLayout.PAGE_END);
        container.add(pnllst, BorderLayout.CENTER);

        JPanel pnlcmd = new JPanel();
        pnlcmd.setLayout(new BoxLayout(pnlcmd, BoxLayout.LINE_AXIS));
        pnlcmd.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        pnlcmd.add(Box.createHorizontalGlue());
        btnOK = new JButton("OK");
        btnOK.setFocusPainted(false);
        btnOK.addMouseListener(new DisposeWindow());
        btnCancel = new JButton("Cancel");
        btnCancel.setFocusPainted(false);
        btnCancel.addMouseListener(new DisposeWindow());
        pnlcmd.add(btnOK);
        pnlcmd.add(Box.createHorizontalStrut(5));
        pnlcmd.add(btnCancel);
        container.add(pnlcmd, BorderLayout.PAGE_END);

    }

    public void setAlarmData(List<Alarm> list) {


    }

    public void show() {
        frame = new JFrame("Reminder");
        frame.setContentPane(container);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setMinimumSize(new Dimension(300, 250));
        frame.setPreferredSize(new Dimension(500, 450));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void addAlarmListUpdateGUI(Alarm arm) {
        int index = mdl.addAlarm(arm);
        alarmList.setSelectedIndex(index);
        alarmList.ensureIndexIsVisible(index);
    }

    public void removeAlarmListUpdateGUI(int loc) {
        mdl.removeAlarm(loc);
    }

    private class AlarmCommands extends MouseAdapter {
        private JFrame parent;

        public AlarmCommands(JFrame m) {
            parent = m;
        }

        public void mouseClicked(MouseEvent evt) {

            Object src = evt.getSource();

            if (src == btnAdd) {
                AlarmEditor edit = new AlarmEditor(ReminderViewer.this);
                edit.show(parent);
            } else if (src == btnRemove) {
                int indx = alarmList.getSelectedIndex();
                if (indx < 0) return;
                removeAlarmListUpdateGUI(indx);
            }

        }
    }

    private class DisposeWindow extends MouseAdapter {

        public void mouseClicked(MouseEvent evt) {
            frame.dispose();
        }
    }

}
