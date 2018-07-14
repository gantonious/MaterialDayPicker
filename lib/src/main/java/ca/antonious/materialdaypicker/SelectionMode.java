package ca.antonious.materialdaypicker;

import java.util.List;

public interface SelectionMode {
    List<MaterialDayPicker.Weekday> getSelectableDays();
    SelectionState getSelectionStateAfterSelecting(SelectionState lastSelectionState, MaterialDayPicker.Weekday dayToSelect);
    SelectionState getSelectionStateAfterDeselecting(SelectionState lastSelectionState, MaterialDayPicker.Weekday dayToDeselect);
}
