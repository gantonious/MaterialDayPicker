package ca.antonious.materialdaypicker;

import java.util.ArrayList;
import java.util.List;

public class SelectionDifference {
    private List<MaterialDayPicker.Weekday> daysToSelect;
    private List<MaterialDayPicker.Weekday> daysToDeselect;

    public SelectionDifference(SelectionState initialSelectionState, SelectionState finalSelectionState) {
        daysToSelect = new ArrayList<>();
        daysToDeselect = new ArrayList<>();

        List<MaterialDayPicker.Weekday> initialSelections = initialSelectionState.getSelectedDays();
        List<MaterialDayPicker.Weekday> finalSelections = finalSelectionState.getSelectedDays();

        for (MaterialDayPicker.Weekday day: initialSelections) {
            // if final selections did not contain an initial selection
            // then we should deselect that day
            if (!finalSelections.contains(day)) {
                daysToDeselect.add(day);
            }
        }

        for (MaterialDayPicker.Weekday day: finalSelections) {
            // if initial selection did not contain a final selection
            // then we should select that day
            if (!initialSelections.contains(day)) {
                daysToSelect.add(day);
            }
        }
    }

    public List<MaterialDayPicker.Weekday> getDaysToSelect() {
        return new ArrayList<>(daysToSelect);
    }

    public List<MaterialDayPicker.Weekday> getDaysToDeselect() {
        return new ArrayList<>(daysToDeselect);
    }
}
