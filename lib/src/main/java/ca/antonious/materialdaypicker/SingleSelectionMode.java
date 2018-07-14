package ca.antonious.materialdaypicker;

public class SingleSelectionMode extends DefaultSelectionMode {

    public static SelectionMode create() {
        return new SingleSelectionMode();
    }

    @Override
    public SelectionState getSelectionStateAfterSelecting(SelectionState lastSelectionState, MaterialDayPicker.Weekday dayToSelect) {
        return SelectionState.withSingleDay(dayToSelect);
    }
}
