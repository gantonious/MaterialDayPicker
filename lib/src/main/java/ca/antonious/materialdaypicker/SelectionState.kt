package ca.antonious.materialdaypicker

data class SelectionState @JvmOverloads constructor(val selectedDays: List<MaterialDayPicker.Weekday> = emptyList()) {

    fun withDaySelected(dayToSelect: MaterialDayPicker.Weekday): SelectionState {
        return SelectionState(selectedDays + dayToSelect)
    }

    fun withDayDeselected(dayToDeselect: MaterialDayPicker.Weekday): SelectionState {
        return SelectionState(selectedDays - dayToDeselect)
    }

    companion object {
        fun withSingleDay(day: MaterialDayPicker.Weekday): SelectionState {
            return SelectionState().withDaySelected(day)
        }
    }
}
