package ca.antonious.materialdaypicker

interface SelectionMode {
    fun getSelectionStateAfterSelecting(lastSelectionState: SelectionState, dayToSelect: MaterialDayPicker.Weekday): SelectionState
    fun getSelectionStateAfterDeselecting(lastSelectionState: SelectionState, dayToDeselect: MaterialDayPicker.Weekday): SelectionState
}
