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
 * An android widget that resembles the day of the week picker in the
 * stock clock app.
 *
 * All colors can be customized by overriding the following color values:
 *     dayPressed - the color when a day is being held down
 *     daySelected - the color when a day is selected
 *     dayDeselected - the color when a day is deselected
 */
class MaterialDayPicker @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : LinearLayout(context, attrs, defStyleAttr) {

    /**
     * Gets/sets the current [DayPressedListener].
     *
     * @see DayPressedListener
     */
    var dayPressedListener: DayPressedListener? = null

    /**
     * Gets/sets the current [DaySelectionChangedListener].
     *
     * @see DaySelectionChangedListener
     */
    var daySelectionChangedListener: DaySelectionChangedListener? = null

    /**
     * Gets/sets the current selection mode. Note when updating the selection mode
     * all selections will be cleared.
     *
     * eg. To only allow one day to be selected at a time do
     *     materialDayPicker.selectionMode = SingleSelectionMode.create()
     *
     * @see SelectionMode
     * @return the current selection mode
     */
    var selectionMode: SelectionMode = DefaultSelectionMode()
        set(value) {
            field = value
            clearSelectionIgnoringSelectionMode()
        }

    /**
     * Returns a list of the currently selected [Weekday]s
     */
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

    /**
     * Checks if the passed [Weekday] is currently selected
     *
     * @param weekday the day to check
     */
    fun isSelected(weekday: Weekday): Boolean {
        return selectedDays.contains(weekday)
    }

    /**
     * Updates the passed in [Weekday] to the selected state.
     *
     * @param weekday to select
     */
    fun selectDay(weekday: Weekday) {
        handleSelection(weekday)
    }

    /**
     * Updates the passed in [Weekday] to a deselected state.
     *
     * @param weekday to deselect
     */
    fun deselectDay(weekday: Weekday) {
        handleDeselection(weekday)
    }

    /**
     * Clears the current selection and replaces it with the passed in
     * [Weekday]s
     *
     * @param weekdays days to select
     */
    fun setSelectedDays(vararg weekdays: Weekday) {
        setSelectedDays(weekdays.toList())
    }

    /**
     * Clears the current selection and replaces it with the passed in
     * list of [Weekday]s
     *
     * @param weekdays days to select
     */
    fun setSelectedDays(weekdays: List<Weekday>) {
        disableListenerWhileExecuting {
            clearSelection()
            weekdays.forEach { selectDay(it) }
        }
    }

    /**
     * Clears all currently selected days
     */
    fun clearSelection() {
        disableListenerWhileExecuting {
            selectedDays.forEach { deselectDay(it) }
        }
    }

    /**
     * Sets a lambda to invoke whenever a day is selected/deselected.
     *
     * This method wraps the provided lambda into a [DayPressedListener] and
     * sets it to this instance's [dayPressedListener].
     *
     * @see DayPressedListener
     * @param onDayPressed lambda to invoke when a day is pressed
     */
    @JvmSynthetic
    fun setDayPressedListener(onDayPressed: (weekday: Weekday, isSelected: Boolean) -> Unit) {
        this.dayPressedListener = object : DayPressedListener {
            override fun onDayPressed(weekday: Weekday, isSelected: Boolean) {
                onDayPressed.invoke(weekday, isSelected)
            }
        }
    }

    /**
     * Sets a lambda to invoke whenever the selection of days has changed.
     *
     * This method wraps the provided lambda into a [DaySelectionChangedListener]
     * and sets it to this instance's [daySelectionChangedListener].
     *
     * @see DaySelectionChangedListener
     * @param onDaySelectionChanged lambda to invoke when day selection has changed
     */
    @JvmSynthetic
    fun setDaySelectionChangedListener(onDaySelectionChanged: (selectedDays: List<Weekday>) -> Unit) {
        this.daySelectionChangedListener = object : DaySelectionChangedListener {
            override fun onDaySelectionChanged(selectedDays: List<Weekday>) {
                onDaySelectionChanged.invoke(selectedDays)
            }
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

    /**
     * Representation of
     */
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

    /**
     * Provides a way to listen to any changes made to the selected days. If you need to capture
     * updates for a specific day use a [DayPressedListener]
     */
    interface DaySelectionChangedListener {
        /**
         * This is invoked when any selection change is made.
         *
         * @param selectedDays all currently selected days after the most recent update
         */
        fun onDaySelectionChanged(selectedDays: List<@JvmSuppressWildcards Weekday>)
    }

    /**
     * Provides a way to listen to any changes on a per day basis. This provides more granular
     * update information compared to [DaySelectionChangedListener]
     */
    interface DayPressedListener {
        /**
         * This is invoked when a specific day is toggled on/off
         *
         * @param weekday The day that was updated
         * @param isSelected The updated selection state for provided [weekday]
         */
        fun onDayPressed(weekday: Weekday, isSelected: Boolean)
    }
}
