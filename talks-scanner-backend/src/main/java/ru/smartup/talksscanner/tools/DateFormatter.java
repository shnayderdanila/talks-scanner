package ru.smartup.talksscanner.tools;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *  Formatter date to ISO standard
 * */
public class DateFormatter {
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_DATE_TIME;

    public static String dateTimeToString(LocalDateTime dateTime) {
        return dateFormatter.format(dateTime);
    }

    public static LocalDateTime dateTimeToFormat(LocalDateTime dateTime) {
        return LocalDateTime.parse(dateTimeToString(dateTime), dateFormatter);
    }
}
