package ca.antonious.materialdaypicker

import org.junit.Test

class DefaultSelectionModeTest {

    @Test
    fun `Test selection`() {
        assertSelection(
            selectionMode = DefaultSelectionMode(),
            initialSelections = listOf(
                MaterialDayPicker.Weekday.MONDAY,
                MaterialDayPicker.Weekday.WEDNESDAY,
                MaterialDayPicker.Weekday.FRIDAY
            ),
            expectedSelections = listOf(
                MaterialDayPicker.Weekday.MONDAY,
                MaterialDayPicker.Weekday.WEDNESDAY,
                MaterialDayPicker.Weekday.FRIDAY,
                MaterialDayPicker.Weekday.SATURDAY
            ),
            dayToSelect = MaterialDayPicker.Weekday.SATURDAY
        )
    }

    @Test
    fun `Test deselection`() {
        assertDeselection(
            selectionMode = DefaultSelectionMode(),
            initialSelections = listOf(
                MaterialDayPicker.Weekday.MONDAY,
                MaterialDayPicker.Weekday.WEDNESDAY,
                MaterialDayPicker.Weekday.FRIDAY
            ),
            expectedSelections = listOf(
                MaterialDayPicker.Weekday.MONDAY,
                MaterialDayPicker.Weekday.FRIDAY
            ),
            dayToDeselect = MaterialDayPicker.Weekday.WEDNESDAY
        )
    }
}
