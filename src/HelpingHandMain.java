import modules.DatabaseEngine;

import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.StringTokenizer;

/**
 * @author 11301007
 */
public class HelpingHandMain {

    DatabaseEngine dbe = DatabaseEngine.getInstance();

    public boolean connectDB() {
        return dbe.connect();
    }

    public String[][] getMedDBData() {
        int colNum = dbe.getMetaData("Medicine").length;
        String[] rawData = dbe.getAllData("Medicine");
        int rowNum = rawData.length;

        StringTokenizer rdSplitter = null;

        String[][] allData = new String[rowNum][colNum];

        for (int i = 0; i < rowNum; i++) {
            rdSplitter = new StringTokenizer(rawData[i], "|");
            for (int j = 0; j < colNum; j++) {
                if (j == 1 || j == 2 || j == 3) {
                    String tmp = rdSplitter.nextToken();
                    tmp = tmp.replaceFirst("" + tmp.charAt(0), ("" + tmp.charAt(0)).toUpperCase());
                    allData[i][j] = tmp;
                } else {
                    allData[i][j] = rdSplitter.nextToken();
                }
            }
        }

        return allData;
    }

    public boolean uNameChecker(String uname) {
        boolean retVal = dbe.uNameChecker(uname);
        return retVal;
    }

    public int userAuthentication(String uname, String pass) {
        int db_msg = dbe.userAuthenticateQuery(uname, pass);
        return db_msg;
    }

    public String[] userInfoGather(String uname, String pass) {
        int u_id = dbe.userAuthenticateQuery(uname, pass);
        String[] uInfo = dbe.getUserInfo(u_id);
        return uInfo;
    }

    public void userRegistration(String uname, String pass, String name, int age, String gender) {
        dbe.addUserToDB(uname, pass, name, age, gender);
    }


    public boolean mNameChecker(String mname) {
        boolean retVal = dbe.medNameChecker(mname);
        return retVal;
    }

    public void addMedToDB(String mName, String manu, String gmName, int dpp, int quan, int st) {
        dbe.addMedToDB(mName.toLowerCase(), manu.toLowerCase(), gmName.toLowerCase(), dpp, quan, st);
    }

    public void editMed(int mID, String mName, String manu, String gmName, int dpp, int quan, int st) {
        dbe.editMedToDB(mID, mName.toLowerCase(), manu.toLowerCase(), gmName.toLowerCase(), dpp, quan, st);
    }


    public String[][] searchMedData(String searchField, String sVar) {
        int colNum = dbe.getMetaData("Medicine").length;
        String[] rawData = dbe.searchMedData(searchField, sVar);
        int rowNum = rawData.length;

        StringTokenizer rdSplitter = null;

        String[][] rsltData = new String[rowNum][colNum];

        for (int i = 0; i < rowNum; i++) {
            rdSplitter = new StringTokenizer(rawData[i], "|");
            for (int j = 0; j < colNum; j++) {
                if (j == 1 || j == 2 || j == 3) {
                    String tmp = rdSplitter.nextToken();
                    tmp = tmp.replaceFirst("" + tmp.charAt(0), ("" + tmp.charAt(0)).toUpperCase());
                    rsltData[i][j] = tmp;
                } else {
                    rsltData[i][j] = rdSplitter.nextToken();
                }
            }
        }

        return rsltData;
    }

    public void delMedRec(int rec_id) {
        dbe.deleteMedFrmDB(rec_id);
    }

    public void addPatRecToDB(int u_id, String mName, int quan, String comp, Timestamp sdate, Timestamp edate, int dpp) {
        int m_id = dbe.medIDResolver(mName, quan);
        dbe.addPatRecToDB(u_id, m_id, comp, sdate, edate, dpp);
    }

    public void editPatRecToDB(int sl_no, String mName, int quan, String comp, Timestamp sdate, Timestamp edate, int dpp) {
        int m_id = dbe.medIDResolver(mName, quan);
        dbe.editPatRecToDB(sl_no, m_id, comp, sdate, edate, dpp);
    }

    public void delPatRec(int rec_id) {
        dbe.deletePatRecFrmDB(rec_id);
    }

    public String[][] getPatRecDBData(int u_id) {
        int colNum = dbe.getMetaData("ptnt_rcd").length;
        String[] rawData = dbe.getAllData("ptnt_rcd");
        int rowNum = rawData.length;

        StringTokenizer rdSplitter = null;

        LinkedList<String[]> allList = new LinkedList<String[]>();

        for (int i = 0; i < rowNum; i++) {
            String[] tmpData = new String[colNum + 1];

            rdSplitter = new StringTokenizer(rawData[i], "|");

            String tmpID = rdSplitter.nextToken();

            if (Integer.parseInt(rdSplitter.nextToken()) == u_id) {
                tmpData[0] = tmpID;
                int mID = Integer.parseInt(rdSplitter.nextToken());
                String[] mData = dbe.medNameResolver(mID);
                mData[0] = mData[0].replaceFirst("" + mData[0].charAt(0), ("" + mData[0].charAt(0)).toUpperCase());
                tmpData[1] = mData[0];
                tmpData[2] = mData[1];
                for (int j = 3; j < colNum; j++) {
                    tmpData[j] = rdSplitter.nextToken();
                }

                allList.add(tmpData);
            }
        }


        String[][] allData = new String[allList.size()][colNum];

        for (int i = 0; i < allList.size(); i++) {
            allData[i] = allList.get(i);
        }

        return allData;
    }


    public boolean changePass(int u_id, String oldPass, String newPass) {
        return dbe.changePass(u_id, oldPass, newPass);
    }

    public boolean changeInfo(int u_id, String newName, int newAge, String newGen) {
        return dbe.changeInfo(u_id, newName, newAge, newGen);
    }
}
