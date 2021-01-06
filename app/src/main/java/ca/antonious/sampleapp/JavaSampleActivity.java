package ca.antonious.sampleapp;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import ca.antonious.materialdaypicker.DefaultSelectionMode;
import ca.antonious.materialdaypicker.MaterialDayPicker;
import ca.antonious.materialdaypicker.SingleSelectionMode;

public class JavaSampleActivity extends AppCompatActivity {
    private TextView eventLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);

        eventLog = findViewById(R.id.event_log);

        final MaterialDayPicker materialDayPicker = findViewById(R.id.dayPicker);
        materialDayPicker.setDaySelectionChangedListener(new MaterialDayPicker.DaySelectionChangedListener() {
            @Override
            public void onDaySelectionChanged(@NonNull List<MaterialDayPicker.Weekday> selectedDays) {
                appendLog(String.format("[DaySelectionChangedListener]%s", selectedDays.toString()));
            }
        });

        materialDayPicker.setDayPressedListener(new MaterialDayPicker.DayPressedListener() {
            @Override
            public void onDayPressed(@NonNull MaterialDayPicker.Weekday weekday, boolean isSelected) {
                appendLog(String.format("[DayPressedListener] %s is selected: %b", weekday.toString(), isSelected));
            }
        });

        materialDayPicker.setSelectedDays(MaterialDayPicker.Weekday.MONDAY);

        final MaterialDayPicker enabledDaysPicker = findViewById(R.id.enabledDaysSelector);
        enabledDaysPicker.selectAllDays();
        enabledDaysPicker.setDayPressedListener(new MaterialDayPicker.DayPressedListener() {
            @Override
            public void onDayPressed(@NotNull MaterialDayPicker.Weekday weekday, boolean isSelected) {
                materialDayPicker.setDayEnabled(weekday, isSelected);
            }
        });

        Button clearButton = findViewById(R.id.clearButton);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                materialDayPicker.clearSelection();
            }
        });

        Button clearLogButton = findViewById(R.id.clearLogButton);
        clearLogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventLog.setText("");
            }
        });

        Switch singleModeSwitch = findViewById(R.id.singleModeSwitch);
        singleModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean didGetChecked) {
                if (didGetChecked) {
                    materialDayPicker.setSelectionMode(SingleSelectionMode.create());
                } else {
                    materialDayPicker.setSelectionMode(DefaultSelectionMode.create());
                }
            }
        });

        final List<Locale> allLocales = Arrays.asList(Locale.getAvailableLocales());
        Locale defaultLocale = Locale.getDefault();

        final List<MaterialDayPicker.Weekday> allWeekdays = Arrays.asList(MaterialDayPicker.Weekday.values());
        MaterialDayPicker.Weekday defaultFirstDayOfWeek = MaterialDayPicker.Weekday.Companion.getFirstDayOfWeekFor(defaultLocale);

        final Spinner localeSpinner = findViewById(R.id.locale_spinner);
        final Spinner firstDayOfWeekSpinner = findViewById(R.id.first_day_of_week_spinner);

        localeSpinner.setAdapter(new ArrayAdapter<Locale>(this, R.layout.spinner_text_view, allLocales));

        localeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Locale selectedLocale = allLocales.get(position);
                materialDayPicker.setLocale(selectedLocale);

                MaterialDayPicker.Weekday firstDayOfWeekForSelectedLocale = MaterialDayPicker.Weekday.Companion.getFirstDayOfWeekFor(selectedLocale);
                firstDayOfWeekSpinner.setSelection(allWeekdays.indexOf(firstDayOfWeekForSelectedLocale));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // no-op
            }
        });

        localeSpinner.setSelection(allLocales.indexOf(defaultLocale));

        firstDayOfWeekSpinner.setAdapter(new ArrayAdapter<MaterialDayPicker.Weekday>(this, R.layout.spinner_text_view, allWeekdays));

        firstDayOfWeekSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                materialDayPicker.setFirstDayOfWeek(allWeekdays.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // no-op
            }
        });

        firstDayOfWeekSpinner.setSelection(allWeekdays.indexOf(defaultFirstDayOfWeek));
    }

    private void appendLog(String logMessage) {
        eventLog.setText(logMessage + "\n" + eventLog.getText());
    }
}
