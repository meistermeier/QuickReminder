package com.meistermeier.reminder;

import java.util.Date;

/**
 * Some nice information
 */
public class DefaultTaskItem implements TaskItem {
    private Integer id;
    private String name;
    private Date dueDate;
    private boolean reminderActive;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
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
