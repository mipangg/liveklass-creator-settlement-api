package io.mipangg.liveklasscreatorsettlementapi.domain.salerecord.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record SaleRecordListReadResponse(

        @NotBlank
        String id,

        @NotBlank
        String courseId,

        @NotBlank
        String studentId,

        @NotNull
        @Min(0)
        BigDecimal amount,

        @NotNull
        @PastOrPresent
        OffsetDateTime paidAt

) {

}
