package ca.antonious.materialdaypicker;

import java.util.List;

public class DefaultSelectionMode implements SelectionMode {

    public static SelectionMode create() {
        return new DefaultSelectionMode();
    }

    @Override
    public List<MaterialDayPicker.Weekday> getSelectableDays() {
        return MaterialDayPicker.Weekday.getAllDays();
    }

    @Override
    public SelectionState getSelectionStateAfterSelecting(SelectionState lastSelectionState, MaterialDayPicker.Weekday dayToSelect) {
        return lastSelectionState.withDaySelected(dayToSelect);
    }

    @Override
    public SelectionState getSelectionStateAfterDeselecting(SelectionState lastSelectionState, MaterialDayPicker.Weekday dayToDeselect) {
        return lastSelectionState.withDayDeselected(dayToDeselect);
    }
}
