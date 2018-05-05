package modules;

import java.io.*;
import java.util.List;

public class Alarm implements Comparable<Alarm> {

    static final String file;

    static {
        file = "alarm_info.txt";
    }

    private long time;
    private String alarmName;
    private String alarmDescp;

    public Alarm() {

    }

    public static void writeAlarmToStream(Alarm arm, DataOutputStream out)
            throws IOException {

        out.writeLong(arm.time);
        out.writeUTF(arm.alarmName);
        out.writeUTF(arm.alarmDescp);

    }

    public static void readAlarmFromFile(Alarm arm, DataInputStream in)
            throws IOException {

        arm.time = in.readLong();
        arm.alarmName = in.readUTF();
        arm.alarmDescp = in.readUTF();

    }

    public static void writeToFile(List<Alarm> lst) throws IOException {

        DataOutputStream out = null;
        int alarms = lst.size();

        try {
            out = new DataOutputStream(new BufferedOutputStream(
                    new FileOutputStream(file)));

            out.writeInt(alarms);
            int i = 0;

            while (i < alarms) {
                Alarm.writeAlarmToStream(lst.get(i), out);
                out.flush();
                i++;
            }

        } finally {
            if (out != null) out.close();
        }


    }

    public static void readFromFile(List<Alarm> lst) throws IOException {

        DataInputStream in = null;
        int alarms = 0;
        lst.clear();

        try {
            in = new DataInputStream(new BufferedInputStream
                    (new FileInputStream(file)));

            alarms = in.readInt();
            int i = 0;

            while (i < alarms) {
                Alarm arm = new Alarm();
                Alarm.readAlarmFromFile(arm, in);
                lst.add(arm);
                i++;
            }

        } finally {
            if (in != null) in.close();
        }

    }

    public long getTime() {
        return time;
    }

    public void setTime(long t) {
        time = t;
    }

    public String getAlarmName() {
        return alarmName;
    }

    public String getAlarmDescription() {
        return alarmDescp;
    }

    public void setalarmName(String name) {
        alarmName = name;
    }

    public void setalarmDescription(String desc) {
        alarmDescp = desc;
    }

    public boolean equals(Alarm am) {
        if (time == am.getTime()) return true;
        else return false;
    }

    public int compareTo(Alarm am) {
        return (int) (time - am.getTime());
    }

}
