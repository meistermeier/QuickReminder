package com.meistermeier.reminder;

/**
 * Some nice information
 */
public class DefaultTaskItem implements TaskItem {
    private long id;
    private String name;
    private long timestamp;
    private boolean reminderActive;

    public DefaultTaskItem() {

    }

    public DefaultTaskItem(long id, String name, long timestamp, boolean reminderActive) {
        this.id = id;
        this.name = name;
        this.timestamp = timestamp;
        this.reminderActive = reminderActive;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public Boolean isReminderActive() {
        return reminderActive;
    }

    public void setReminderActive(boolean reminderActive) {
        this.reminderActive = reminderActive;
    }

    public String toString() {
        return this.name;
    }
}
