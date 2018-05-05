package ui_resource;

import modules.Alarm;

import javax.swing.*;
import java.awt.*;

@SuppressWarnings("serial")
public final class AlarmCellRenderer extends AlarmPanel implements ListCellRenderer {

    static {
        //borderColor = new Color(99, 130, 191);			// original border color
        //borderColor = new Color(79, 113, 183);		// modified border color (2nd color)
        //borderColor = new Color(86, 119, 186);		// modified border color (3rd color)
    }

    //private static Color borderColor;
    private boolean painted;

    public AlarmCellRenderer() {
        painted = false;
    }

    public final Component getListCellRendererComponent(JList list,
                                                        Object value,
                                                        int index,
                                                        boolean isSelected,
                                                        boolean cellHasFocus) {

        Alarm msg = (Alarm) value;

        this.setAlarmShowData(msg);

        if (isSelected == true) {
            setColorAsSelected(list.getSelectionBackground());
        } else {
            setColorAsNotSelected();
        }

        if (painted == false) {
            list.revalidate();
            list.repaint();
            painted = true;
        }

//    	if (cellHasFocus == true) attachBorder(borderColor);
//    	else removeBorder();

        return this;
    }

}

