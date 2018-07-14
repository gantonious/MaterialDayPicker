package ca.antonious.sampleapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.util.List;

import ca.antonious.materialdaypicker.MaterialDayPicker;
import ca.antonious.materialdaypicker.SingleSelectionMode;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MaterialDayPicker materialDayPicker = findViewById(R.id.dayPicker);
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
                //Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });

        //materialDayPicker.setSelectionMode(new SingleSelectionMode());
        materialDayPicker.setSelectedDays(MaterialDayPicker.Weekday.MONDAY);
    }
}
