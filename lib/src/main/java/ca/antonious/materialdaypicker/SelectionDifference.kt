package ca.antonious.materialdaypicker

internal data class SelectionDifference(
    val daysToSelect: List<MaterialDayPicker.Weekday>,
    val daysToDeselect: List<MaterialDayPicker.Weekday>
)

internal fun selectionDifferenceOf(
    initialSelectionState: SelectionState,
    finalSelectionState: SelectionState
): SelectionDifference {

    val initialSelections = initialSelectionState.selectedDays
    val finalSelections = finalSelectionState.selectedDays

    return SelectionDifference(
        daysToDeselect = initialSelections.filterNot { it in finalSelections },
        daysToSelect = finalSelections.filterNot { it in initialSelections }
    )
}
