package com.meistermeier.reminder;

import java.util.Date;

/**
 * Some nice information
 */
public interface TaskItem {

    final static String ID_FIELD = "id";
    final static String NAME_FIELD = "name";
    final static String DUE_DATE_FIELD = "timestamp";
    final static String REMINDER_FIELD = "reminder";

    long getId();

    void setId(long id);

    String getName();

    void setName(String name);

    long getTimestamp();

    void setTimestamp(long dueDate);

    Boolean isReminderActive();

    void setReminderActive(boolean reminderActive);
}
