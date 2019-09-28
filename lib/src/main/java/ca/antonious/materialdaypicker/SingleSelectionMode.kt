package ca.antonious.materialdaypicker

/**
 * An implementation of [SelectionMode] that only allows one day to be
 * selected at a time. If a day was already selected when a user selected
 * a new day, the last day will get deselected and the new day will be selected.
 */
class SingleSelectionMode : DefaultSelectionMode() {

    override fun getSelectionStateAfterSelecting(lastSelectionState: SelectionState, dayToSelect: MaterialDayPicker.Weekday): SelectionState {
        return SelectionState.withSingleDay(dayToSelect)
    }

    companion object {
        /**
         * Creates a new instance of a [SingleSelectionMode]
         */
        @JvmStatic
        fun create(): SelectionMode {
            return SingleSelectionMode()
        }
    }
}
