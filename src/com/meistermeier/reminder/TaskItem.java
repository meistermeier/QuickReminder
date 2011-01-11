package com.meistermeier.reminder;

import java.util.Date;

/**
 * Some nice information
 */
public interface TaskItem {

    final static String ID_FIELD = "id";
    final static String NAME_FIELD = "name";
    final static String DUE_DATE_FIELD = "due_date";
    final static String REMINDER_FIELD = "reminder";


    Integer getId();

    void setId(Integer id);

    String getName();

    void setName(String name);

    Date getDueDate();

    void setDueDate(Date dueDate);

    Boolean isReminderActive();

    void setReminderActive(boolean reminderActive);
}
