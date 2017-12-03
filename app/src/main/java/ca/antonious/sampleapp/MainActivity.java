package ca.antonious.sampleapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.util.List;

import ca.antonious.materialdaypicker.MaterialDayPicker;

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

        materialDayPicker.setSelectedDays(MaterialDayPicker.Weekday.MONDAY);
    }
}
