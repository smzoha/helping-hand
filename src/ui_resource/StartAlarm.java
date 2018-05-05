package ui_resource;

import modules.Alarm;

import javax.sound.sampled.Clip;

public class StartAlarm implements Runnable {

    private AlarmPopup pop;
    private Alarm alm;
    private Clip alarmTone;

    public StartAlarm(Clip alarmTone) {
        pop = null;
        alm = null;
        this.alarmTone = alarmTone;
    }

    public void setAlarmInfo(Alarm alm) {
        this.alm = alm;
    }

    public void run() {
        if (pop == null) pop = new AlarmPopup();
        pop.setAlarmInfo(alm);
        pop.setAlarmTone(alarmTone);
        pop.show(null);
    }

}
