package com.ecommerce.shared.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Utilitários de data — centralizados para consistência em toda a aplicação.
 */
public final class DateUtils {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    private DateUtils() {}

    public static String format(LocalDateTime dateTime) {
        if (dateTime == null) return null;
        return dateTime.format(FORMATTER);
    }

    public static LocalDateTime now() {
        return LocalDateTime.now();
    }
}
