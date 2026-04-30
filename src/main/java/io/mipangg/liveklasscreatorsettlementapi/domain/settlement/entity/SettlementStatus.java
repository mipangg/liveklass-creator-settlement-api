package io.mipangg.liveklasscreatorsettlementapi.domain.settlement.entity;

import lombok.Getter;

@Getter
public enum SettlementStatus {

    PENDING("정산 대기"),
    CONFIRMED("정산 확정"),
    PAID("정산 완료");

    private final String status;

    SettlementStatus(String status) {
        this.status = status;
    }

}
