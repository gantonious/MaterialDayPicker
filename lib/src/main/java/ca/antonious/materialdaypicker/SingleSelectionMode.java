package ca.antonious.materialdaypicker;

public class SingleSelectionMode extends DefaultSelectionMode {
    @Override
    public SelectionState getSelectionStateAfterSelecting(SelectionState lastSelectionState, MaterialDayPicker.Weekday dayToSelect) {
        return SelectionState.withSingleDay(dayToSelect);
    }
}
