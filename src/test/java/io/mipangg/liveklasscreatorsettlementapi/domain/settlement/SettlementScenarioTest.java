package io.mipangg.liveklasscreatorsettlementapi.domain.settlement;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import io.mipangg.liveklasscreatorsettlementapi.domain.cancel.dto.CancelCreateRequest;
import io.mipangg.liveklasscreatorsettlementapi.domain.cancel.service.CancelService;
import io.mipangg.liveklasscreatorsettlementapi.domain.creator.entity.Creator;
import io.mipangg.liveklasscreatorsettlementapi.domain.creator.repository.CreatorRepository;
import io.mipangg.liveklasscreatorsettlementapi.domain.salerecord.service.SaleRecordService;
import io.mipangg.liveklasscreatorsettlementapi.domain.settlement.dto.SettlementMonthlyReadRequest;
import io.mipangg.liveklasscreatorsettlementapi.domain.settlement.dto.SettlementMonthlyReadResponse;
import io.mipangg.liveklasscreatorsettlementapi.domain.settlement.entity.Settlement;
import io.mipangg.liveklasscreatorsettlementapi.domain.settlement.repository.SettlementRepository;
import io.mipangg.liveklasscreatorsettlementapi.domain.settlement.service.SettlementService;
import io.mipangg.liveklasscreatorsettlementapi.global.exception.CustomLogicException;
import io.mipangg.liveklasscreatorsettlementapi.global.exception.ErrorCode;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.OffsetDateTime;
import java.time.YearMonth;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class SettlementScenarioTest {

    @Autowired
    private SettlementService settlementService;

    @Autowired
    private SaleRecordService saleRecordService;

    @Autowired
    private CancelService cancelService;

    @Autowired
    private SettlementRepository settlementRepository;
    @Autowired
    private CreatorRepository creatorRepository;

    @Test
    @DisplayName("creator-1의 2025-03 정산 (sale-1,2,3,4, cancel-1,2)")
    void monthlySettlementCalculationTest() {

        SettlementMonthlyReadResponse resp = settlementService.findSettlement(
                new SettlementMonthlyReadRequest(
                        "creator-1",
                        YearMonth.of(2025, 3)
                )
        );

        assertThat(resp.totalSaleAmount()).isEqualByComparingTo("260000");
        assertThat(resp.totalCancelAmount()).isEqualByComparingTo("110000");
        assertThat(resp.netSaleAmount()).isEqualByComparingTo("150000");
        assertThat(resp.commission()).isEqualByComparingTo("30000");
        assertThat(resp.totalSettlementAmount()).isEqualByComparingTo("120000");

    }
    
    @Test
    @DisplayName("부분 환불 처리 - 환불액(30,000)이 원결제(80,000)보다 작은 경우 순 판매 반영 (sale-4, cancel-2)")
    void PartialCancelTest() {

        SettlementMonthlyReadResponse resp = settlementService.findSettlement(
                new SettlementMonthlyReadRequest(
                        "creator-1",
                        YearMonth.of(2025, 3)
                )
        );

        // sale-4 (80000) - cancel-2 (30000)
        assertThat(resp.totalCancelAmount()).isEqualByComparingTo("110000");
    }

    @Test
    @DisplayName("월 경계 취소 - 1월 판매, 2월 취소 시 각각 해당 월 정산에 반영 (sale-5, cancel-3)")
    void monthBoundaryTest() {

        SettlementMonthlyReadResponse jan = settlementService.findSettlement(
                new SettlementMonthlyReadRequest(
                        "creator-2",
                        YearMonth.of(2025, 1)
                )
        );

        assertThat(jan.totalSaleAmount()).isEqualByComparingTo("60000");
        assertThat(jan.totalCancelAmount()).isEqualByComparingTo("0");

        SettlementMonthlyReadResponse feb = settlementService.findSettlement(
                new SettlementMonthlyReadRequest(
                        "creator-2",
                        YearMonth.of(2025, 2)
                )
        );

        assertThat(feb.totalSaleAmount()).isEqualByComparingTo("0");
        assertThat(feb.totalCancelAmount()).isEqualByComparingTo("60000");

    }

    @Test
    @DisplayName("빈 월 조회 - 판매 내역 없는 월 조회 시 0원 응답 또는 처리 방침 일관성 확인 (creator-3, 2025-03)")
    void emptySettlementMonthTest() {

        SettlementMonthlyReadResponse resp = settlementService.findSettlement(
                new SettlementMonthlyReadRequest(
                        "creator-3",
                        YearMonth.of(2025, 3)
                )
        );

        assertThat(resp.totalSaleAmount()).isEqualByComparingTo("0");
        assertThat(resp.totalCancelAmount()).isEqualByComparingTo("0");
        assertThat(resp.netSaleAmount()).isEqualByComparingTo("0");
        assertThat(resp.commission()).isEqualByComparingTo("0");
        assertThat(resp.totalSettlementAmount()).isEqualByComparingTo("0");

    }
    
    @Test
    @DisplayName("과거 월 조회 시 정산 스냅샷이 저장 (sale-1,2,3,4, cancel-1,2)")
    void pastMonthSettlementSnapshotTest() {

        YearMonth month = YearMonth.of(2025, 3);

        // 과거 월 정산 조회
        settlementService.findSettlement(
                new SettlementMonthlyReadRequest(
                        "creator-1",
                        month
                )
        );

        // 조회 후 SettlementRepository에 정산 스냅샷 저장
        Creator creator = creatorRepository.findById("creator-1")
                .orElseThrow(() -> new CustomLogicException(ErrorCode.CREATOR_NOT_FOUND));
        Settlement settlement = settlementRepository
                .findByCreatorAndYearMonth(creator, month.atDay(1))
                .orElseThrow(() -> new CustomLogicException(ErrorCode.SETTLEMENT_NOT_FOUND));

        assertThat(settlement.getCreator()).isEqualTo(creator);
        assertThat(settlement.getSettlementMonth()).isEqualTo(month.atDay(1));

    }

    @Test
    @DisplayName("현재 월 조회 시 정산 스냅샷이 저장되지 않음 (sale-8,9)")
    void currentMonthSettlementSnapshotTest() {

        YearMonth month = YearMonth.of(2026, 5);

        // 현재 월 정산 조회
        settlementService.findSettlement(
                new SettlementMonthlyReadRequest(
                        "creator-4",
                        month
                )
        );

        // 조회 후 Settlement에서 해당 연월의 정산 데이터 찾을 수 없음
        Creator creator = creatorRepository.findById("creator-4")
                .orElseThrow(() -> new CustomLogicException(ErrorCode.CREATOR_NOT_FOUND));

        Optional<Settlement> settlement = settlementRepository
                .findByCreatorAndYearMonth(creator, month.atDay(1));

        assertThat(settlement).isNotPresent();
        
    }
    
    @Test
    @DisplayName("동일 판매에 대한 다중 취소 (sale-4, cancel-2)")
    void multipleCancelTest() {

        cancelService.saveCancel(
                new CancelCreateRequest(
                        "sale-4",
                        BigDecimal.valueOf(20000),
                        OffsetDateTime.parse("2025-03-26T11:00:00+09:00"))
        );

        SettlementMonthlyReadResponse resp =
                settlementService.findSettlement(
                        new SettlementMonthlyReadRequest("creator-1", YearMonth.of(2025, 3))
                );

        assertThat(resp.totalCancelAmount()).isEqualByComparingTo("130000");
    
    }
    
    @Test
    @DisplayName("수수료 계산 소수점 처리 - 수수료 계산 시 반올림 정책 적용")
    void commissionRoundingTest() {

        BigDecimal net = BigDecimal.valueOf(33333);

        BigDecimal commission = net
                .multiply(BigDecimal.valueOf(20))
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

        assertThat(commission).isEqualByComparingTo("6666.60");
    
    }

    @Test
    @DisplayName("기간 경계값 포함 여부 (sale-5)")
    void boundaryTimeTest() {

        SettlementMonthlyReadResponse res =
                settlementService.findSettlement(
                        new SettlementMonthlyReadRequest("creator-2", YearMonth.of(2025, 1))
                );

        assertThat(res.saleCount()).isEqualTo(1);

    }

    @Test
    @DisplayName("크리에이터 데이터 분리 검증 (creator-1,2, sale-1,2,3,4,6)")
    void creatorIsolationTest() {

        SettlementMonthlyReadResponse creator1Settlement =
                settlementService.findSettlement(
                        new SettlementMonthlyReadRequest("creator-1", YearMonth.of(2025, 3))
                );

        assertThat(creator1Settlement.totalSaleAmount()).isEqualByComparingTo("260000");
        assertThat(creator1Settlement.totalCancelAmount()).isEqualByComparingTo("110000");
        assertThat(creator1Settlement.netSaleAmount()).isEqualByComparingTo("150000");
        assertThat(creator1Settlement.commission()).isEqualByComparingTo("30000");
        assertThat(creator1Settlement.totalSettlementAmount()).isEqualByComparingTo("120000");

        SettlementMonthlyReadResponse creator2Settlement =
                settlementService.findSettlement(
                        new SettlementMonthlyReadRequest("creator-2", YearMonth.of(2025, 3))
                );

        assertThat(creator2Settlement.totalSaleAmount()).isEqualByComparingTo("60000");
        assertThat(creator2Settlement.totalCancelAmount()).isEqualByComparingTo("0");
        assertThat(creator2Settlement.netSaleAmount()).isEqualByComparingTo("60000");
        assertThat(creator2Settlement.commission()).isEqualByComparingTo("12000");
        assertThat(creator2Settlement.totalSettlementAmount()).isEqualByComparingTo("48000");

    }
    
    @Test
    @DisplayName("환불 요청 금액이 원결제 값보다 크면 예외 발생 (sale-3)")
    void cancelExceedTest() {

        assertThatThrownBy(() ->
                cancelService.saveCancel(
                        new CancelCreateRequest(
                                "sale-3",
                                BigDecimal.valueOf(999999),
                                OffsetDateTime.now()
                        )
                )
        ).isInstanceOf(CustomLogicException.class);
    
    }
    
}
