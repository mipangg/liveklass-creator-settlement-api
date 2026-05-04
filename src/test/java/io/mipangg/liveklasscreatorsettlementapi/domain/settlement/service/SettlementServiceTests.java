package io.mipangg.liveklasscreatorsettlementapi.domain.settlement.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.mipangg.liveklasscreatorsettlementapi.domain.cancel.entity.Cancel;
import io.mipangg.liveklasscreatorsettlementapi.domain.cancel.repository.CancelRepository;
import io.mipangg.liveklasscreatorsettlementapi.domain.commissionrate.entity.CommissionRate;
import io.mipangg.liveklasscreatorsettlementapi.domain.commissionrate.repository.CommissionRateRepository;
import io.mipangg.liveklasscreatorsettlementapi.domain.common.util.DateTimeUtils;
import io.mipangg.liveklasscreatorsettlementapi.domain.course.entity.Course;
import io.mipangg.liveklasscreatorsettlementapi.domain.creator.entity.Creator;
import io.mipangg.liveklasscreatorsettlementapi.domain.creator.repository.CreatorRepository;
import io.mipangg.liveklasscreatorsettlementapi.domain.salerecord.entity.SaleRecord;
import io.mipangg.liveklasscreatorsettlementapi.domain.salerecord.repository.SaleRecordRepository;
import io.mipangg.liveklasscreatorsettlementapi.domain.settlement.dto.SettlementMonthlyReadRequest;
import io.mipangg.liveklasscreatorsettlementapi.domain.settlement.dto.SettlementMonthlyReadResponse;
import io.mipangg.liveklasscreatorsettlementapi.domain.settlement.dto.SettlementSummaryReadRequest;
import io.mipangg.liveklasscreatorsettlementapi.domain.settlement.dto.SettlementSummaryReadResponse;
import io.mipangg.liveklasscreatorsettlementapi.domain.settlement.dto.SettlementSummaryRow;
import io.mipangg.liveklasscreatorsettlementapi.domain.settlement.entity.Settlement;
import io.mipangg.liveklasscreatorsettlementapi.domain.settlement.entity.SettlementStatus;
import io.mipangg.liveklasscreatorsettlementapi.domain.settlement.repository.SettlementRepository;
import io.mipangg.liveklasscreatorsettlementapi.domain.student.entity.Student;
import io.mipangg.liveklasscreatorsettlementapi.global.TestUtil;
import io.mipangg.liveklasscreatorsettlementapi.global.exception.CustomLogicException;
import io.mipangg.liveklasscreatorsettlementapi.global.exception.ErrorCode;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SettlementServiceTests {
    
    @InjectMocks
    private SettlementService settlementService;
    
    @Mock
    private SettlementRepository settlementRepository;
    
    @Mock
    private CreatorRepository creatorRepository;
    
    @Mock
    private CommissionRateRepository commissionRateRepository;
    
    @Mock
    SaleRecordRepository saleRecordRepository;
    
    @Mock
    CancelRepository cancelRepository;
    
    @Mock
    DateTimeUtils dataTimeUtils;
    
    @Test
    @DisplayName("크리에이터별 월별 과거 정산 내역 조회 시 Settlement가 없으면 생성, DB에 저장 후 반환한다")
    void findSettlementPastRecordSuccessTest() {

        Creator creator = TestUtil.genCreators().getFirst();
        YearMonth yearMonth = YearMonth.of(2025, 3);

        SettlementMonthlyReadRequest req = new SettlementMonthlyReadRequest("creator-1", yearMonth);

        OffsetDateTime startDate = OffsetDateTime.parse("2025-03-01T00:00:00+09:00");
        OffsetDateTime endDate = OffsetDateTime.parse("2025-03-31T23:59:59+09:00");

        List<Course> courses = TestUtil.genCourses();
        List<Student> students = TestUtil.genStudents();
        List<SaleRecord> saleRecords = List.of(
                SaleRecord.builder()
                        .course(courses.get(0))
                        .student(students.get(0))
                        .amount(BigDecimal.valueOf(50000))
                        .paidAt(OffsetDateTime.parse("2025-03-05T10:00:00+09:00"))
                        .build(),
                SaleRecord.builder()
                        .course(courses.get(0))
                        .student(students.get(1))
                        .amount(BigDecimal.valueOf(50000))
                        .paidAt(OffsetDateTime.parse("2025-03-15T14:30:00+09:00"))
                        .build(),
                SaleRecord.builder()
                        .course(courses.get(1))
                        .student(students.get(2))
                        .amount(BigDecimal.valueOf(80000))
                        .paidAt(OffsetDateTime.parse("2025-03-20T09:00:00+09:00"))
                        .build(),
                SaleRecord.builder()
                        .course(courses.get(1))
                        .student(students.get(3))
                        .amount(BigDecimal.valueOf(80000))
                        .paidAt(OffsetDateTime.parse("2025-03-22T11:00:00+09:00"))
                        .build()
        );

        List<Cancel> cancels = List.of(
                Cancel.builder()
                        .saleRecord(saleRecords.get(2))
                        .amount(BigDecimal.valueOf(80000))
                        .canceledAt(OffsetDateTime.parse("2025-03-22T09:00:00+09:00"))
                        .build(),
                Cancel.builder()
                        .saleRecord(saleRecords.get(3))
                        .amount(BigDecimal.valueOf(30000))
                        .canceledAt(OffsetDateTime.parse("2025-03-24T11:00:00+09:00"))
                        .build()
        );

        Settlement settlement = TestUtil.genSettlementForCreator1();

        CommissionRate commissionRate = TestUtil.genCommissionRate();

        when(creatorRepository.findById(anyString())).thenReturn(Optional.of(creator));
        when(dataTimeUtils.toStartDateTime(any(YearMonth.class))).thenReturn(startDate);
        when(dataTimeUtils.toEndDateTime(any(YearMonth.class))).thenReturn(endDate);
        when(settlementRepository.findByCreatorAndYearMonth(any(), any()))
                .thenReturn(Optional.empty());
        when(commissionRateRepository.getCurrentCommissionRate())
                .thenReturn(Optional.of(commissionRate));
        when(saleRecordRepository.findByCreatorAndPaidAtBetween(any(), any(), any()))
                .thenReturn(saleRecords);
        when(cancelRepository.findByCreatorAndCanceledAtBetween(any(), any(), any()))
                .thenReturn(cancels);
        when(settlementRepository.saveAndFlush(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        SettlementMonthlyReadResponse result = settlementService.findSettlement(req);

        assertThat(result.totalSaleAmount()).isEqualByComparingTo(settlement.getTotalSaleAmount());
        assertThat(result.totalCancelAmount()).isEqualByComparingTo(settlement.getTotalCancelAmount());
        assertThat(result.netSaleAmount()).isEqualByComparingTo(settlement.getNetSaleAmount());
        assertThat(result.commission()).isEqualByComparingTo(settlement.getCommission());
        assertThat(result.totalSettlementAmount()).isEqualByComparingTo(settlement.getTotalSettlementAmount());
        assertThat(result.saleCount()).isEqualTo(settlement.getSaleCount());
        assertThat(result.cancelCount()).isEqualTo(settlement.getCancelCount());

        verify(settlementRepository).saveAndFlush(any());
    }

    @Test
    @DisplayName("크리에이터별 현재 월 정산 내역 조회 시 월 1일부터 현재까지 정산 내역을 계산 후 반환한다")
    void findSettlementCurrentRecordSuccessTest() {

        Creator creator = TestUtil.genCreators().getFirst();
        YearMonth yearMonth = YearMonth.of(2026, 5);

        SettlementMonthlyReadRequest req = new SettlementMonthlyReadRequest("creator-1", yearMonth);

        OffsetDateTime startDate = OffsetDateTime.parse("2026-05-01T00:00:00+09:00");

        List<Course> courses = TestUtil.genCourses();
        List<Student> students = TestUtil.genStudents();
        List<SaleRecord> saleRecords = List.of(
                SaleRecord.builder()
                        .course(courses.get(0))
                        .student(students.get(0))
                        .amount(BigDecimal.valueOf(50000))
                        .paidAt(OffsetDateTime.parse("2026-05-05T10:00:00+09:00"))
                        .build(),
                SaleRecord.builder()
                        .course(courses.get(0))
                        .student(students.get(1))
                        .amount(BigDecimal.valueOf(50000))
                        .paidAt(OffsetDateTime.parse("2026-05-15T14:30:00+09:00"))
                        .build(),
                SaleRecord.builder()
                        .course(courses.get(1))
                        .student(students.get(2))
                        .amount(BigDecimal.valueOf(80000))
                        .paidAt(OffsetDateTime.parse("2026-05-20T09:00:00+09:00"))
                        .build(),
                SaleRecord.builder()
                        .course(courses.get(1))
                        .student(students.get(3))
                        .amount(BigDecimal.valueOf(80000))
                        .paidAt(OffsetDateTime.parse("2026-05-22T11:00:00+09:00"))
                        .build()
        );

        List<Cancel> cancels = List.of(
                Cancel.builder()
                        .saleRecord(saleRecords.get(2))
                        .amount(BigDecimal.valueOf(80000))
                        .canceledAt(OffsetDateTime.parse("2025-03-22T09:00:00+09:00"))
                        .build(),
                Cancel.builder()
                        .saleRecord(saleRecords.get(3))
                        .amount(BigDecimal.valueOf(30000))
                        .canceledAt(OffsetDateTime.parse("2025-03-24T11:00:00+09:00"))
                        .build()
        );

        CommissionRate commissionRate = TestUtil.genCommissionRate();

        Settlement settlement = Settlement.builder()
                .settlementMonth(YearMonth.of(2026, 5))
                .totalSaleAmount(BigDecimal.valueOf(260000))
                .totalCancelAmount(BigDecimal.valueOf(110000))
                .netSaleAmount(BigDecimal.valueOf(150000))
                .commission(BigDecimal.valueOf(30000))
                .totalSettlementAmount(BigDecimal.valueOf(120000))
                .commissionRate(commissionRate)
                .saleCount(4)
                .cancelCount(2)
                .build();


        when(creatorRepository.findById(anyString())).thenReturn(Optional.of(creator));
        when(dataTimeUtils.toStartDateTime(any(YearMonth.class))).thenReturn(startDate);
        when(commissionRateRepository.getCurrentCommissionRate())
                .thenReturn(Optional.of(commissionRate));
        when(saleRecordRepository.findByCreatorAndPaidAtBetween(any(), any(), any()))
                .thenReturn(saleRecords);
        when(cancelRepository.findByCreatorAndCanceledAtBetween(any(), any(), any()))
                .thenReturn(cancels);

        SettlementMonthlyReadResponse result = settlementService.findSettlement(req);

        assertThat(result.totalSaleAmount()).isEqualByComparingTo(settlement.getTotalSaleAmount());
        assertThat(result.totalCancelAmount()).isEqualByComparingTo(settlement.getTotalCancelAmount());
        assertThat(result.netSaleAmount()).isEqualByComparingTo(settlement.getNetSaleAmount());
        assertThat(result.commission()).isEqualByComparingTo(settlement.getCommission());
        assertThat(result.totalSettlementAmount()).isEqualByComparingTo(settlement.getTotalSettlementAmount());
        assertThat(result.saleCount()).isEqualTo(settlement.getSaleCount());
        assertThat(result.cancelCount()).isEqualTo(settlement.getCancelCount());

        verify(settlementRepository, never()).saveAndFlush(any());

    }
    
    @Test
    @DisplayName("크리에이터별 월별 정산 내역 조회 시 판매 내역이 없으면 0원을 반환한다")
    void findSettlementZeroIncomeSuccessTest() {

        Creator creator = TestUtil.genCreators().getFirst();
        YearMonth yearMonth = YearMonth.of(2025, 3);

        SettlementMonthlyReadRequest req = new SettlementMonthlyReadRequest("creator-1", yearMonth);

        OffsetDateTime startDate = OffsetDateTime.parse("2025-03-01T00:00:00+09:00");
        OffsetDateTime endDate = OffsetDateTime.parse("2025-03-31T23:59:59+09:00");

        CommissionRate commissionRate = TestUtil.genCommissionRate();

        Settlement settlement = Settlement.builder()
                .settlementMonth(YearMonth.of(2025, 3))
                .totalSaleAmount(BigDecimal.valueOf(0))
                .totalCancelAmount(BigDecimal.valueOf(0))
                .netSaleAmount(BigDecimal.valueOf(0))
                .commission(BigDecimal.valueOf(0))
                .totalSettlementAmount(BigDecimal.valueOf(0))
                .commissionRate(commissionRate)
                .saleCount(0)
                .cancelCount(0)
                .build();


        when(creatorRepository.findById(anyString())).thenReturn(Optional.of(creator));
        when(dataTimeUtils.toStartDateTime(any(YearMonth.class))).thenReturn(startDate);
        when(dataTimeUtils.toEndDateTime(any(YearMonth.class))).thenReturn(endDate);
        when(settlementRepository.findByCreatorAndYearMonth(any(), any()))
                .thenReturn(Optional.empty());
        when(commissionRateRepository.getCurrentCommissionRate())
                .thenReturn(Optional.of(commissionRate));
        when(saleRecordRepository.findByCreatorAndPaidAtBetween(any(), any(), any()))
                .thenReturn(List.of());
        when(settlementRepository.saveAndFlush(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        SettlementMonthlyReadResponse result = settlementService.findSettlement(req);

        assertThat(result.totalSaleAmount()).isEqualByComparingTo(settlement.getTotalSaleAmount());
        assertThat(result.totalCancelAmount()).isEqualByComparingTo(settlement.getTotalCancelAmount());
        assertThat(result.netSaleAmount()).isEqualByComparingTo(settlement.getNetSaleAmount());
        assertThat(result.commission()).isEqualByComparingTo(settlement.getCommission());
        assertThat(result.totalSettlementAmount()).isEqualByComparingTo(settlement.getTotalSettlementAmount());
        assertThat(result.saleCount()).isEqualTo(settlement.getSaleCount());
        assertThat(result.cancelCount()).isEqualTo(settlement.getCancelCount());

        verify(settlementRepository).saveAndFlush(any());
    
    }

    @Test
    @DisplayName("미래의 월로 크리에이터별 월별 정산 내역 조회를 시도하면 예외가 발생한다")
    void findSettlementFutureSettlementFailTest() {

        Creator creator = TestUtil.genCreators().getFirst();
        YearMonth yearMonth = YearMonth.of(2027, 3);

        SettlementMonthlyReadRequest req = new SettlementMonthlyReadRequest("creator-1", yearMonth);

        when(creatorRepository.findById(anyString())).thenReturn(Optional.of(creator));

        assertThatThrownBy(
                () -> {
                    settlementService.findSettlement(req);
                }
        ).isInstanceOf(CustomLogicException.class)
                .hasMessage(ErrorCode.INVALID_DATE.getMessage());

    }

    @Test
    @DisplayName("존재하지 않는 크리에이터 아이디로 크리에이터별 월별 정산 내역 조회를 시도하면 예외가 발생한다")
    void findSettlementCreatorNotFoundFailTest() {

        YearMonth yearMonth = YearMonth.of(2027, 3);

        SettlementMonthlyReadRequest req = new SettlementMonthlyReadRequest("creator-1", yearMonth);

        when(creatorRepository.findById(anyString())).thenReturn(Optional.empty());

        assertThatThrownBy(
                () -> {
                    settlementService.findSettlement(req);
                }
        ).isInstanceOf(CustomLogicException.class)
                .hasMessage(ErrorCode.CREATOR_NOT_FOUND.getMessage());

    }
    
    @Test
    @DisplayName("기간 내 전체 크리에이터 정산 현황 목록을 조회할 수 있다")
    void getSettlementSummarySuccessTest() {

        SettlementSummaryReadRequest req =
                new SettlementSummaryReadRequest(
                        YearMonth.of(2025, 1),
                        YearMonth.of(2025, 3)
                );

        List<SettlementSummaryRow> settlementSummaryRows = List.of(
                new SettlementSummaryRow(
                        "creator-1",
                        SettlementStatus.PENDING,
                        BigDecimal.valueOf(10000)
                ),
                new SettlementSummaryRow(
                        "creator-1",
                        SettlementStatus.CONFIRMED,
                        BigDecimal.valueOf(20000)
                ),
                new SettlementSummaryRow(
                        "creator-1",
                        SettlementStatus.PAID,
                        BigDecimal.valueOf(30000)
                ),
                new SettlementSummaryRow(
                        "creator-2",
                        SettlementStatus.PENDING,
                        BigDecimal.valueOf(5000)
                ),
                new SettlementSummaryRow(
                        "creator-2",
                        SettlementStatus.PAID,
                        BigDecimal.valueOf(70000)
                )
        );

        when(settlementRepository.findBySettlementMonthBetween(any(), any()))
                .thenReturn(settlementSummaryRows);

        SettlementSummaryReadResponse resp = settlementService.getSettlementSummary(req);

        assertThat(resp.creators()).hasSize(2);
        assertThat(resp.total().pendingAmount()).isEqualTo(BigDecimal.valueOf(15000));
        assertThat(resp.total().confirmedAmount()).isEqualTo(BigDecimal.valueOf(20000));
        assertThat(resp.total().paidAmount()).isEqualTo(BigDecimal.valueOf(100000));

    }
    
    @Test
    @DisplayName("기간 내 정산 내역이 없으면 모두 0원을 반환한다")
    void getSettlementZeroAmountSuccessTest() {

        SettlementSummaryReadRequest req =
                new SettlementSummaryReadRequest(
                        YearMonth.of(2025, 1),
                        YearMonth.of(2025, 3)
                );

        List<SettlementSummaryRow> settlementSummaryRows = List.of();

        when(settlementRepository.findBySettlementMonthBetween(any(), any()))
                .thenReturn(settlementSummaryRows);

        SettlementSummaryReadResponse resp = settlementService.getSettlementSummary(req);

        assertThat(resp.creators()).isEmpty();
        assertThat(resp.total().pendingAmount()).isEqualTo(BigDecimal.ZERO);
        assertThat(resp.total().confirmedAmount()).isEqualTo(BigDecimal.ZERO);
        assertThat(resp.total().paidAmount()).isEqualTo(BigDecimal.ZERO);
    
    }

}