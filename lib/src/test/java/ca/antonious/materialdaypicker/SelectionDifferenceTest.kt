package ca.antonious.materialdaypicker

import org.junit.Assert.assertEquals
import org.junit.Test

class SelectionDifferenceTest {

    @Test
    fun `Test evaluating difference`() {
        val expectedSelectionDifference = SelectionDifference(
            daysToDeselect = listOf(
                MaterialDayPicker.Weekday.WEDNESDAY
            ),
            daysToSelect = listOf(
                MaterialDayPicker.Weekday.MONDAY,
                MaterialDayPicker.Weekday.FRIDAY
            )
        )

        val actualSelectionDifference = selectionDifferenceOf(
            initialSelectionState = SelectionState(
                listOf(
                    MaterialDayPicker.Weekday.TUESDAY,
                    MaterialDayPicker.Weekday.WEDNESDAY
                )
            ),
            finalSelectionState = SelectionState(
                listOf(
                    MaterialDayPicker.Weekday.MONDAY,
                    MaterialDayPicker.Weekday.TUESDAY,
                    MaterialDayPicker.Weekday.FRIDAY
                )
            )
        )

        assertEquals(expectedSelectionDifference, actualSelectionDifference)
    }
}
