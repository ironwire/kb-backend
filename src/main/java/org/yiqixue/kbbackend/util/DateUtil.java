package org.yiqixue.kbbackend.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class DateUtil {

    private static final DateTimeFormatter DEFAULT_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static String formatDateTime(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.format(DEFAULT_FORMATTER) : null;
    }

    public static String formatDate(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.format(DATE_FORMATTER) : null;
    }

    public static String getTimeAgo(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "Unknown";
        }

        LocalDateTime now = LocalDateTime.now();
        long minutes = ChronoUnit.MINUTES.between(dateTime, now);
        long hours = ChronoUnit.HOURS.between(dateTime, now);
        long days = ChronoUnit.DAYS.between(dateTime, now);

        if (minutes < 1) {
            return "Just now";
        } else if (minutes < 60) {
            return minutes + " minutes ago";
        } else if (hours < 24) {
            return hours + " hours ago";
        } else if (days < 30) {
            return days + " days ago";
        } else {
            return formatDate(dateTime);
        }
    }

    public static boolean isRecent(LocalDateTime dateTime, int days) {
        if (dateTime == null) {
            return false;
        }

        LocalDateTime cutoff = LocalDateTime.now().minusDays(days);
        return dateTime.isAfter(cutoff);
    }
}