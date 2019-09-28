package ca.antonious.sampleapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

import java.util.List;

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
    }

    private void appendLog(String logMessage) {
        eventLog.setText(logMessage + "\n" + eventLog.getText());
    }
}
