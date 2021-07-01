package dev.nicholaskoldys.matso.utility;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeService {

    public static final DateTimeFormatter LASTBREAKDOWN_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    public static final DateTimeFormatter DASHDATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final DateTimeFormatter DASHTIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm");
    public static final DateTimeFormatter OFFSET_LENGTH_FORMAT = DateTimeFormatter.ofPattern("HH:mm");

    public static LocalDateTime toUTC(LocalDateTime dateTime) {
        LocalDateTime dateTimeUTC = null;
        if (dateTime != null) {
            ZonedDateTime universalTime = ZonedDateTime.of(dateTime, ZoneId.systemDefault());
            dateTimeUTC = universalTime.withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime();
        }
        return dateTimeUTC;
    }

    public static Timestamp toStampUTC(LocalDateTime dateTime) {
        Timestamp timestamp = null;
        if (dateTime != null) {
            timestamp =  Timestamp.valueOf(toUTC(dateTime));
        }
        return timestamp;
    }

    public static LocalDateTime fromUTC(LocalDateTime dateTime) {
        LocalDateTime dateTimeLocal = null;
        if (dateTime != null) {
            ZonedDateTime timeInUtc = ZonedDateTime.of(dateTime, ZoneId.of("UTC"));
            dateTimeLocal = timeInUtc.withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime();
            //dateTimeLocal = LocalDateTime.parse(dateTimeLocal.toString(), LASTBREAKDOWN_FORMAT);
        }
        return dateTimeLocal;
    }
}
