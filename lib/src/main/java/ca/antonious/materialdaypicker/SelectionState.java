package ca.antonious.materialdaypicker;

import java.util.ArrayList;
import java.util.List;

public class SelectionState {
    private List<MaterialDayPicker.Weekday> selectedDays;

    public SelectionState() {
        this(new ArrayList<MaterialDayPicker.Weekday>());
    }

    public SelectionState(List<MaterialDayPicker.Weekday> selectedDays) {
        this.selectedDays = new ArrayList<>(selectedDays);
    }

    public static SelectionState withSingleDay(MaterialDayPicker.Weekday day) {
        return new SelectionState().withDaySelected(day);
    }

    public SelectionState withDaySelected(MaterialDayPicker.Weekday dayToSelect) {
        List<MaterialDayPicker.Weekday> newSelections = new ArrayList<>(selectedDays);
        newSelections.add(dayToSelect);
        return new SelectionState(newSelections);
    }

    public SelectionState withDayDeselected(MaterialDayPicker.Weekday dayToDeselect) {
        List<MaterialDayPicker.Weekday> newSelections = new ArrayList<>(selectedDays);
        newSelections.remove(dayToDeselect);
        return new SelectionState(newSelections);
    }
}
