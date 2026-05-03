package io.mipangg.liveklasscreatorsettlementapi.domain.salerecord.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record SaleRecordCreateRequest(

        @NotBlank(message = "courseId는 필수입니다.")
        String courseId,

        @NotBlank(message = "studentId는 필수입니다.")
        String studentId,

        @NotNull(message = "amount는 필수입니다.")
        @DecimalMin(value = "0.00", message = "amount는 최소 0.00 이상이어야 합니다.")
        BigDecimal amount,

        @NotNull(message = "paidAt은 필수입니다.")
        @PastOrPresent(message = "paidAt은 과거 또는 현재여야 합니다.")
        OffsetDateTime paidAt

) {

}
