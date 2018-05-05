import com.jtattoo.plaf.graphite.GraphiteLookAndFeel;
import modules.AlarmManager;
import ui_resource.*;

import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class HelpingHandGUI {

    public static int curr_user_id = -1;
    public static Clip alarmTone;
    private JFrame frmMain;
    private JTextField txtSearch;
    private JTable tblMedDB;
    private JTextField txtName;
    private JTextField txtSex;
    private JTextField txtAge;
    private JTable tblPatientCurrRec;

    /**
     * Create the application.
     */
    public HelpingHandGUI() {

        AlarmManager am = AlarmManager.getInstance();
        if (alarmTone != null) am.setAlarmRing(alarmTone);

        am.sendAlarmsToDispatcher();
        initialize();
    }

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {

                HelpingHandGUI window = null;
                LookAndFeel lnf = new GraphiteLookAndFeel();

                try {
                    UIManager.setLookAndFeel(lnf);
                    window = new HelpingHandGUI();
                    window.frmMain.setVisible(true);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                try {
                    URL alarmLoc = HelpingHandGUI.class.getResource("/resources/alarm_tone.au");
                    alarmTone = window.createAudioClip(alarmLoc);
                    System.out.println("Alarm Loaded: " + alarmLoc.getFile());
                    alarmTone.setLoopPoints(0, -1);
                    AlarmManager.getInstance().setAlarmRing(alarmTone);

                } catch (Exception ex) {

                    String msg = "Cannot find audio interface. System cannot ring alarms";
                    JOptionPane.showConfirmDialog(window.frmMain, msg, "Error",
                            JOptionPane.OK_OPTION, JOptionPane.ERROR_MESSAGE);

                }
            }
        });
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {

        final HelpingHandMain hhmain = new HelpingHandMain();
        hhmain.connectDB();

        final Object[] mDBMeta = {"ID", "Name", "Manufacturer", "Generic Name", "Dose Per Date", "Quantity", "Storage Temperature"};

        frmMain = new JFrame();
        frmMain.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frmMain.addWindowListener(new WindowHandler(frmMain));
        frmMain.setResizable(false);
        frmMain.setTitle("Helping Hand v1.0");
        frmMain.setBounds(100, 100, 795, 484);
        frmMain.getContentPane().setLayout(null);

        final JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.setFont(new Font("Tahoma", Font.BOLD, 13));
        tabbedPane.setBounds(0, 11, 784, 369);
        tabbedPane.setFocusable(false);
        frmMain.getContentPane().add(tabbedPane);
        frmMain.setLocationRelativeTo(null);

        ImageIcon mainIcon = new ImageIcon(getClass().getResource("/resources/HHLogoicon.png"));
        frmMain.setIconImage(mainIcon.getImage());

        JPanel panel_MedDB = new JPanel();
        tabbedPane.addTab("Medicine Database", null, panel_MedDB);
        panel_MedDB.setLayout(null);

        JScrollPane scrlMedDBTbl = new JScrollPane();
        scrlMedDBTbl.setBounds(10, 66, 604, 264);
        panel_MedDB.add(scrlMedDBTbl);

        tblMedDB = new JTable();
        scrlMedDBTbl.setViewportView(tblMedDB);
        final DefaultTableModel medDTM = (DefaultTableModel) tblMedDB.getModel();

        txtSearch = new JTextField();
        txtSearch.setBounds(132, 11, 334, 31);
        panel_MedDB.add(txtSearch);
        txtSearch.setColumns(10);

        JLabel lblSearch = new JLabel("Enter Search Term:");
        lblSearch.setFont(new Font("Tahoma", Font.PLAIN, 12));
        lblSearch.setBounds(10, 11, 119, 31);
        panel_MedDB.add(lblSearch);

        String[] searchOptions = {"Name", "Generic Name", "Manufacturer"};
        final JComboBox cmbSearchCriteria = new JComboBox(searchOptions);
        cmbSearchCriteria.setBounds(476, 11, 148, 31);
        panel_MedDB.add(cmbSearchCriteria);
        cmbSearchCriteria.setSelectedIndex(0);

        JButton btnSearch = new JButton("Search");
        btnSearch.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent arg0) {
                int sCriteria = cmbSearchCriteria.getSelectedIndex();
                String searchField, sVar;
                if (sCriteria == 0) {
                    sVar = txtSearch.getText();
                    searchField = "med_name";
                } else if (sCriteria == 1) {
                    sVar = txtSearch.getText();
                    searchField = "generic_name";
                } else {
                    sVar = txtSearch.getText();
                    searchField = "manufacturer";
                }

                Object[][] srchRslt = hhmain.searchMedData(searchField, sVar);
                medDTM.setRowCount(0);
                for (int i = 0; i < srchRslt.length; i++) {
                    medDTM.addRow(srchRslt[i]);
                }
            }
        });
        btnSearch.setFont(new Font("Tahoma", Font.PLAIN, 12));
        btnSearch.setBounds(634, 10, 133, 31);
        panel_MedDB.add(btnSearch);

        medDTM.setColumnIdentifiers(mDBMeta);
        Object[][] mDBData = hhmain.getMedDBData();
        for (int i = 0; i < mDBData.length; i++) {
            medDTM.addRow(mDBData[i]);
        }

        JSeparator separator_1 = new JSeparator();
        separator_1.setBackground(Color.DARK_GRAY);
        separator_1.setBounds(10, 53, 757, 2);
        panel_MedDB.add(separator_1);

        JButton btnResetDBMed = new JButton("Reset Database");
        btnResetDBMed.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent arg0) {
                medDTM.setRowCount(0);
                Object[][] refMDBData = hhmain.getMedDBData();
                for (int i = 0; i < refMDBData.length; i++) {
                    medDTM.addRow(refMDBData[i]);
                }
            }
        });
        btnResetDBMed.setFont(new Font("Tahoma", Font.PLAIN, 12));
        btnResetDBMed.setBounds(634, 108, 133, 31);
        panel_MedDB.add(btnResetDBMed);

        /************************************
         *  Add Medicine Record Frame
         ************************************/

        final JFrame addMedRecMBox = new JFrame("Medicine Record Add/Edit Panel");
        addMedRecMBox.setBounds(150, 150, 465, 420);
        addMedRecMBox.setResizable(false);

        final AddMedRecPanel addMedRecMDBPanel = new AddMedRecPanel();
        addMedRecMBox.getContentPane().add(addMedRecMDBPanel);

        final JTextField txtMedNameMDB = new JTextField();
        txtMedNameMDB.setBounds(130, 51, 295, 26);
        addMedRecMDBPanel.add(txtMedNameMDB);
        txtMedNameMDB.setColumns(10);

        final JTextField txtMedManuNameMDB = new JTextField();
        txtMedManuNameMDB.setColumns(10);
        txtMedManuNameMDB.setBounds(130, 92, 295, 26);
        addMedRecMDBPanel.add(txtMedManuNameMDB);

        final JTextField txtMedGenNameMDB = new JTextField();
        txtMedGenNameMDB.setColumns(10);
        txtMedGenNameMDB.setBounds(130, 134, 295, 26);
        addMedRecMDBPanel.add(txtMedGenNameMDB);

        final JTextField txtMedDPPMDB = new JTextField();
        txtMedDPPMDB.setColumns(10);
        txtMedDPPMDB.setBounds(130, 181, 86, 26);
        addMedRecMDBPanel.add(txtMedDPPMDB);

        final JTextField txtMedQuantityMDB = new JTextField();
        txtMedQuantityMDB.setColumns(10);
        txtMedQuantityMDB.setBounds(130, 223, 86, 26);
        addMedRecMDBPanel.add(txtMedQuantityMDB);

        final JTextField txtMedSTMDB = new JTextField();
        txtMedSTMDB.setColumns(10);
        txtMedSTMDB.setBounds(157, 263, 86, 26);
        addMedRecMDBPanel.add(txtMedSTMDB);

        final JButton btnAddRecordMDB = new JButton("Add Record");
        btnAddRecordMDB.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent arg0) {
                if ((txtMedNameMDB.getText().length() == 0) || (txtMedManuNameMDB.getText().length() == 0) || (txtMedGenNameMDB.getText().length() == 0)) {
                    JOptionPane.showMessageDialog(addMedRecMBox, "Please fill out the mandatory fields.", "Warning!", JOptionPane.WARNING_MESSAGE);
                } else {
                    String mName = txtMedNameMDB.getText();
                    String manu = txtMedManuNameMDB.getText();
                    String gmName = txtMedGenNameMDB.getText();
                    try {
                        int dpp = Integer.parseInt(txtMedDPPMDB.getText());
                        int quan = Integer.parseInt(txtMedQuantityMDB.getText());
                        int st = Integer.parseInt(txtMedSTMDB.getText());
                        hhmain.addMedToDB(mName, manu, gmName, dpp, quan, st);
                        JOptionPane.showMessageDialog(addMedRecMBox, "Medicine information added to database.");
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(addMedRecMBox, "Incorrect information provided.", "Warning!", JOptionPane.WARNING_MESSAGE);
                    }

                    medDTM.setRowCount(0);
                    Object[][] mData = hhmain.getMedDBData();
                    for (int i = 0; i < mData.length; i++) {
                        medDTM.addRow(mData[i]);
                    }

                    addMedRecMBox.dispose();
                }
            }
        });
        btnAddRecordMDB.setFont(new Font("Tahoma", Font.PLAIN, 13));
        btnAddRecordMDB.setBounds(30, 324, 168, 38);
        addMedRecMDBPanel.add(btnAddRecordMDB);


        final JButton btnEditRecordMDB = new JButton("Edit Record");
        btnEditRecordMDB.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent arg0) {
                String mName = txtMedNameMDB.getText();
                String manu = txtMedManuNameMDB.getText();
                String gmName = txtMedGenNameMDB.getText();
                try {
                    int dpp = Integer.parseInt(txtMedDPPMDB.getText());
                    int quan = Integer.parseInt(txtMedQuantityMDB.getText());
                    int st = Integer.parseInt(txtMedSTMDB.getText());

                    int mID = Integer.parseInt((String) medDTM.getValueAt(tblMedDB.getSelectedRow(), 0));

                    hhmain.editMed(mID, mName, manu, gmName, dpp, quan, st);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(addMedRecMBox, "Incorrect information provided.", "Warning!", JOptionPane.WARNING_MESSAGE);
                }

                medDTM.setRowCount(0);
                Object[][] mData = hhmain.getMedDBData();
                for (int i = 0; i < mData.length; i++) {
                    medDTM.addRow(mData[i]);
                }

                JOptionPane.showMessageDialog(addMedRecMBox, "Medicine information successfully edited!");

                addMedRecMBox.dispose();
            }
        });
        btnEditRecordMDB.setFont(new Font("Tahoma", Font.PLAIN, 13));
        btnEditRecordMDB.setBounds(30, 324, 168, 38);
        addMedRecMDBPanel.add(btnEditRecordMDB);

        final JButton btnCancelMDB = new JButton("Cancel");
        btnCancelMDB.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent arg0) {
                if (addMedRecMBox.isVisible()) {
                    addMedRecMBox.dispose();
                }
            }
        });
        btnCancelMDB.setFont(new Font("Tahoma", Font.PLAIN, 13));
        btnCancelMDB.setBounds(223, 324, 168, 38);
        addMedRecMDBPanel.add(btnCancelMDB);


        /********************************
         * END OF MED RECORD PANEL COMPONENT
         ********************************/

        final JButton btnAddRecordMed = new JButton("Add Record");

        btnAddRecordMed.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent arg0) {
                btnAddRecordMDB.setVisible(true);
                btnEditRecordMDB.setVisible(false);

                txtMedNameMDB.setText(null);
                txtMedManuNameMDB.setText(null);
                txtMedGenNameMDB.setText(null);
                txtMedDPPMDB.setText(null);
                txtMedQuantityMDB.setText(null);
                txtMedSTMDB.setText(null);

                addMedRecMBox.setVisible(true);
            }
        });
        btnAddRecordMed.setFont(new Font("Tahoma", Font.PLAIN, 12));
        btnAddRecordMed.setBounds(634, 152, 133, 31);
        panel_MedDB.add(btnAddRecordMed);

        final JButton btnEditRecordMed = new JButton("Edit Record");
        btnEditRecordMed.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent arg0) {
                btnAddRecordMDB.setVisible(false);
                btnEditRecordMDB.setVisible(true);

                int row_num = tblMedDB.getSelectedRow();

                if (row_num != -1) {
                    txtMedNameMDB.setText((String) medDTM.getValueAt(row_num, 1));
                    txtMedManuNameMDB.setText((String) medDTM.getValueAt(row_num, 2));
                    txtMedGenNameMDB.setText((String) medDTM.getValueAt(row_num, 3));
                    txtMedDPPMDB.setText((String) medDTM.getValueAt(row_num, 4));
                    txtMedQuantityMDB.setText((String) medDTM.getValueAt(row_num, 5));
                    txtMedSTMDB.setText((String) medDTM.getValueAt(row_num, 6));

                    addMedRecMBox.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(addMedRecMBox, "Select a row first in order to edit.", "Warning!", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        btnEditRecordMed.setFont(new Font("Tahoma", Font.PLAIN, 12));
        btnEditRecordMed.setBounds(634, 194, 133, 31);
        panel_MedDB.add(btnEditRecordMed);

        JButton btnDeleteRecord = new JButton("Delete Record");
        btnDeleteRecord.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent arg0) {
                int row_indx = tblMedDB.getSelectedRow();

                if (row_indx != -1) {
                    int mID = Integer.parseInt((String) medDTM.getValueAt(tblMedDB.getSelectedRow(), 0));

                    hhmain.delMedRec(mID);

                    medDTM.setRowCount(0);
                    Object[][] mData = hhmain.getMedDBData();
                    for (int i = 0; i < mData.length; i++) {
                        medDTM.addRow(mData[i]);
                    }

                    JOptionPane.showMessageDialog(addMedRecMBox, "Medicine information removed from database.");

                } else {
                    JOptionPane.showMessageDialog(addMedRecMBox, "Select a row first in order to delete.", "Warning!", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        btnDeleteRecord.setFont(new Font("Tahoma", Font.PLAIN, 12));
        btnDeleteRecord.setBounds(634, 235, 133, 31);
        panel_MedDB.add(btnDeleteRecord);

        /***************
         * Patient DB
         ***************/

        JPanel panel_PatientDB = new JPanel();
        tabbedPane.addTab("Patient Prescription", null, panel_PatientDB);
        panel_PatientDB.setLayout(null);

        final JLabel lblName = new JLabel("Name:");
        lblName.setEnabled(false);
        lblName.setFont(new Font("Tahoma", Font.PLAIN, 12));
        lblName.setBounds(10, 11, 46, 23);
        panel_PatientDB.add(lblName);

        txtName = new JTextField();
        txtName.setEnabled(false);
        txtName.setBackground(Color.WHITE);
        txtName.setEditable(false);
        txtName.setBounds(60, 11, 709, 25);
        panel_PatientDB.add(txtName);
        txtName.setColumns(10);

        final JLabel lblGender = new JLabel("Gender:");
        lblGender.setEnabled(false);
        lblGender.setFont(new Font("Tahoma", Font.PLAIN, 12));
        lblGender.setBounds(10, 45, 51, 25);
        panel_PatientDB.add(lblGender);

        txtSex = new JTextField();
        txtSex.setEnabled(false);
        txtSex.setBackground(Color.WHITE);
        txtSex.setEditable(false);
        txtSex.setBounds(60, 46, 139, 25);
        panel_PatientDB.add(txtSex);
        txtSex.setColumns(10);

        final JLabel lblAge = new JLabel("Age:");
        lblAge.setEnabled(false);
        lblAge.setFont(new Font("Tahoma", Font.PLAIN, 12));
        lblAge.setBounds(222, 47, 34, 20);
        panel_PatientDB.add(lblAge);

        txtAge = new JTextField();
        txtAge.setEnabled(false);
        txtAge.setBackground(Color.WHITE);
        txtAge.setEditable(false);
        txtAge.setBounds(260, 46, 86, 25);
        panel_PatientDB.add(txtAge);
        txtAge.setColumns(10);

        JSeparator separator_2 = new JSeparator();
        separator_2.setBackground(Color.DARK_GRAY);
        separator_2.setBounds(10, 81, 759, 8);
        panel_PatientDB.add(separator_2);

        JScrollPane scrlTblPatRec = new JScrollPane();
        scrlTblPatRec.setBounds(10, 100, 593, 230);
        panel_PatientDB.add(scrlTblPatRec);

        Object[] prMeta = {"ID", "Medicine Name", "Quantity", "Complications", "Start Date & Time", "End Date & Time", "Dose Per Day"};

        tblPatientCurrRec = new JTable();
        scrlTblPatRec.setViewportView(tblPatientCurrRec);
        tblPatientCurrRec.setEnabled(false);

        final DefaultTableModel patRecDTM = (DefaultTableModel) tblPatientCurrRec.getModel();
        patRecDTM.setColumnIdentifiers(prMeta);


        /*************************************
         * Add Patient Record Components
         **************************************/

        final JFrame addRecordPRBox = new JFrame("Patient Record Add/Edit Panel");
        addRecordPRBox.setBounds(150, 150, 450, 410);
        addRecordPRBox.setResizable(false);

        AddPtntRecPanel addRecPRPanel = new AddPtntRecPanel();
        addRecordPRBox.getContentPane().add(addRecPRPanel);

        final JTextField txtMedNamePR = new JTextField();
        txtMedNamePR.setBounds(120, 52, 292, 25);
        addRecPRPanel.add(txtMedNamePR);
        txtMedNamePR.setColumns(10);

        final JTextField txtQuantityPR = new JTextField();
        txtQuantityPR.setColumns(10);
        txtQuantityPR.setBounds(120, 96, 75, 25);
        addRecPRPanel.add(txtQuantityPR);

        final JTextField txtCompPR = new JTextField();
        txtCompPR.setColumns(10);
        txtCompPR.setBounds(120, 138, 292, 25);
        addRecPRPanel.add(txtCompPR);

        final SpinnerDateModel sDateModel = new SpinnerDateModel();
        final JSpinner spinStartDatePR = new JSpinner(sDateModel);
        JSpinner.DateEditor sDateEditor = new JSpinner.DateEditor(spinStartDatePR, "dd-MMM-yyyy hh:mm a");
        spinStartDatePR.setEditor(sDateEditor);
        spinStartDatePR.setValue(new Date());
        spinStartDatePR.setBounds(141, 180, 200, 25);
        addRecPRPanel.add(spinStartDatePR);

        final SpinnerDateModel eDateModel = new SpinnerDateModel();
        final JSpinner spinEndDatePR = new JSpinner(eDateModel);
        JSpinner.DateEditor eDateEditor = new JSpinner.DateEditor(spinEndDatePR, "dd-MMM-yyyy hh:mm a");
        spinEndDatePR.setEditor(eDateEditor);
        spinEndDatePR.setValue(new Date());
        spinEndDatePR.setBounds(141, 225, 200, 25);
        addRecPRPanel.add(spinEndDatePR);

        final JTextField txtDosePDayPR = new JTextField();
        txtDosePDayPR.setColumns(10);
        txtDosePDayPR.setBounds(120, 266, 75, 25);
        addRecPRPanel.add(txtDosePDayPR);

        final JButton btnAddRecPR = new JButton("Add Record");
        btnAddRecPR.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent arg0) {
                if (txtMedNamePR.getText().length() == 0 || txtQuantityPR.getText().length() == 0) {
                    JOptionPane.showMessageDialog(addRecordPRBox, "Please fill out the mandatory fields.", "Warning!", JOptionPane.WARNING_MESSAGE);
                } else {
                    String medNamePR = txtMedNamePR.getText().toLowerCase();
                    if (hhmain.mNameChecker(medNamePR)) {

                        String complicationsPR = txtCompPR.getText();

                        Date startDate = (Date) spinStartDatePR.getValue();
                        Timestamp startTS = new Timestamp(startDate.getTime());

                        Date endDate = (Date) spinEndDatePR.getValue();
                        Timestamp endTS = new Timestamp(endDate.getTime());

                        try {
                            int quantityPR = Integer.parseInt(txtQuantityPR.getText());
                            int dpp = Integer.parseInt(txtDosePDayPR.getText());

                            hhmain.addPatRecToDB(curr_user_id, medNamePR, quantityPR, complicationsPR, startTS, endTS, dpp);

                            JOptionPane.showMessageDialog(addMedRecMBox, "Record successfully added to database!");

                        } catch (Exception e) {
                            JOptionPane.showMessageDialog(addMedRecMBox, "Incorrect information provided.", "Warning!", JOptionPane.WARNING_MESSAGE);
                        }

                        patRecDTM.setRowCount(0);
                        Object[][] patRecData = hhmain.getPatRecDBData(curr_user_id);
                        for (int i = 0; i < patRecData.length; i++) {
                            patRecDTM.addRow(patRecData[i]);
                        }

                        addRecordPRBox.dispose();
                    } else {
                        JOptionPane.showMessageDialog(addRecordPRBox, "<html>No such medicine is in the Database.<br>Make sure you spelled it right or add it into the database in the Medicine Database window.</html>", "Warning!", JOptionPane.WARNING_MESSAGE);
                    }
                }
            }
        });
        btnAddRecPR.setBounds(42, 310, 164, 41);
        addRecPRPanel.add(btnAddRecPR);

        final JButton btnEditRecPR = new JButton("Edit Record");
        btnEditRecPR.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent arg0) {
                int sl_no = Integer.parseInt((String) patRecDTM.getValueAt(tblPatientCurrRec.getSelectedRow(), 0));
                String medNamePR = txtMedNamePR.getText();
                if (hhmain.mNameChecker(medNamePR)) {
                    String complicationsPR = txtCompPR.getText();

                    Date startDate = (Date) spinStartDatePR.getValue();
                    Timestamp startTS = new Timestamp(startDate.getTime());

                    Date endDate = (Date) spinEndDatePR.getValue();
                    Timestamp endTS = new Timestamp(endDate.getTime());

                    try {
                        int quantityPR = Integer.parseInt(txtQuantityPR.getText());
                        int dpp = Integer.parseInt(txtDosePDayPR.getText());

                        hhmain.editPatRecToDB(sl_no, medNamePR, quantityPR, complicationsPR, startTS, endTS, dpp);
                        JOptionPane.showMessageDialog(addMedRecMBox, "Record successfully edited and saved to database!");

                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(addMedRecMBox, "Incorrect information provided.", "Warning!", JOptionPane.WARNING_MESSAGE);
                    }

                    patRecDTM.setRowCount(0);
                    Object[][] patRecData = hhmain.getPatRecDBData(curr_user_id);
                    for (int i = 0; i < patRecData.length; i++) {
                        patRecDTM.addRow(patRecData[i]);
                    }

                    addRecordPRBox.dispose();

                } else {
                    JOptionPane.showMessageDialog(addRecordPRBox, "<html>No such medicine is in the Database.<br>Make sure you spelled it right or add it into the database in the Medicine Database window.</html>", "Warning!", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        btnEditRecPR.setBounds(42, 310, 164, 41);
        addRecPRPanel.add(btnEditRecPR);

        JButton btnCancelRecPR = new JButton("Cancel");
        btnCancelRecPR.setBounds(216, 310, 164, 41);
        addRecPRPanel.add(btnCancelRecPR);
        btnCancelRecPR.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent arg0) {
                addRecordPRBox.dispose();
            }
        });

        /********************************
         * END OF PATIENT RECORD PANEL COMPONENT
         ********************************/

        final JButton btnAddRecPat = new JButton("Add New Record");
        btnAddRecPat.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent arg0) {
                btnAddRecPR.setVisible(true);
                btnEditRecPR.setVisible(false);

                addRecordPRBox.setVisible(true);
                txtMedNamePR.setText(null);
                txtQuantityPR.setText(null);
                txtCompPR.setText(null);
                spinStartDatePR.setValue(new Date());
                spinEndDatePR.setValue(new Date());
                txtDosePDayPR.setText(null);
            }
        });
        btnAddRecPat.setEnabled(false);
        btnAddRecPat.setFont(new Font("Tahoma", Font.PLAIN, 12));
        btnAddRecPat.setBounds(613, 100, 156, 31);
        panel_PatientDB.add(btnAddRecPat);

        final JButton btnDelRecPat = new JButton("Delete Record");
        btnDelRecPat.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent arg0) {
                int rec_id = Integer.parseInt((String) patRecDTM.getValueAt(tblPatientCurrRec.getSelectedRow(), 0));

                hhmain.delPatRec(rec_id);

                patRecDTM.setRowCount(0);
                Object[][] patRecData = hhmain.getPatRecDBData(curr_user_id);
                for (int i = 0; i < patRecData.length; i++) {
                    patRecDTM.addRow(patRecData[i]);
                }

                JOptionPane.showMessageDialog(addMedRecMBox, "Record successfully removed from database!");
            }
        });
        btnDelRecPat.setEnabled(false);
        btnDelRecPat.setFont(new Font("Tahoma", Font.PLAIN, 12));
        btnDelRecPat.setBounds(613, 169, 156, 31);
        panel_PatientDB.add(btnDelRecPat);

        final JButton btnEditRecPat = new JButton("Edit Record");
        btnEditRecPat.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent arg0) {
                btnAddRecPR.setVisible(false);
                btnEditRecPR.setVisible(true);

                int row_num = tblPatientCurrRec.getSelectedRow();

                if (row_num != -1) {
                    txtMedNamePR.setText((String) patRecDTM.getValueAt(row_num, 1));
                    txtQuantityPR.setText((String) patRecDTM.getValueAt(row_num, 2));
                    txtCompPR.setText((String) patRecDTM.getValueAt(row_num, 3));

                    String startDate = (String) patRecDTM.getValueAt(row_num, 4);
                    String endDate = (String) patRecDTM.getValueAt(row_num, 5);

                    SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                    try {
                        spinStartDatePR.setValue(dFormat.parse(startDate));
                        spinEndDatePR.setValue(dFormat.parse(endDate));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    txtDosePDayPR.setText((String) patRecDTM.getValueAt(row_num, 6));

                    addRecordPRBox.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(addMedRecMBox, "Select a row first in order to edit.", "Warning!", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        btnEditRecPat.setEnabled(false);
        btnEditRecPat.setFont(new Font("Tahoma", Font.PLAIN, 12));
        btnEditRecPat.setBounds(613, 135, 156, 31);
        panel_PatientDB.add(btnEditRecPat);

        final JButton btnGenerateReport = new JButton("Generate Report");
        btnGenerateReport.addMouseListener(new MouseAdapter() {
            private Scanner fileReader;

            public void mouseClicked(MouseEvent arg0) {
                File inpRF = new File("resources/report.html");

                JFileChooser jfc = new JFileChooser("Save Report");
                FileNameExtensionFilter htmlFilter = new FileNameExtensionFilter("HTML File", "html");
                jfc.setFileFilter(htmlFilter);

                int approvalVal = jfc.showSaveDialog(frmMain);

                if (approvalVal == JFileChooser.APPROVE_OPTION) {
                    File outRF = new File(jfc.getSelectedFile().getAbsolutePath() + ".html");

                    try {
                        fileReader = new Scanner(inpRF);
                        FileWriter fw = new FileWriter(outRF);

                        while (fileReader.hasNextLine()) {
                            String line = fileReader.nextLine();
                            if (line.equals("!!NAME!!")) {
                                line = line.replaceAll("!!NAME!!", txtName.getText());
                            } else if (line.equals("!!AGE!!")) {
                                line = line.replaceAll("!!AGE!!", txtAge.getText());
                            } else if (line.equals("!!GENDER!!")) {
                                line = line.replaceAll("!!GENDER!!", txtSex.getText());
                            } else if (line.equals("!!ENTER INFO HERE!!")) {
                                line = "<tr>";
                                String[][] tblData = hhmain.getPatRecDBData(curr_user_id);

                                for (int i = 0; i < tblData.length; i++) {
                                    for (int j = 0; j < tblData[0].length - 1; j++) {
                                        line = line + "<td>" + tblData[i][j] + "</td>";
                                    }

                                    line = line + "</tr>";
                                }


                            }

                            fw.write(line);
                        }

                        fw.flush();
                        fw.close();

                        File logo = new File("resources/images/HHLogoReport.png");
                        File outLogo = new File(outRF.getParent() + "/images");
                        outLogo.mkdir();
                        outLogo = new File(outLogo.getAbsolutePath() + "/HHLogoReport.png");
                        Files.copy(logo.toPath(), outLogo.toPath());

                        Desktop ds = null;
                        if (Desktop.isDesktopSupported()) ds = Desktop.getDesktop();
                        if (ds != null) {

                            if (ds.isSupported(Desktop.Action.OPEN)) {
                                ds.open(outRF);
                            }

                        }

                        JOptionPane.showMessageDialog(frmMain, "Report successfully generated!");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        });
        btnGenerateReport.setEnabled(false);
        btnGenerateReport.setFont(new Font("Tahoma", Font.PLAIN, 12));
        btnGenerateReport.setBounds(613, 203, 156, 31);
        panel_PatientDB.add(btnGenerateReport);

        JSeparator separator_3 = new JSeparator();
        separator_3.setBackground(Color.DARK_GRAY);
        separator_3.setBounds(613, 245, 156, 8);
        panel_PatientDB.add(separator_3);

        final JButton btnReminder = new JButton("Reminder");
        btnReminder.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                AlarmManager am = AlarmManager.getInstance();
                ReminderViewer rv = new ReminderViewer(am.getalAlarmList());
                rv.show();
            }
        });
        btnReminder.setEnabled(false);
        btnReminder.setFont(new Font("Tahoma", Font.PLAIN, 12));
        btnReminder.setBounds(613, 255, 156, 31);
        panel_PatientDB.add(btnReminder);

        final JButton btnLogout = new JButton("Log Out");
        btnLogout.setEnabled(false);
        btnLogout.setFont(new Font("Tahoma", Font.PLAIN, 12));
        btnLogout.setBounds(613, 42, 156, 31);
        panel_PatientDB.add(btnLogout);

        /***************************
         * PASSWORD EDIT BOX COMPONENTS
         *****************************/
        final JFrame passChangeBox = new JFrame("Change Password");
        passChangeBox.setBounds(150, 150, 469, 230);
        passChangeBox.setResizable(false);

        EditPassPanel passBox = new EditPassPanel();
        passChangeBox.getContentPane().add(passBox);

        final JPasswordField txtConfNewPass = new JPasswordField();
        txtConfNewPass.setBounds(173, 109, 257, 20);
        passBox.add(txtConfNewPass);

        final JPasswordField txtNewPass = new JPasswordField();
        txtNewPass.setBounds(173, 64, 257, 20);
        passBox.add(txtNewPass);

        final JPasswordField txtOldPass = new JPasswordField();
        txtOldPass.setBounds(173, 19, 257, 20);
        passBox.add(txtOldPass);

        final JButton btnConfirm = new JButton("Confirm");
        btnConfirm.addMouseListener(new MouseAdapter() {
            @SuppressWarnings("deprecation")
            public void mouseClicked(MouseEvent arg0) {
                String oldPass = txtOldPass.getText();
                String newPass = txtNewPass.getText();
                String newPassConf = txtConfNewPass.getText();

                boolean retVal = false;

                if (newPass.equals(newPassConf)) {
                    retVal = hhmain.changePass(curr_user_id, oldPass, newPass);
                } else {
                    JOptionPane.showMessageDialog(passChangeBox, "New passwords do not match. Try again.", "Warning!", JOptionPane.WARNING_MESSAGE);
                }

                if (retVal) {
                    JOptionPane.showMessageDialog(passChangeBox, "Password changed successfully!");
                    passChangeBox.dispose();
                } else {
                    JOptionPane.showMessageDialog(passChangeBox, "Password change failed! Try again.", "Warning!", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        btnConfirm.setFont(new Font("Tahoma", Font.PLAIN, 13));
        btnConfirm.setBounds(325, 144, 105, 35);
        passBox.add(btnConfirm);

        /***************************
         * INFO EDIT BOX COMPONENTS
         *****************************/

        final JFrame infoEditBox = new JFrame("Change Profile Information");
        infoEditBox.setBounds(150, 150, 410, 225);
        infoEditBox.setResizable(false);

        EditInfoPanel infoEditPanel = new EditInfoPanel();
        infoEditBox.getContentPane().add(infoEditPanel);

        final JTextField txtNameEdit = new JTextField();
        txtNameEdit.setBounds(71, 19, 310, 20);
        infoEditPanel.add(txtNameEdit);
        txtNameEdit.setColumns(10);

        final JTextField txtAgeEdit = new JTextField();
        txtAgeEdit.setColumns(10);
        txtAgeEdit.setBounds(71, 72, 69, 20);
        infoEditPanel.add(txtAgeEdit);

        final JTextField txtGenderEdit = new JTextField();
        txtGenderEdit.setColumns(10);
        txtGenderEdit.setBounds(74, 122, 66, 20);
        infoEditPanel.add(txtGenderEdit);

        final JButton btnConfInfoEdit = new JButton("Confirm");

        btnConfInfoEdit.setFont(new Font("Tahoma", Font.PLAIN, 13));
        btnConfInfoEdit.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent arg0) {
                String newName = txtNameEdit.getText();
                int newAge = Integer.parseInt(txtAgeEdit.getText());
                String newGender = txtGenderEdit.getText();

                boolean retVal = hhmain.changeInfo(curr_user_id, newName, newAge, newGender);

                if (retVal) {
                    JOptionPane.showMessageDialog(infoEditBox, "Profile information updated!");

                    txtName.setText(newName);
                    txtAge.setText("" + newAge);
                    txtSex.setText(newGender);

                    infoEditBox.dispose();
                } else {
                    JOptionPane.showMessageDialog(infoEditBox, "Updating failed! Try again.", "Warning!", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        btnConfInfoEdit.setBounds(267, 142, 114, 41);
        infoEditPanel.add(btnConfInfoEdit);

        /***************************
         * EDIT BOX COMPONENT END
         *****************************/


        final JButton btnEditInfoPat = new JButton("Edit Information");
        btnEditInfoPat.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent arg0) {
                Object[] options = {"Change Password", "Edit Information"};
                int usrChoice = JOptionPane.showOptionDialog(frmMain, "Choose what you wish to edit.",
                        "Question", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

                if (usrChoice == 0) {
                    passChangeBox.setVisible(true);
                    txtOldPass.setText(null);
                    txtNewPass.setText(null);
                    txtConfNewPass.setText(null);
                } else {
                    infoEditBox.setVisible(true);
                    txtNameEdit.setText(null);
                    txtAgeEdit.setText(null);
                    txtGenderEdit.setText(null);
                }
            }
        });
        btnEditInfoPat.setEnabled(false);
        btnEditInfoPat.setFont(new Font("Tahoma", Font.PLAIN, 12));
        btnEditInfoPat.setBounds(447, 42, 156, 31);
        panel_PatientDB.add(btnEditInfoPat);


        final JButton btnAbout = new JButton("");
        btnAbout.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent arg0) {
                JFrame abBox = new JFrame("About");
                abBox.setBounds(100, 100, 566, 400);
                AboutBox ab = new AboutBox();
                abBox.getContentPane().add(ab);
                abBox.setResizable(false);
                abBox.setVisible(true);
            }
        });
        btnAbout.setToolTipText("<html><b>Helping Hand v1.0</b><br>Click to know about the cast and crew.</html>");
        btnAbout.setIcon(new ImageIcon(HelpingHandGUI.class.getResource("/resources/HHLogosmall.png")));
        btnAbout.setBounds(703, 385, 76, 60);
        btnAbout.setContentAreaFilled(false);
        btnAbout.setOpaque(false);
        btnAbout.setBorderPainted(false);
        frmMain.getContentPane().add(btnAbout);


        final JFrame userAuthBox = new JFrame("Login");
        userAuthBox.setBounds(150, 150, 405, 210);
        userAuthBox.setResizable(false);

        final JFrame regBox = new JFrame("Registration Form");
        regBox.setBounds(150, 150, 430, 385);
        regBox.setResizable(false);


        /**********************************
         *  login JFrame components
         ***********************************/

        UsrAuthPanel usrAuthBoxPanel = new UsrAuthPanel();
        userAuthBox.getContentPane().add(usrAuthBoxPanel);
        userAuthBox.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        final JTextField txtUNameAuth = new JTextField();
        txtUNameAuth.setBounds(93, 53, 285, 23);
        txtUNameAuth.setColumns(10);
        usrAuthBoxPanel.add(txtUNameAuth);

        final JPasswordField txtPassAuth = new JPasswordField();
        txtPassAuth.setBounds(93, 87, 285, 23);
        usrAuthBoxPanel.add(txtPassAuth);

        final JButton btnLoginAuth = new JButton("Login");
        btnLoginAuth.setFont(new Font("Tahoma", Font.PLAIN, 13));
        btnLoginAuth.setBounds(67, 133, 97, 23);
        usrAuthBoxPanel.add(btnLoginAuth);
        btnLoginAuth.addMouseListener(new MouseAdapter() {
            @SuppressWarnings("deprecation")
            public void mouseClicked(MouseEvent arg0) {
                int db_msg = hhmain.userAuthentication(txtUNameAuth.getText(), txtPassAuth.getText());
                if (db_msg > 0) {
                    curr_user_id = db_msg;
                    String[] uinfo = hhmain.userInfoGather(txtUNameAuth.getText(), txtPassAuth.getText());

                    txtName.setText(uinfo[1]);
                    txtAge.setText(uinfo[2]);
                    txtSex.setText(uinfo[3]);

                    Object[][] patRecData = hhmain.getPatRecDBData(curr_user_id);
                    patRecDTM.setRowCount(0);
                    for (int i = 0; i < patRecData.length; i++) {
                        patRecDTM.addRow(patRecData[i]);
                    }

                    JOptionPane.showMessageDialog(userAuthBox, "Welcome to the patient prescription panel!");
                    userAuthBox.dispose();

                    lblName.setEnabled(true);
                    lblAge.setEnabled(true);
                    lblGender.setEnabled(true);
                    txtName.setEnabled(true);
                    txtAge.setEnabled(true);
                    txtSex.setEnabled(true);

                    btnLogout.setEnabled(true);
                    btnEditInfoPat.setEnabled(true);

                    tblPatientCurrRec.setEnabled(true);
                    btnAddRecPat.setEnabled(true);
                    btnDelRecPat.setEnabled(true);
                    btnEditRecPat.setEnabled(true);
                    btnGenerateReport.setEnabled(true);

                    btnReminder.setEnabled(true);
                } else {
                    JOptionPane.showMessageDialog(userAuthBox, "Invalid user name. Please try again.", "Warning!", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        JButton btnCancelAuth = new JButton("Cancel");
        btnCancelAuth.setFont(new Font("Tahoma", Font.PLAIN, 13));
        btnCancelAuth.setBounds(174, 133, 97, 23);
        usrAuthBoxPanel.add(btnCancelAuth);
        btnCancelAuth.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent arg0) {
                Object[] btnOptions = {"Yes", "No"};
                int usrChoice = JOptionPane.showOptionDialog(userAuthBox, "Do you really want to cancel? You cannot access the Patient Prescription interface without login.",
                        "Warning!", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, btnOptions, btnOptions[1]);
                if (usrChoice == 0) {
                    tabbedPane.setSelectedIndex(0);
                    userAuthBox.dispose();
                }
            }
        });

        final JButton btnRegisterAuth = new JButton("Register");
        btnRegisterAuth.setFont(new Font("Tahoma", Font.PLAIN, 13));
        btnRegisterAuth.setBounds(281, 133, 97, 23);
        usrAuthBoxPanel.add(btnRegisterAuth);
        btnRegisterAuth.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent arg0) {
                if (userAuthBox.isVisible()) {
                    userAuthBox.dispose();
                }
                regBox.setVisible(true);
            }
        });

        /*************************************
         * END OF LOGIN COMPONENTS
         *************************************/


        /************************************
         * Registration Panel Component
         ********************************/

        RegPanel regPanel = new RegPanel();
        regBox.getContentPane().add(regPanel);
        regBox.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        final JTextField txtUsrNameReg = new JTextField();
        txtUsrNameReg.setBounds(106, 61, 288, 25);
        regPanel.add(txtUsrNameReg);
        txtUsrNameReg.setColumns(10);

        final JTextField txtNameReg = new JTextField();
        txtNameReg.setColumns(10);
        txtNameReg.setBounds(106, 146, 288, 25);
        regPanel.add(txtNameReg);

        final JTextField txtAgeReg = new JTextField();
        txtAgeReg.setColumns(10);
        txtAgeReg.setBounds(106, 188, 68, 25);
        regPanel.add(txtAgeReg);

        final JTextField txtGenderReg = new JTextField();
        txtGenderReg.setColumns(10);
        txtGenderReg.setBounds(106, 230, 68, 25);
        regPanel.add(txtGenderReg);

        final JPasswordField txtPasswordReg = new JPasswordField();
        txtPasswordReg.setBounds(106, 99, 288, 31);
        regPanel.add(txtPasswordReg);

        JButton btnRegisterReg = new JButton("Register");
        btnRegisterReg.setBounds(29, 291, 144, 36);
        btnRegisterReg.setFont(new Font("Tahoma", Font.PLAIN, 13));
        regPanel.add(btnRegisterReg);
        btnRegisterReg.addMouseListener(new MouseAdapter() {
            @SuppressWarnings("deprecation")
            public void mouseClicked(MouseEvent arg0) {
                if ((txtUsrNameReg.getText().length() == 0) || (txtPasswordReg.getText().length() == 0) || (txtNameReg.getText().length() == 0)) {
                    JOptionPane.showMessageDialog(regBox, "Please fill out the mandatory fields.", "Warning!", JOptionPane.WARNING_MESSAGE);
                } else {

                    boolean unameCheck = hhmain.uNameChecker(txtUsrNameReg.getText());

                    if (unameCheck) {
                        JOptionPane.showMessageDialog(regBox, "User name already in use. Please try another.", "Warning!", JOptionPane.WARNING_MESSAGE);
                    } else {
                        String uNameReg = txtUsrNameReg.getText();
                        String passReg = txtPasswordReg.getText();
                        String nameReg = txtNameReg.getText();
                        String genderReg = txtGenderReg.getText();

                        try {
                            int ageReg = Integer.parseInt(txtAgeReg.getText());
                            hhmain.userRegistration(uNameReg, passReg, nameReg, ageReg, genderReg);
                            JOptionPane.showMessageDialog(regBox, "Registration successful! Please try logging in.");
                        } catch (Exception e) {
                            JOptionPane.showMessageDialog(addMedRecMBox, "Incorrect information provided.", "Warning!", JOptionPane.WARNING_MESSAGE);
                        }

                        txtUsrNameReg.setText(null);
                        txtPasswordReg.setText(null);
                        txtNameReg.setText(null);
                        txtAgeReg.setText(null);
                        txtGenderReg.setText(null);

                        regBox.dispose();

                        userAuthBox.setVisible(true);
                    }
                }
            }
        });

        JButton btnCancelReg = new JButton("Cancel");
        btnCancelReg.setBounds(226, 291, 144, 36);
        btnCancelReg.setFont(new Font("Tahoma", Font.PLAIN, 13));
        regPanel.add(btnCancelReg);
        btnCancelReg.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent arg0) {
                if (regBox.isVisible()) {
                    regBox.dispose();
                }

                txtUsrNameReg.setText(null);
                txtPasswordReg.setText(null);
                txtNameReg.setText(null);
                txtAgeReg.setText(null);
                txtGenderReg.setText(null);

                userAuthBox.setVisible(true);
            }
        });

        /****************************************
         * END OF REGISTRATION COMPONENTS
         *****************************************/

        btnLogout.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent arg0) {
                txtUNameAuth.setText(null);
                txtPassAuth.setText(null);
                tabbedPane.setSelectedIndex(0);
                JOptionPane.showMessageDialog(regBox, "You have been successfully logged out of the Patient Prescription panel!");
            }
        });

        tabbedPane.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                if (tabbedPane.getSelectedIndex() == 1) {
                    userAuthBox.setVisible(true);
                } else {
                    userAuthBox.dispose();
                    regBox.dispose();

                    lblName.setEnabled(false);
                    lblAge.setEnabled(false);
                    lblGender.setEnabled(false);
                    txtName.setEnabled(false);
                    txtName.setText(null);
                    txtAge.setEnabled(false);
                    txtAge.setText(null);
                    txtSex.setEnabled(false);
                    txtSex.setText(null);

                    btnLogout.setEnabled(false);
                    btnEditInfoPat.setEnabled(false);

                    tblPatientCurrRec.setEnabled(false);
                    btnAddRecPat.setEnabled(false);
                    btnDelRecPat.setEnabled(false);
                    btnEditRecPat.setEnabled(false);
                    btnGenerateReport.setEnabled(false);

                    btnReminder.setEnabled(false);

                    curr_user_id = -1;

                    patRecDTM.setRowCount(0);
                }
            }
        });
    }

    private final Clip createAudioClip(URL url) throws IOException,
            LineUnavailableException, UnsupportedAudioFileException {
        InputStream in = null;
        AudioInputStream ain = null;
        Clip clip = null;

        try {
            in = new BufferedInputStream(url.openStream());
            ain = AudioSystem.getAudioInputStream(in);
            clip = AudioSystem.getClip();
            clip.open(ain);

        } finally {
            if (in != null) in.close();
            if (ain != null) ain.close();
        }
        return clip;
    }

    private class WindowHandler extends WindowAdapter {
        JFrame mainframe;
        String msg;

        public WindowHandler(JFrame frm) {
            mainframe = frm;
            msg = "Are you sure you want to exit?";
        }

        public void windowClosing(WindowEvent evt) {
            int res = JOptionPane.showConfirmDialog(mainframe, msg, "Exit"
                    , JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null);
            if (res == JOptionPane.YES_OPTION) {
                AlarmManager am = AlarmManager.getInstance();
                am.stopService();
                mainframe.dispose();
                System.exit(0);
            }
        }
    }
}
