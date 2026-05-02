package io.mipangg.liveklasscreatorsettlementapi.domain.common.util;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import org.springframework.stereotype.Component;

@Component
public class DateTimeUtils {

    private final ZoneId zone;

    public DateTimeUtils() {
        this.zone = ZoneId.systemDefault();
    }

    public OffsetDateTime toStartDateTime(LocalDate date) {

        return date.atStartOfDay(zone).toOffsetDateTime();
    }

    public OffsetDateTime toEndDateTime(LocalDate date) {

        return date.atTime(LocalTime.MAX).atZone(zone).toOffsetDateTime();
    }

}
