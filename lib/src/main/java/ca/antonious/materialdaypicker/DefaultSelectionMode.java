package ca.antonious.materialdaypicker;

public class DefaultSelectionMode implements SelectionMode {

    public static SelectionMode create() {
        return new DefaultSelectionMode();
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
