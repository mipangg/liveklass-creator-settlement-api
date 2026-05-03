package io.mipangg.liveklasscreatorsettlementapi.domain.common.util;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.YearMonth;
import java.time.ZoneId;
import org.springframework.stereotype.Component;

@Component
public class DateTimeUtils {

    private final ZoneId zone;

    public DateTimeUtils() {
        this.zone = ZoneId.of("Asia/Seoul");
    }

    public OffsetDateTime toStartDateTime(LocalDate date) {

        return date.atStartOfDay(zone).toOffsetDateTime();
    }

    public OffsetDateTime toEndDateTime(LocalDate date) {

        return date.atTime(LocalTime.MAX).atZone(zone).toOffsetDateTime();
    }

    public OffsetDateTime toStartDateTime(YearMonth yearMonth) {

        // 해당 월의 1일 0시 0분, Offset(UTC) 결합하여 OffsetDateTime 변환
        return yearMonth.atDay(1).atStartOfDay(zone).toOffsetDateTime();

    }

    public OffsetDateTime toEndDateTime(YearMonth yearMonth) {

        // 해당 월의 1일 0시 0분, Offset(UTC) 결합하여 OffsetDateTime 변환
        return yearMonth.atEndOfMonth().atTime(LocalTime.MAX).atZone(zone).toOffsetDateTime();
    }

}
