package ui_resource;

import modules.Alarm;

import javax.swing.*;
import java.util.List;

public class AlarmListModel extends AbstractListModel {

    private List<Alarm> list;

    public AlarmListModel(List<Alarm> lst) {
        list = lst;
    }

    public int getSize() {
        return list.size();
    }

    public Object getElementAt(int i) {
        return list.get(i);
    }

    public int addAlarm(Alarm alm) {
        list.add(alm);
        this.fireIntervalAdded(this, 0, getSize() - 1);
        return getSize() - 1;
    }

    public void removeAlarm(int index) {
        list.remove(index);
        this.fireIntervalRemoved(this, 0, index);
    }

}
