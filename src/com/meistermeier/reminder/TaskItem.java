package com.meistermeier.reminder;

import java.util.Date;

/**
 * Some nice information
 */
public interface TaskItem {
    String getName();

    void setName(String name);

    Date getDueDate();

    void setDueDate(Date dueDate);

    Boolean isReminderActive();

    void setReminderActive(boolean reminderActive);
}
