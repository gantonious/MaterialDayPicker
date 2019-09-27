package ca.antonious.materialdaypicker

/**
 * An implementation of [SelectionMode] that allows users to select and
 * deselect days without any restrictions.
 */
open class DefaultSelectionMode : SelectionMode {

    override fun getSelectionStateAfterSelecting(lastSelectionState: SelectionState, dayToSelect: MaterialDayPicker.Weekday): SelectionState {
        return lastSelectionState.withDaySelected(dayToSelect)
    }

    override fun getSelectionStateAfterDeselecting(lastSelectionState: SelectionState, dayToDeselect: MaterialDayPicker.Weekday): SelectionState {
        return lastSelectionState.withDayDeselected(dayToDeselect)
    }

    companion object {
        /**
         * Creates a new instance of a [DefaultSelectionMode]
         */
        @JvmStatic
        fun create(): SelectionMode {
            return DefaultSelectionMode()
        }
    }
}
