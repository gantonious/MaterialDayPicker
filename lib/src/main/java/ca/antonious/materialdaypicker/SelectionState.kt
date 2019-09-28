package ca.antonious.materialdaypicker

/**
 * A representation of the current days selected with convenience methods
 * to allow for easily transforming into a new [SelectionState].
 */
data class SelectionState @JvmOverloads constructor(
    val selectedDays: List<MaterialDayPicker.Weekday> = emptyList()
) {

    /**
     * Creates a new [SelectionState] with [dayToSelect] selected.
     *
     * @param dayToSelect the day to select
     * @return a new instance of a [SelectionState] with [dayToSelect] selected
     */
    fun withDaySelected(dayToSelect: MaterialDayPicker.Weekday): SelectionState {
        return SelectionState(selectedDays + dayToSelect)
    }

    /**
     * Creates a new [SelectionState] with [dayToDeselect] deselected.
     *
     * @param dayToDeselect the day to deselect
     * @return a new instance of a [SelectionState] with [dayToDeselect] deselected
     */
    fun withDayDeselected(dayToDeselect: MaterialDayPicker.Weekday): SelectionState {
        return SelectionState(selectedDays - dayToDeselect)
    }

    companion object {
        /**
         * Creates a [SelectionState] with only [day] selected
         *
         * @param day the day to select
         * @return a new instance of a [SelectionState] with only [day] selected
         */
        fun withSingleDay(day: MaterialDayPicker.Weekday): SelectionState {
            return SelectionState().withDaySelected(day)
        }
    }
}
