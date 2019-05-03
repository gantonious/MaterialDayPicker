package ca.antonious.sampleapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import java.util.List;

import ca.antonious.materialdaypicker.DefaultSelectionMode;
import ca.antonious.materialdaypicker.MaterialDayPicker;
import ca.antonious.materialdaypicker.SingleSelectionMode;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final MaterialDayPicker materialDayPicker = findViewById(R.id.dayPicker);
        materialDayPicker.setDaySelectionChangedListener(new MaterialDayPicker.DaySelectionChangedListener() {
            @Override
            public void onDaySelectionChanged(List<MaterialDayPicker.Weekday> selectedDays) {
                Toast.makeText(MainActivity.this, "Days Changed", Toast.LENGTH_SHORT).show();
            }
        });

        materialDayPicker.setDayPressedListener(new MaterialDayPicker.DayPressedListener() {
            @Override
            public void onDayPressed(MaterialDayPicker.Weekday weekday, boolean isSelected) {
                String message = String.format("%s is selected: %b.", weekday.toString(), isSelected);
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
}
