package com.example.alarm.model;

import javax.inject.Inject;

public class Alarm {

    private final long id;
    private int timeHours = 7;
    private int timeMinutes = 0;
    private String name = "default";
    private boolean enabled = false;

    @Inject
    public Alarm(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public int getTimeHours() {
        return timeHours;
    }

    public void setTimeHours(int timeHours) {
        this.timeHours = timeHours;
    }

    public int getTimeMinutes() {
        return timeMinutes;
    }

    public void setTimeMinutes(int timeMinutes) {
        this.timeMinutes = timeMinutes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}
