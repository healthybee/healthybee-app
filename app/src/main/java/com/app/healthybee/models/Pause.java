package com.app.healthybee.models;

/**
 * Created by root on 21/9/18.
 */

public class Pause {
    private String OrderTitle;
    private boolean isChecked;

    public Pause(String orderTitle, boolean isChecked) {
        OrderTitle = orderTitle;
        this.isChecked = isChecked;
    }

    public String getOrderTitle() {
        return OrderTitle;
    }

    public void setOrderTitle(String orderTitle) {
        OrderTitle = orderTitle;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
