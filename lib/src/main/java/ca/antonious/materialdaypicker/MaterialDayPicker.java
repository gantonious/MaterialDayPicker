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
    private SelectionMode selectionMode = new DefaultSelectionMode();

    private DayPressedListener dayPressedListener;
    private DaySelectionChangedListener daySelectionChangedListener;

    public MaterialDayPicker(Context context) {
        this(context, null);
    }

    public MaterialDayPicker(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MaterialDayPicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        inflateLayoutUsing(context);
        bindViews();
        listenToToggleEvents();
    }

    public void setSelectionMode(SelectionMode selectionMode) {
        if (selectionMode == null) {
            throw new IllegalArgumentException("SelectionMode must not be null.");
        }
        this.selectionMode = selectionMode;
        handleChangesInAvailableDays();
    }

    public void setDayPressedListener(DayPressedListener dayPressedListener) {
        this.dayPressedListener = dayPressedListener;
    }

    public void setDaySelectionChangedListener(DaySelectionChangedListener daySelectionChangedListener) {
        this.daySelectionChangedListener = daySelectionChangedListener;
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

    public boolean isSelected(Weekday weekday) {
        return getSelectedDays().contains(weekday);
    }

    public void selectDay(final Weekday weekday) {
        handleSelection(weekday);
    }

    public void deselectDay(final Weekday weekday) {
        handleDeselection(weekday);
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
                    selectDay(weekday);
                }
            }
        });
    }

    public void clearSelection() {
        disableListenerWhileExecuting(new Action() {
            @Override
            public void call() {
                for (Weekday selectedDay: getSelectedDays()) {
                    deselectDay(selectedDay);
                }
            }
        });
    }

    private void inflateLayoutUsing(Context context) {
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

    private void listenToToggleEvents() {
        for (int i = 0; i < dayToggles.size(); i++) {
            final Weekday weekdayForToggle = Weekday.values()[i];

            dayToggles.get(i).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean didGetChecked) {
                    if (didGetChecked) {
                        handleSelection(weekdayForToggle);
                    } else {
                        handleDeselection(weekdayForToggle);
                    }
                }
            });
        }
    }

    private void ignoreToggleEvents() {
        for (ToggleButton toggleButton: dayToggles) {
            toggleButton.setOnCheckedChangeListener(null);
        }
    }

    private void handleSelection(Weekday dayToSelect) {
        SelectionState currentSelectionState = getSelectionState();
        SelectionState nextSelectionState = selectionMode.getSelectionStateAfterSelecting(currentSelectionState, dayToSelect);
        SelectionDifference selectionDifference = new SelectionDifference(currentSelectionState, nextSelectionState);
        applySelectionChangesUsing(selectionDifference);
    }

    private void handleDeselection(Weekday dayToDeselect) {
        SelectionState currentSelectionState = getSelectionState();
        SelectionState nextSelectionState = selectionMode.getSelectionStateAfterDeselecting(currentSelectionState, dayToDeselect);
        SelectionDifference selectionDifference = new SelectionDifference(currentSelectionState, nextSelectionState);
        applySelectionChangesUsing(selectionDifference);
    }

    private void applySelectionChangesUsing(SelectionDifference selectionDifference) {
        ignoreToggleEvents();

        for (Weekday dayToDeselect: selectionDifference.getDaysToDeselect()) {
            getToggleFor(dayToDeselect).setChecked(false);
            onDayPressed(dayToDeselect, false);
        }

        for (Weekday dayToSelect: selectionDifference.getDaysToSelect()) {
            getToggleFor(dayToSelect).setChecked(true);
            onDayPressed(dayToSelect, true);
        }

        handleChangesInAvailableDays();
        listenToToggleEvents();
        onDaySelectionChanged();
    }

    private void handleChangesInAvailableDays() {
        for (ToggleButton dayToggle: dayToggles) {
            dayToggle.setEnabled(false);
        }

        for (Weekday day: selectionMode.getSelectableDays()) {
            getToggleFor(day).setEnabled(true);
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

    private SelectionState getSelectionState() {
        return new SelectionState(getSelectedDays());
    }

    private void onDaySelectionChanged() {
        if (daySelectionChangedListener != null) {
            daySelectionChangedListener.onDaySelectionChanged(getSelectedDays());
        }
    }

    private void onDayPressed(Weekday weekday, boolean didGetSelected) {
        if (dayPressedListener != null) {
            dayPressedListener.onDayPressed(weekday, didGetSelected);
        }
    }

    public enum Weekday {
        SUNDAY,
        MONDAY,
        TUESDAY,
        WEDNESDAY,
        THURSDAY,
        FRIDAY,
        SATURDAY;

        public static List<Weekday> getAllDays() {
            return Arrays.asList(Weekday.values());
        }
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
