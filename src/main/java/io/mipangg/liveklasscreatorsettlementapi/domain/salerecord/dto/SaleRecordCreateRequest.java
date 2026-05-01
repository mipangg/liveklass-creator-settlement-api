package io.mipangg.liveklasscreatorsettlementapi.domain.salerecord.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record SaleRecordCreateRequest(

        @NotBlank
        String courseId,

        @NotBlank
        String studentId,

        @Min(0)
        @NotNull
        BigDecimal amount,

        @NotNull
        @PastOrPresent
        OffsetDateTime paidAt

) {

}
