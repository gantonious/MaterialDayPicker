package ca.antonious.materialdaypicker

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.ToggleButton
import kotlinx.android.synthetic.main.day_of_the_week_picker.view.friday_toggle
import kotlinx.android.synthetic.main.day_of_the_week_picker.view.monday_toggle
import kotlinx.android.synthetic.main.day_of_the_week_picker.view.saturday_toggle
import kotlinx.android.synthetic.main.day_of_the_week_picker.view.sunday_toggle
import kotlinx.android.synthetic.main.day_of_the_week_picker.view.thursday_toggle
import kotlinx.android.synthetic.main.day_of_the_week_picker.view.tuesday_toggle
import kotlinx.android.synthetic.main.day_of_the_week_picker.view.wednesday_toggle

/**
 * Created by George on 2017-11-04.
 */
class MaterialDayPicker @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : LinearLayout(context, attrs, defStyleAttr) {
    var dayPressedListener: DayPressedListener? = null
    var daySelectionChangedListener: DaySelectionChangedListener? = null

    var selectionMode: SelectionMode = DefaultSelectionMode()
        set(value) {
            field = value
            clearSelectionIgnoringSelectionMode()
        }

    val selectedDays: List<Weekday>
        get() = dayToggles.asSequence()
            .mapIndexed { index, button -> Pair(Weekday[index], button.isChecked) }
            .filter { it.second }
            .map { it.first }
            .toList()

    private val dayToggles = mutableListOf<ToggleButton>()

    private val selectionState: SelectionState
        get() = SelectionState(selectedDays)

    init {
        inflateLayoutUsing(context)
        bindViews()
        listenToToggleEvents()
    }

    fun isSelected(weekday: Weekday): Boolean {
        return selectedDays.contains(weekday)
    }

    fun selectDay(weekday: Weekday) {
        handleSelection(weekday)
    }

    fun deselectDay(weekday: Weekday) {
        handleDeselection(weekday)
    }

    fun setSelectedDays(vararg weekdays: Weekday) {
        setSelectedDays(weekdays.toList())
    }

    fun setSelectedDays(weekdays: List<Weekday>) {
        disableListenerWhileExecuting {
            clearSelection()
            weekdays.forEach { selectDay(it) }
        }
    }

    fun clearSelection() {
        disableListenerWhileExecuting {
            selectedDays.forEach { deselectDay(it) }
        }
    }

    private fun inflateLayoutUsing(context: Context) {
        LayoutInflater.from(context).inflate(R.layout.day_of_the_week_picker, this, true)
    }

    private fun bindViews() {
        dayToggles.apply {
            add(sunday_toggle)
            add(monday_toggle)
            add(tuesday_toggle)
            add(wednesday_toggle)
            add(thursday_toggle)
            add(friday_toggle)
            add(saturday_toggle)
        }
    }

    private fun listenToToggleEvents() {
        for (i in dayToggles.indices) {
            val weekdayForToggle = Weekday[i]

            dayToggles[i].setOnCheckedChangeListener { compoundButton, didGetChecked ->
                // temporally undo what the user just did so the selection mode
                // can evaluate what it should do with the intended action
                // the selection mode will generate the proper actions to
                // carry about based on the users intent in
                // applySelectionChangesUsing(SelectionDifference selectionDifference)
                ignoreToggleEvents()
                compoundButton.isChecked = !didGetChecked
                listenToToggleEvents()

                if (didGetChecked) {
                    handleSelection(weekdayForToggle)
                } else {
                    handleDeselection(weekdayForToggle)
                }
            }
        }
    }

    private fun ignoreToggleEvents() {
        for (toggleButton in dayToggles) {
            toggleButton.setOnCheckedChangeListener(null)
        }
    }

    private fun handleSelection(dayToSelect: Weekday) {
        val currentSelectionState = selectionState
        val nextSelectionState = selectionMode.getSelectionStateAfterSelecting(currentSelectionState, dayToSelect)
        val selectionDifference = selectionDifferenceOf(currentSelectionState, nextSelectionState)
        applySelectionChangesUsing(selectionDifference)
    }

    private fun handleDeselection(dayToDeselect: Weekday) {
        val currentSelectionState = selectionState
        val nextSelectionState = selectionMode.getSelectionStateAfterDeselecting(currentSelectionState, dayToDeselect)
        val selectionDifference = selectionDifferenceOf(currentSelectionState, nextSelectionState)
        applySelectionChangesUsing(selectionDifference)
    }

    private fun applySelectionChangesUsing(selectionDifference: SelectionDifference) {
        ignoreToggleEvents()

        for (dayToDeselect in selectionDifference.daysToDeselect) {
            getToggleFor(dayToDeselect).isChecked = false
            dayPressedListener?.onDayPressed(dayToDeselect, false)
        }

        for (dayToSelect in selectionDifference.daysToSelect) {
            getToggleFor(dayToSelect).isChecked = true
            dayPressedListener?.onDayPressed(dayToSelect, true)
        }

        listenToToggleEvents()
        onDaySelectionChanged()
    }

    private fun clearSelectionIgnoringSelectionMode() {
        ignoreToggleEvents()

        for (dayToggle in dayToggles) {
            dayToggle.isChecked = false
        }

        listenToToggleEvents()
    }

    private fun disableListenerWhileExecuting(action: () -> Unit) {
        val tempListener = daySelectionChangedListener
        daySelectionChangedListener = null

        action.invoke()

        daySelectionChangedListener = tempListener
        onDaySelectionChanged()
    }

    private fun getToggleFor(weekday: Weekday): ToggleButton {
        return dayToggles[weekday.ordinal]
    }

    private fun onDaySelectionChanged() {
        daySelectionChangedListener?.onDaySelectionChanged(selectedDays)
    }

    enum class Weekday {
        SUNDAY,
        MONDAY,
        TUESDAY,
        WEDNESDAY,
        THURSDAY,
        FRIDAY,
        SATURDAY;

        companion object {
            operator fun get(index: Int): Weekday {
                return Weekday.values()[index]
            }

            val allDays: List<Weekday>
                get() = Weekday.values().toList()
        }
    }

    interface DaySelectionChangedListener {
        fun onDaySelectionChanged(selectedDays: List<@JvmSuppressWildcards Weekday>)
    }

    interface DayPressedListener {
        fun onDayPressed(weekday: Weekday, isSelected: Boolean)
    }
}
