package ca.antonious.sampleapp

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity

import ca.antonious.materialdaypicker.DefaultSelectionMode
import ca.antonious.materialdaypicker.MaterialDayPicker
import ca.antonious.materialdaypicker.SingleSelectionMode
import kotlinx.android.synthetic.main.activity_sample.clearButton
import kotlinx.android.synthetic.main.activity_sample.clearLogButton
import kotlinx.android.synthetic.main.activity_sample.dayPicker
import kotlinx.android.synthetic.main.activity_sample.enabledDaysSelector
import kotlinx.android.synthetic.main.activity_sample.event_log
import kotlinx.android.synthetic.main.activity_sample.first_day_of_week_spinner
import kotlinx.android.synthetic.main.activity_sample.locale_spinner
import kotlinx.android.synthetic.main.activity_sample.singleModeSwitch
import java.util.Locale

class KotlinSampleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample)

        val materialDayPicker = dayPicker

        materialDayPicker.setDaySelectionChangedListener { selectedDays ->
            appendLog("[DaySelectionChangedListener] $selectedDays")
        }

        materialDayPicker.setDayPressedListener { weekday, isSelected ->
            appendLog("[DayPressedListener] $weekday is selected: $isSelected")
        }

        enabledDaysSelector.setDayPressedListener { weekday, isSelected ->
            materialDayPicker.setDayEnabled(weekday, isSelected)
        }

        enabledDaysSelector.selectAllDays()

        materialDayPicker.setSelectedDays(MaterialDayPicker.Weekday.MONDAY)

        clearButton.setOnClickListener {
            materialDayPicker.clearSelection()
        }

        clearLogButton.setOnClickListener {
            event_log.text = ""
        }

        singleModeSwitch.setOnCheckedChangeListener { _, didGetChecked ->
            if (didGetChecked) {
                materialDayPicker.selectionMode = SingleSelectionMode.create()
            } else {
                materialDayPicker.selectionMode = DefaultSelectionMode.create()
            }
        }

        val allLocales = Locale.getAvailableLocales()
        val defaultLocale = Locale.getDefault()

        val allWeekdays = MaterialDayPicker.Weekday.values()
        val defaultFirstDayOfWeek = MaterialDayPicker.Weekday.getFirstDayOfWeekFor(defaultLocale)

        locale_spinner.adapter = ArrayAdapter(this, R.layout.spinner_text_view, allLocales)
        locale_spinner.setSelection(allLocales.indexOf(defaultLocale))

        locale_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedLocale = allLocales[position]
                materialDayPicker.locale = selectedLocale

                val firstDayOfWeekForSelectedLocale = MaterialDayPicker.Weekday.getFirstDayOfWeekFor(selectedLocale)
                first_day_of_week_spinner.setSelection(allWeekdays.indexOf(firstDayOfWeekForSelectedLocale))
            }
        }

        first_day_of_week_spinner.adapter = ArrayAdapter(this, R.layout.spinner_text_view, allWeekdays)
        first_day_of_week_spinner.setSelection(allWeekdays.indexOf(defaultFirstDayOfWeek))

        first_day_of_week_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                materialDayPicker.firstDayOfWeek = allWeekdays[position]
            }
        }
    }

    private fun appendLog(log: String) {
        event_log.text = "$log\n" + event_log.text
    }
}
