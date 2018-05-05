package modules;

import java.sql.*;
import java.util.LinkedList;

/**
 * @author Shamah M Zoha
 */

public class DatabaseEngine {
    private static DatabaseEngine instance;

    static {
        instance = new DatabaseEngine();
    }

    private Connection dbConnect = null;
    private PreparedStatement addStatement = null;
    private Statement queryStatement = null;
    private ResultSet queryOut = null;

    public static DatabaseEngine getInstance() {
        return instance;
    }

    public boolean connect() {
        try {
            Class.forName("org.sqlite.JDBC");
            dbConnect = DriverManager.getConnection("jdbc:sqlite:resources/main.db");
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public String[] getMetaData(String tableName) {
        String queryString = "SELECT * FROM " + tableName + ";";
        String[] mdData = null;

        try {
            queryStatement = dbConnect.createStatement();
            queryOut = queryStatement.executeQuery(queryString);

            mdData = new String[queryOut.getMetaData().getColumnCount()];

            for (int i = 1; i <= mdData.length; i++) {
                mdData[i - 1] = queryOut.getMetaData().getColumnName(i);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return mdData;
    }

    public String[] getAllData(String tableName) {
        String queryString = "SELECT * FROM " + tableName + ";";
        LinkedList<String> allTupleData = new LinkedList<String>();

        try {
            queryStatement = dbConnect.createStatement();
            queryOut = queryStatement.executeQuery(queryString);

            String[] mdData = new String[queryOut.getMetaData().getColumnCount()];

            for (int i = 1; i <= mdData.length; i++) {
                mdData[i - 1] = queryOut.getMetaData().getColumnName(i);
            }

            String tuple = "";

            while (queryOut.next()) {
                tuple = "";

                for (int j = 0; j < mdData.length; j++) {
                    if (mdData[j].equals("start_dt") || mdData[j].equals("last_dose_time")) {
                        tuple = tuple + queryOut.getTimestamp(mdData[j]).toString() + "|";
                    } else {
                        tuple = tuple + queryOut.getString(mdData[j]) + "|";
                    }
                }

                allTupleData.add(tuple);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        String[] allData = new String[allTupleData.size()];
        return allTupleData.toArray(allData);
    }


    /**
     * userAutheticateQuery method
     * -1 for both user_id and password invalid;
     * 0 for either user_id or password invalid;
     * u_id for successful authentication.
     *
     * @param uname
     * @param pass
     * @return
     */

    public int userAuthenticateQuery(String uname, String pass) {

        int u_id = -1;

        try {
            queryStatement = dbConnect.createStatement();
            queryOut = queryStatement.executeQuery("SELECT * FROM USER WHERE user_name = '" + uname + "' AND password = '" + pass + "';");

            while (queryOut.next()) {
                String outUName = queryOut.getString("user_name");
                String outPass = queryOut.getString("password");

                if (outUName.equals(uname) && outPass.equals(pass)) {
                    u_id = queryOut.getInt("user_id");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return u_id;
    }


    public boolean uNameChecker(String uname) {
        boolean retVal = false;

        try {
            queryStatement = dbConnect.createStatement();
            queryOut = queryStatement.executeQuery("SELECT * FROM USER WHERE user_name = '" + uname + "';");

            while (queryOut.next()) {
                retVal = true;
                break;
            }
        } catch (Exception e) {
            e.getStackTrace();
        }

        return retVal;
    }


    public boolean medNameChecker(String mname) {
        boolean retVal = false;

        try {
            queryStatement = dbConnect.createStatement();
            queryOut = queryStatement.executeQuery("SELECT * FROM MEDICINE WHERE med_name = '" + mname + "';");

            while (queryOut.next()) {
                retVal = true;
                break;
            }

        } catch (Exception e) {
            e.getStackTrace();
        }

        return retVal;
    }


    public String[] getUserInfo(int u_id) {
        String[] userInfo = new String[4];

        try {
            queryStatement = dbConnect.createStatement();
            queryOut = queryStatement.executeQuery("SELECT * FROM USER WHERE user_id = " + u_id);

            while (queryOut.next()) {
                int uid = queryOut.getInt("user_id");
                if (uid == u_id) {
                    userInfo[0] = queryOut.getString("user_name");
                    userInfo[1] = queryOut.getString("name");
                    userInfo[2] = "" + queryOut.getInt("age");
                    userInfo[3] = queryOut.getString("gender");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return userInfo;
    }


    public void addUserToDB(String uname, String pass, String name, int age, String gender) {
        try {
            addStatement = dbConnect.prepareStatement("insert into user (user_name, password, name, age, gender) values (?, ?, ?, ?, ?);");
            addStatement.setString(1, uname);
            addStatement.setString(2, pass);
            addStatement.setString(3, name);
            addStatement.setInt(4, age);
            addStatement.setString(5, gender);
            addStatement.executeUpdate();
        } catch (Exception e) {
            e.getStackTrace();
        }
    }


    public int medIDResolver(String mName, int quan) {
        int retVal = -1;
        try {
            queryStatement = dbConnect.createStatement();
            queryOut = queryStatement.executeQuery("SELECT * FROM MEDICINE WHERE med_name = '" + mName + "' AND quantity = '" + quan + "';");

            while (queryOut.next()) {
                retVal = queryOut.getInt("med_id");
                break;
            }

        } catch (Exception e) {
            e.getStackTrace();
        }

        return retVal;
    }

    public String[] medNameResolver(int m_id) {
        String[] retVal = null;

        try {
            queryStatement = dbConnect.createStatement();
            queryOut = queryStatement.executeQuery("SELECT * FROM MEDICINE WHERE med_id = '" + m_id + "';");

            while (queryOut.next()) {
                retVal = new String[2];
                retVal[0] = queryOut.getString("med_name");
                retVal[1] = "" + queryOut.getInt("quantity");
                break;
            }

        } catch (Exception e) {
            e.getStackTrace();
        }

        return retVal;

    }


    public String[] searchMedData(String searchField, String sVar) {
        String queryString = "SELECT * FROM MEDICINE WHERE " + searchField + " = '" + sVar.toLowerCase() + "';";
        LinkedList<String> rsltTupleData = new LinkedList<String>();

        try {
            queryStatement = dbConnect.createStatement();
            queryOut = queryStatement.executeQuery(queryString);

            String[] mdData = new String[queryOut.getMetaData().getColumnCount()];

            for (int i = 1; i <= mdData.length; i++) {
                mdData[i - 1] = queryOut.getMetaData().getColumnName(i);
            }

            String tuple = "";

            while (queryOut.next()) {
                tuple = "";

                for (int j = 0; j < mdData.length; j++) {
                    tuple = tuple + queryOut.getString(mdData[j]) + "|";
                }

                rsltTupleData.add(tuple);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        String[] allData = new String[rsltTupleData.size()];
        return rsltTupleData.toArray(allData);
    }


    public void addMedToDB(String mName, String manu, String gmName, int dpp, int quan, int st) {
        try {
            addStatement = dbConnect.prepareStatement("insert into medicine (med_name, manufacturer, generic_name, dose_per_day, quantity, storage_temp) values (?, ?, ?, ?, ?, ?);");
            addStatement.setString(1, mName);
            addStatement.setString(2, manu);
            addStatement.setString(3, gmName);
            addStatement.setInt(4, dpp);
            addStatement.setInt(5, quan);
            addStatement.setInt(6, st);
            addStatement.executeUpdate();
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    public void deleteMedFrmDB(int rec_id) {
        try {
            addStatement = dbConnect.prepareStatement("DELETE FROM medicine WHERE med_id = " + rec_id + ";");
            addStatement.executeUpdate();
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    public void editMedToDB(int m_ID, String mName, String manu, String gmName, int dpp, int quan, int st) {
        try {
            addStatement = dbConnect.prepareStatement("update medicine set med_name = '" + mName
                    + "', manufacturer = '" + manu + "', generic_name = '" + gmName + "', dose_per_day = " + dpp
                    + ", quantity = " + quan + ", storage_temp = " + st + " where med_id = " + m_ID + ";");
            addStatement.executeUpdate();
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    public void addPatRecToDB(int uid, int mid, String comp, Timestamp start_date, Timestamp last_dose, int dlft) {
        try {
            addStatement = dbConnect.prepareStatement("insert into ptnt_rcd (user_id, med_id, complications, start_dt, last_dose_time, dose_left) values (?, ?, ?, ?, ?, ?);");
            addStatement.setInt(1, uid);
            addStatement.setInt(2, mid);
            addStatement.setString(3, comp);
            addStatement.setTimestamp(4, start_date);
            addStatement.setTimestamp(5, last_dose);
            addStatement.setInt(6, dlft);
            addStatement.executeUpdate();
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    public void editPatRecToDB(int sl_no, int mid, String comp, Timestamp start_date, Timestamp last_dose, int dlft) {
        try {
            addStatement = dbConnect.prepareStatement("update ptnt_rcd set med_id = " + mid +
                    ", complications = '" + comp + "', start_dt = '" + start_date
                    + "', last_dose_time = '" + last_dose + "', dose_left = " + dlft + " where sl_no = " + sl_no + ";");
            addStatement.executeUpdate();
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    public void deletePatRecFrmDB(int rec_id) {
        try {
            addStatement = dbConnect.prepareStatement("DELETE FROM ptnt_rcd WHERE sl_no = " + rec_id + ";");
            addStatement.executeUpdate();
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    public boolean changePass(int u_id, String oldPass, String newPass) {

        boolean retVal = false;

        try {
            queryStatement = dbConnect.createStatement();
            queryOut = queryStatement.executeQuery("SELECT * FROM USER WHERE user_id = " + u_id + ";");

            while (queryOut.next()) {
                if (queryOut.getString("password").equals(oldPass)) {
                    addStatement = dbConnect.prepareStatement("UPDATE user SET password = '" + newPass + "' where user_id = " + u_id + ";");
                    addStatement.executeUpdate();
                    retVal = true;
                    break;
                } else {
                    break;
                }
            }
        } catch (Exception e) {
            e.getStackTrace();
        }

        return retVal;
    }


    public boolean changeInfo(int u_id, String newName, int newAge, String newGen) {

        boolean retVal = false;


        try {
            addStatement = dbConnect.prepareStatement("UPDATE user SET name = '" + newName + "', age = " + newAge + ", gender = '" + newGen + "' where user_id = " + u_id + ";");
            addStatement.executeUpdate();

            retVal = true;
        } catch (Exception e) {
            e.getStackTrace();
        }

        return retVal;
    }
}
