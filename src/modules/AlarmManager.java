package modules;

import javax.sound.sampled.Clip;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class AlarmManager {

    private static AlarmManager instance;

    static {
        instance = null;
    }

    private ExecutorService exec;
    private AlarmDispatcher dispatcher;
    private List<Alarm> alarmList;
    private Clip alarmTone;

    private AlarmManager() {
        exec = Executors.newFixedThreadPool(4);
        dispatcher = new AlarmDispatcher(null, exec);
        alarmList = new LinkedList<Alarm>();
    }

    public static AlarmManager getInstance() {
        if (instance == null) {
            instance = new AlarmManager();
            instance.loadAlarmsFile();
        }
        return instance;
    }

    public void setAlarmRing(Clip clp) {
        alarmTone = clp;
    }

    public Clip getAlarmTone() {
        return alarmTone;
    }

    public void sendAlarmsToDispatcher() {
        dispatcher.addAllAlarms(alarmList);
        if (dispatcher.isRunning() == false) {
            exec.execute(dispatcher);
        }
    }

    public List<Alarm> getalAlarmList() {
        return alarmList;
    }

    public void loadAlarmsFile() {
        try {
            Alarm.readFromFile(alarmList);
            System.out.println("Loaded alarm: " + alarmList.size());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void stopService() {

        try {
            Alarm.writeToFile(alarmList);
            System.out.println("Stored alarm: " + alarmList.size());
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        dispatcher.stop();
        exec.shutdown(); // Disable new tasks from being submitted
        try {
            // Wait a while for existing tasks to terminate
            if (!exec.awaitTermination(60, TimeUnit.SECONDS)) {
                exec.shutdownNow(); // Cancel currently executing tasks
                // Wait a while for tasks to respond to being cancelled
                if (!exec.awaitTermination(60, TimeUnit.SECONDS))
                    System.err.println("Pool did not terminate");
            }
        } catch (InterruptedException ie) {
            // (Re-)Cancel if current thread also interrupted
            exec.shutdownNow();
            // Preserve interrupt status
            Thread.currentThread().interrupt();
        }
        System.out.println("Thread Service shutdown");
    }

}
