package ca.antonious.materialdaypicker

class SingleSelectionMode : DefaultSelectionMode() {

    override fun getSelectionStateAfterSelecting(lastSelectionState: SelectionState, dayToSelect: MaterialDayPicker.Weekday): SelectionState {
        return SelectionState.withSingleDay(dayToSelect)
    }

    companion object {
        @JvmStatic
        fun create(): SelectionMode {
            return SingleSelectionMode()
        }
    }
}
