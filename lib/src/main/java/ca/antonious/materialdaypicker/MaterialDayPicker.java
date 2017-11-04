package ca.antonious.materialdaypicker;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by George on 2017-11-04.
 */

public class MaterialDayPicker extends LinearLayout {
    private List<ToggleButton> dayToggles = new ArrayList<>();

    private DaySelectionChangedListener daySelectionChangedListener;

    public MaterialDayPicker(Context context) {
        this(context, null);
    }

    public MaterialDayPicker(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MaterialDayPicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        inflateLayout(context);
        bindViews();
        handleToggleEvents();
    }

    public void clearSelection() {
        DaySelectionChangedListener tempListener = daySelectionChangedListener;
        daySelectionChangedListener = null;

        for (ToggleButton toggleButton: dayToggles) {
            toggleButton.setChecked(false);
        }

        daySelectionChangedListener = tempListener;
        onDaySelectionChanged();
    }

    public List<Weekday> getSelectedDays() {
        List<Weekday> selectedDays = new ArrayList<>();

        for (int i = 0; i < dayToggles.size(); i++) {
            if (dayToggles.get(i).isChecked()) {
                selectedDays.add(Weekday.values()[i]);
            }
        }

        return selectedDays;
    }

    public void setDaySelectionChangedListener(DaySelectionChangedListener daySelectionChangedListener) {
        this.daySelectionChangedListener = daySelectionChangedListener;
    }

    private void inflateLayout(Context context) {
        LayoutInflater.from(context).inflate(R.layout.day_of_the_week_picker, this, true);
    }

    private void bindViews() {
        dayToggles.add((ToggleButton) findViewById(R.id.sunday_toggle));
        dayToggles.add((ToggleButton) findViewById(R.id.monday_toggle));
        dayToggles.add((ToggleButton) findViewById(R.id.tuesday_toggle));
        dayToggles.add((ToggleButton) findViewById(R.id.wednesday_toggle));
        dayToggles.add((ToggleButton) findViewById(R.id.thursday_toggle));
        dayToggles.add((ToggleButton) findViewById(R.id.friday_toggle));
        dayToggles.add((ToggleButton) findViewById(R.id.saturday_toggle));
    }

    private void handleToggleEvents() {
        for (ToggleButton dayToggle: dayToggles) {
            dayToggle.setOnCheckedChangeListener(onDayToggledListener);
        }
    }

    private void onDaySelectionChanged() {
        if (daySelectionChangedListener != null) {
            daySelectionChangedListener.onDaySelectionChanged(getSelectedDays());
        }
    }

    private CompoundButton.OnCheckedChangeListener onDayToggledListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            onDaySelectionChanged();
        }
    };

    public enum Weekday {
        SUNDAY,
        MONDAY,
        TUESDAY,
        WEDNESDAY,
        THURSDAY,
        FRIDAY,
        SATURDAY
    }

    public interface DaySelectionChangedListener {
        void onDaySelectionChanged(List<Weekday> selectedDays);
    }
}
