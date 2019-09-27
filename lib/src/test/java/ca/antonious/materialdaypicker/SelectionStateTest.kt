package ca.antonious.materialdaypicker

import org.junit.Assert.assertEquals
import org.junit.Test

class SelectionStateTest {
    @Test
    fun `Test selecting a day`() {
        val expectedSelectionState = SelectionState(
            listOf(
                MaterialDayPicker.Weekday.MONDAY,
                MaterialDayPicker.Weekday.TUESDAY
            )
        )

        val actualSelectionState = SelectionState(listOf(MaterialDayPicker.Weekday.MONDAY))
            .withDaySelected(MaterialDayPicker.Weekday.TUESDAY)

        assertEquals(expectedSelectionState, actualSelectionState)
    }

    @Test
    fun `Test deselecting a day`() {
        val expectedSelectionState = SelectionState(emptyList())

        val actualSelectionState = SelectionState(listOf(MaterialDayPicker.Weekday.MONDAY))
            .withDayDeselected(MaterialDayPicker.Weekday.MONDAY)

        assertEquals(expectedSelectionState, actualSelectionState)
    }

    @Test
    fun `Test selection with single day`() {
        val expectedSelectionState = SelectionState(listOf(MaterialDayPicker.Weekday.MONDAY))
        val actualSelectionState = SelectionState.withSingleDay(MaterialDayPicker.Weekday.MONDAY)

        assertEquals(expectedSelectionState, actualSelectionState)
    }
}
