package ca.antonious.materialdaypicker

/**
 * A [SelectionMode] intercepts when a day is selected/deselected
 * and allows custom logic to be run to determine the next [SelectionState].
 *
 * eg. [SingleSelectionMode] only allows one day to be selected at a time
 *
 * @see SingleSelectionMode
 */
interface SelectionMode {

    /**
     * Takes the last [SelectionState] and transforms it to the next
     * [SelectionState] based on the day the user just selected
     *
     * @param lastSelectionState the last [SelectionState]
     * @param dayToSelect the [MaterialDayPicker.Weekday] that was just selected
     * @return An updated [SelectionState] taking into account the day selected
     */
    fun getSelectionStateAfterSelecting(
        lastSelectionState: SelectionState,
        dayToSelect: MaterialDayPicker.Weekday
    ): SelectionState

    /**
     * Takes the last [SelectionState] and transforms it to the next
     * [SelectionState] based on the day the user just deselected
     *
     * @param lastSelectionState the last [SelectionState]
     * @param dayToSelect the [MaterialDayPicker.Weekday] that was just deselected
     * @return An updated [SelectionState] taking into account the day deselected
     */
    fun getSelectionStateAfterDeselecting(
        lastSelectionState: SelectionState,
        dayToDeselect: MaterialDayPicker.Weekday
    ): SelectionState
}
