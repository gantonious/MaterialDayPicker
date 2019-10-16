package ca.antonious.materialdaypicker

import android.content.Context
import android.os.Parcelable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.ToggleButton
import java.lang.IllegalStateException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.day_of_the_week_picker.view.toggle_0
import kotlinx.android.synthetic.main.day_of_the_week_picker.view.toggle_1
import kotlinx.android.synthetic.main.day_of_the_week_picker.view.toggle_2
import kotlinx.android.synthetic.main.day_of_the_week_picker.view.toggle_3
import kotlinx.android.synthetic.main.day_of_the_week_picker.view.toggle_4
import kotlinx.android.synthetic.main.day_of_the_week_picker.view.toggle_5
import kotlinx.android.synthetic.main.day_of_the_week_picker.view.toggle_6

/**
 * An android widget that resembles the day of the week picker in the
 * stock clock app.
 *
 * All colors can be customized by overriding the following color values:
 *     dayPressed - the color when a day is being held down
 *     daySelected - the color when a day is selected
 *     dayDeselected - the color when a day is deselected
 */
class MaterialDayPicker @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

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
     * Gets/sets the current locale of the day picker. This is set to [Locale.getDefault]
     * by default which will use the language/locale configured by the user's device.
     *
     * This is used to:
     *   - Render the abbreviation of a weekday for this [Locale]
     *   - Render the first day of the week for this [Locale]
     *
     * @return the current locale being used
     */
    var locale: Locale = Locale.getDefault()
        set(newLocale) {
            val currentSelections = selectedDays
            field = newLocale
            firstDayOfWeek = Weekday.getFirstDayOfWeekFor(locale = newLocale)

            localizeLabels()
            setDaysIgnoringListenersAndSelectionMode(daysToSelect = currentSelections)
        }

    /**
     * Returns a list of the currently selected [Weekday]s
     */
    val selectedDays: List<Weekday>
        get() = getDayTogglesMatchedWithWeekday()
            .filter { (toggle, _) -> toggle.isChecked }
            .map { (_, weekday) -> weekday }

    private var firstDayOfWeek: Weekday = Weekday.getFirstDayOfWeekFor(locale = locale)

    private val dayToggles = mutableListOf<ToggleButton>()

    private val selectionState: SelectionState
        get() = SelectionState(selectedDays)

    init {
        inflateLayoutUsing(context)
        bindViews()
        listenToToggleEvents()
    }

    override fun onSaveInstanceState(): Parcelable? {
        return SavedStateData(
            superState = super.onSaveInstanceState(),
            selectedDays = selectedDays.toSet()
        )
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        ignoreToggleEvents()
        val savedStateData = state as? SavedStateData

        super.onRestoreInstanceState(savedStateData?.superState)
        post { restoreSelectionState(savedStateData) }
    }

    private fun restoreSelectionState(savedStateData: SavedStateData?) {
        if (savedStateData == null) {
            listenToToggleEvents()
            return
        }

        forEachToggleAndWeekday { toggleButton, weekday ->
            toggleButton.isChecked = weekday in savedStateData.selectedDays
        }

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
            add(toggle_0)
            add(toggle_1)
            add(toggle_2)
            add(toggle_3)
            add(toggle_4)
            add(toggle_5)
            add(toggle_6)
        }

        localizeLabels()
    }

    private fun localizeLabels() {
        ignoreToggleEvents()

        forEachToggleAndWeekday { toggle, weekday ->
            toggle.withLocalizedLabel(weekday)
        }

        listenToToggleEvents()
    }

    private fun ToggleButton.withLocalizedLabel(weekday: Weekday): ToggleButton {
        val abbreviation = weekday.getAbbreviationFor(locale)
        textOn = abbreviation
        textOff = abbreviation
        isChecked = false // we will restore the button state in onRestoreInstanceState
        return this
    }

    private fun listenToToggleEvents() {
        forEachToggleAndWeekday { toggle, weekday ->
            toggle.setOnCheckedChangeListener { compoundButton, didGetChecked ->
                // temporally undo what the user just did so the selection mode
                // can evaluate what it should do with the intended action
                // the selection mode will generate the proper actions to
                // carry about based on the users intent in
                // applySelectionChangesUsing(SelectionDifference selectionDifference)
                ignoreToggleEvents()
                compoundButton.isChecked = !didGetChecked
                listenToToggleEvents()

                if (didGetChecked) {
                    handleSelection(weekday)
                } else {
                    handleDeselection(weekday)
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
        setDaysIgnoringListenersAndSelectionMode(daysToSelect = emptyList())
    }

    private fun setDaysIgnoringListenersAndSelectionMode(daysToSelect: List<Weekday>) {
        ignoreToggleEvents()

        forEachToggleAndWeekday { toggle, weekday ->
            toggle.isChecked = weekday in daysToSelect
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

    private fun getDayTogglesMatchedWithWeekday(): List<Pair<ToggleButton, Weekday>> {
        return dayToggles.zip(Weekday.getOrderedDaysOfWeek(locale = locale))
    }

    private fun forEachToggleAndWeekday(action: (toggleButton: ToggleButton, weekday: Weekday) -> Unit) {
        getDayTogglesMatchedWithWeekday().forEach { (toggle, weekday) ->
            action.invoke(toggle, weekday)
        }
    }

    private fun getToggleFor(weekday: Weekday): ToggleButton {
        var weekdayOffsetRelativeToWeekStartingOnSunday = weekday.ordinal - firstDayOfWeek.ordinal

        if (weekdayOffsetRelativeToWeekStartingOnSunday < 0) {
            weekdayOffsetRelativeToWeekStartingOnSunday += Weekday.values().size
        }

        return dayToggles[weekdayOffsetRelativeToWeekStartingOnSunday]
    }

    private fun onDaySelectionChanged() {
        daySelectionChangedListener?.onDaySelectionChanged(selectedDays)
    }

    /**
     * A representation of a day of the week.
     */
    enum class Weekday {
        SUNDAY,
        MONDAY,
        TUESDAY,
        WEDNESDAY,
        THURSDAY,
        FRIDAY,
        SATURDAY;

        /**
         * Gets a localized abbreviation of this [Weekday].
         *
         * i.e. In an english based locale:
         *
         * ```kotlin
         *     Weekday.MONDAY.abbreviation == "M"
         *     Weekday.THURSDAY.abbreviation == "T"
         * ```
         *
         * @param locale the locale which the abbreviation should be translated for
         * @return The abbreviation as a string
         */
        fun getAbbreviationFor(locale: Locale): String {
            val dayOfWeek = when (this) {
                SUNDAY -> Calendar.SUNDAY
                MONDAY -> Calendar.MONDAY
                TUESDAY -> Calendar.TUESDAY
                WEDNESDAY -> Calendar.WEDNESDAY
                THURSDAY -> Calendar.THURSDAY
                FRIDAY -> Calendar.FRIDAY
                SATURDAY -> Calendar.SATURDAY
            }

            val calendar = Calendar.getInstance().apply {
                set(Calendar.DAY_OF_WEEK, dayOfWeek)
            }

            return SimpleDateFormat("EEEEE", locale).format(calendar.time)
        }

        companion object {
            operator fun get(index: Int): Weekday {
                return Weekday.values()[index]
            }

            val allDays: List<Weekday>
                get() = Weekday.values().toList()

            /**
             * Gets a list of [Weekday]s starting with the first day of the week
             * for a given [locale]
             *
             * @param locale the locale to evaluate the first day for
             * @return A list of [Weekday]s starting on the first day of
             * the week for the given locale
             */
            fun getOrderedDaysOfWeek(locale: Locale): List<Weekday> {
                val daysOfTheWeekStartingOnSunday = allDays
                val firstDayOfWeek = getFirstDayOfWeekFor(locale = locale)
                val indexOfFirstDay = daysOfTheWeekStartingOnSunday.indexOf(firstDayOfWeek)
                val daysToMoveToEndOfWeek = daysOfTheWeekStartingOnSunday.take(indexOfFirstDay)
                return daysOfTheWeekStartingOnSunday.drop(indexOfFirstDay) + daysToMoveToEndOfWeek
            }

            /**
             * Gets the first day of the calendar week for a given [locale]
             *
             * @param locale the locale to evaluate the first day for
             * @return The [Weekday] this week starts on for the given [locale]
             */
            fun getFirstDayOfWeekFor(locale: Locale = Locale.getDefault()): Weekday {
                return when (val firstDayOfWeek = Calendar.getInstance(locale).firstDayOfWeek) {
                    Calendar.SUNDAY -> SUNDAY
                    Calendar.MONDAY -> MONDAY
                    Calendar.TUESDAY -> TUESDAY
                    Calendar.WEDNESDAY -> WEDNESDAY
                    Calendar.THURSDAY -> THURSDAY
                    Calendar.FRIDAY -> FRIDAY
                    Calendar.SATURDAY -> SATURDAY
                    else -> throw IllegalStateException("Failed to resolve first day of week matching $firstDayOfWeek")
                }
            }
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

    @Parcelize
    private data class SavedStateData(
        val superState: Parcelable?,
        val selectedDays: Set<Weekday>
    ) : Parcelable
}
