package ca.antonious.materialdaypicker

open class DefaultSelectionMode : SelectionMode {

    override fun getSelectionStateAfterSelecting(lastSelectionState: SelectionState, dayToSelect: MaterialDayPicker.Weekday): SelectionState {
        return lastSelectionState.withDaySelected(dayToSelect)
    }

    override fun getSelectionStateAfterDeselecting(lastSelectionState: SelectionState, dayToDeselect: MaterialDayPicker.Weekday): SelectionState {
        return lastSelectionState.withDayDeselected(dayToDeselect)
    }

    companion object {
        @JvmStatic
        fun create(): SelectionMode {
            return DefaultSelectionMode()
        }
    }
}
