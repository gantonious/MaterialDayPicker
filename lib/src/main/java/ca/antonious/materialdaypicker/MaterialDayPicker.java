package ca.antonious.materialdaypicker;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by George on 2017-11-04.
 */

public class MaterialDayPicker extends LinearLayout {
    private List<ToggleButton> dayToggles = new ArrayList<>();

    private DaySelectionChangedListener daySelectionChangedListener;
    private DayPressedListener dayPressedListener;

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

    public void setDaySelectionChangedListener(DaySelectionChangedListener daySelectionChangedListener) {
        this.daySelectionChangedListener = daySelectionChangedListener;
    }

    public void setDayPressedListener(DayPressedListener dayPressedListener) {
        this.dayPressedListener = dayPressedListener;
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

    public void selectDay(final Weekday weekday) {
        disableListenerWhileExecuting(new Action() {
            @Override
            public void call() {
                getToggleFor(weekday).setChecked(true);
            }
        });
    }

    public void deselectDay(final Weekday weekday) {
        disableListenerWhileExecuting(new Action() {
            @Override
            public void call() {
                getToggleFor(weekday).setChecked(false);
            }
        });
    }

    public boolean isSelected(Weekday weekday) {
        return getSelectedDays().contains(weekday);
    }

    public void clearSelection() {
        disableListenerWhileExecuting(new Action() {
            @Override
            public void call() {
                for (ToggleButton toggleButton: dayToggles) {
                    toggleButton.setChecked(false);
                }
            }
        });
    }

    public void setSelectedDays(final Weekday... weekdays) {
        setSelectedDays(Arrays.asList(weekdays));
    }

    public void setSelectedDays(final List<Weekday> weekdays) {
        disableListenerWhileExecuting(new Action() {
            @Override
            public void call() {
                clearSelection();

                for (Weekday weekday: weekdays) {
                    getToggleFor(weekday).setChecked(true);
                }
            }
        });
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
        for (int i = 0; i < dayToggles.size(); i++) {
            final Weekday weekdayForToggle = Weekday.values()[i];

            dayToggles.get(i).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    onDayPressed(weekdayForToggle);
                    onDaySelectionChanged();
                }
            });
        }
    }

    private void disableListenerWhileExecuting(Action action) {
        DaySelectionChangedListener tempListener = daySelectionChangedListener;
        daySelectionChangedListener = null;

        action.call();

        daySelectionChangedListener = tempListener;
        onDaySelectionChanged();
    }

    private ToggleButton getToggleFor(Weekday weekday) {
        return dayToggles.get(weekday.ordinal());
    }

    private void onDaySelectionChanged() {
        if (daySelectionChangedListener != null) {
            daySelectionChangedListener.onDaySelectionChanged(getSelectedDays());
        }
    }

    private void onDayPressed(Weekday weekday) {
        if (dayPressedListener != null) {
            dayPressedListener.onDayPressed(weekday, isSelected(weekday));
        }
    }

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

    public interface DayPressedListener {
        void onDayPressed(Weekday weekday, boolean isSelected);
    }

    private interface Action {
        void call();
    }
}
