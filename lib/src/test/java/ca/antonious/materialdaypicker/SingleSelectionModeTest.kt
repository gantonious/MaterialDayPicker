package ca.antonious.materialdaypicker

import org.junit.Test

class SingleSelectionModeTest {
    @Test
    fun `Test selection`() {
        assertSelection(
            selectionMode = SingleSelectionMode(),
            initialSelections = listOf(MaterialDayPicker.Weekday.MONDAY),
            expectedSelections = listOf(MaterialDayPicker.Weekday.SATURDAY),
            dayToSelect = MaterialDayPicker.Weekday.SATURDAY
        )
    }

    @Test
    fun `Test deselection`() {
        assertDeselection(
            selectionMode = DefaultSelectionMode(),
            initialSelections = listOf(MaterialDayPicker.Weekday.WEDNESDAY),
            expectedSelections = emptyList(),
            dayToDeselect = MaterialDayPicker.Weekday.WEDNESDAY
        )
    }
}
