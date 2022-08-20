package com.example.ApachePOIExcelExample.util;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

public class DateUtils {

    private DateUtils() {
    }

    public static String formatDate(Instant date) {
        return DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
                .withLocale(Locale.forLanguageTag("RU"))
                .withZone(ZoneId.systemDefault())
                .format(date);
    }

}