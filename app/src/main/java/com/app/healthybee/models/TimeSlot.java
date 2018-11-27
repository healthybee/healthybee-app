package com.app.healthybee.models;

public class TimeSlot {
    private String timeSlot;
    private boolean isSelected;

    public TimeSlot(String timeSlot, boolean isSelected) {
        this.timeSlot = timeSlot;
        this.isSelected = isSelected;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }


    public String getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }
}
