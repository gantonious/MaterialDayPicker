package ca.antonious.sampleapp

import android.os.Bundle
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatActivity

import ca.antonious.materialdaypicker.DefaultSelectionMode
import ca.antonious.materialdaypicker.MaterialDayPicker
import ca.antonious.materialdaypicker.SingleSelectionMode
import kotlinx.android.synthetic.main.activity_sample.clearButton
import kotlinx.android.synthetic.main.activity_sample.clearLogButton
import kotlinx.android.synthetic.main.activity_sample.dayPicker
import kotlinx.android.synthetic.main.activity_sample.event_log
import kotlinx.android.synthetic.main.activity_sample.singleModeSwitch

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

        materialDayPicker.setSelectedDays(MaterialDayPicker.Weekday.MONDAY)

        clearButton.setOnClickListener {
            materialDayPicker.clearSelection()
        }

        clearLogButton.setOnClickListener {
            event_log.text = ""
        }

        singleModeSwitch.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { compoundButton, didGetChecked ->
            if (didGetChecked) {
                materialDayPicker.selectionMode = SingleSelectionMode.create()
            } else {
                materialDayPicker.selectionMode = DefaultSelectionMode.create()
            }
        })
    }

    private fun appendLog(log: String) {
        event_log.text = "$log\n" + event_log.text
    }
}
