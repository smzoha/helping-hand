package modules;

import ui_resource.StartAlarm;

import javax.swing.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ExecutorService;

public class AlarmDispatcher implements Runnable {

    private Queue<Alarm> alarmqueue;
    private List<Alarm> outdateList;

    private ExecutorService exec;
    private volatile boolean stop;
    private boolean running;

    public AlarmDispatcher(List<Alarm> list, ExecutorService ex) {
        alarmqueue = new LinkedList<Alarm>();
        outdateList = new LinkedList<Alarm>();
        if (list != null) alarmqueue.addAll(list);
        exec = ex;
        stop = false;
        running = false;
    }

    public void stop() {
        stop = true;
        running = false;
    }

    public void addAllAlarms(List<Alarm> lst) {
        synchronized (alarmqueue) {

            for (int i = 0; i < lst.size(); i++) {

                Alarm alm = lst.get(i);
                if (alarmqueue.contains(alm) == false) {
                    if (outdateList.contains(alm) == false) {
                        alarmqueue.add(alm);
                    }
                }

            }

        }
    }

    public boolean isRunning() {
        return running;
    }

    public void run() {

        running = true;

        while (stop == false) {

            try {
                Thread.currentThread().sleep(1000);
                if (alarmqueue.isEmpty() == true) continue;
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            Alarm arm = null;

            synchronized (alarmqueue) {
                arm = alarmqueue.peek();
            }

            long current = System.currentTimeMillis();
            long t_before = current - 3000;
            long t_after = current + 3000;

            long t_arm = arm.getTime();

            if (t_arm > t_before && t_arm < t_after) {
                System.out.println("Alarm happening " + arm.getAlarmName());


                StartAlarm stAlarm = new StartAlarm(AlarmManager.getInstance().getAlarmTone());

                stAlarm.setAlarmInfo(arm);
                SwingUtilities.invokeLater(stAlarm);

                // ring an alarm;
                synchronized (alarmqueue) {
                    alarmqueue.poll();
                }

            } else if (t_arm < t_before) {        // outdated alarm
                synchronized (alarmqueue) {
                    System.out.println("Encountered outdated alarm " + arm.getAlarmName());
                    Alarm alm = alarmqueue.poll();
                    outdateList.add(alm);
                }
            }

        }
        System.out.println("While closed on alarm");

    }

}
