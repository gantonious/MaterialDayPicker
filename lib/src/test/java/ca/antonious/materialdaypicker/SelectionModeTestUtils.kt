package ca.antonious.materialdaypicker

import org.junit.Assert.assertEquals

internal fun assertSelection(
    selectionMode: SelectionMode,
    initialSelections: List<MaterialDayPicker.Weekday>,
    expectedSelections: List<MaterialDayPicker.Weekday>,
    dayToSelect: MaterialDayPicker.Weekday
) {
    val lastSelectionState = SelectionState(selectedDays = initialSelections)
    val expectedSelectionState = SelectionState(selectedDays = expectedSelections)

    val actualSelectionState = selectionMode.getSelectionStateAfterSelecting(
        lastSelectionState = lastSelectionState,
        dayToSelect = dayToSelect
    )

    assertEquals(expectedSelectionState, actualSelectionState)
}

internal fun assertDeselection(
    selectionMode: SelectionMode,
    initialSelections: List<MaterialDayPicker.Weekday>,
    expectedSelections: List<MaterialDayPicker.Weekday>,
    dayToDeselect: MaterialDayPicker.Weekday
) {
    val lastSelectionState = SelectionState(selectedDays = initialSelections)
    val expectedSelectionState = SelectionState(selectedDays = expectedSelections)

    val actualSelectionState = selectionMode.getSelectionStateAfterDeselecting(
        lastSelectionState = lastSelectionState,
        dayToDeselect = dayToDeselect
    )

    assertEquals(expectedSelectionState, actualSelectionState)
}