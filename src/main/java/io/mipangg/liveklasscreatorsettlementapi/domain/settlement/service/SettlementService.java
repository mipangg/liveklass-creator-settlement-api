package io.mipangg.liveklasscreatorsettlementapi.domain.settlement.service;

import io.mipangg.liveklasscreatorsettlementapi.domain.cancel.entity.Cancel;
import io.mipangg.liveklasscreatorsettlementapi.domain.cancel.repository.CancelRepository;
import io.mipangg.liveklasscreatorsettlementapi.domain.commissionrate.entity.CommissionRate;
import io.mipangg.liveklasscreatorsettlementapi.domain.commissionrate.repository.CommissionRateRepository;
import io.mipangg.liveklasscreatorsettlementapi.domain.common.util.DateTimeUtils;
import io.mipangg.liveklasscreatorsettlementapi.domain.creator.entity.Creator;
import io.mipangg.liveklasscreatorsettlementapi.domain.creator.repository.CreatorRepository;
import io.mipangg.liveklasscreatorsettlementapi.domain.salerecord.entity.SaleRecord;
import io.mipangg.liveklasscreatorsettlementapi.domain.salerecord.repository.SaleRecordRepository;
import io.mipangg.liveklasscreatorsettlementapi.domain.settlement.dto.CreatorSettlementSummary;
import io.mipangg.liveklasscreatorsettlementapi.domain.settlement.dto.SalesAndCancelsSummaryDto;
import io.mipangg.liveklasscreatorsettlementapi.domain.settlement.dto.SettlementAmountsDto;
import io.mipangg.liveklasscreatorsettlementapi.domain.settlement.dto.SettlementMonthlyReadRequest;
import io.mipangg.liveklasscreatorsettlementapi.domain.settlement.dto.SettlementMonthlyReadResponse;
import io.mipangg.liveklasscreatorsettlementapi.domain.settlement.dto.SettlementSummaryReadRequest;
import io.mipangg.liveklasscreatorsettlementapi.domain.settlement.dto.SettlementSummaryReadResponse;
import io.mipangg.liveklasscreatorsettlementapi.domain.settlement.dto.SettlementSummaryRow;
import io.mipangg.liveklasscreatorsettlementapi.domain.settlement.dto.SettlementTotalSummary;
import io.mipangg.liveklasscreatorsettlementapi.domain.settlement.entity.Settlement;
import io.mipangg.liveklasscreatorsettlementapi.domain.settlement.entity.SettlementStatus;
import io.mipangg.liveklasscreatorsettlementapi.domain.settlement.repository.SettlementRepository;
import io.mipangg.liveklasscreatorsettlementapi.global.exception.CustomLogicException;
import io.mipangg.liveklasscreatorsettlementapi.global.exception.ErrorCode;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SettlementService {

    private final SettlementRepository settlementRepository;
    private final CreatorRepository creatorRepository;
    private final CommissionRateRepository commissionRateRepository;
    private final SaleRecordRepository saleRecordRepository;
    private final CancelRepository cancelRepository;

    private final DateTimeUtils dateTimeUtils;

    private final BigDecimal ONE_HUNDRED = BigDecimal.valueOf(100);

    @Transactional
    public SettlementMonthlyReadResponse findSettlement(SettlementMonthlyReadRequest req) {

        // req.creator 존재하는지 확인 -> 없으면 Error
        Creator creator = creatorRepository.findById(req.creatorId())
                .orElseThrow(() -> new CustomLogicException(ErrorCode.CREATOR_NOT_FOUND));

        YearMonth settlementMonth = req.settlementMonth();

        // req.settlementMonth
        YearMonth now = YearMonth.now();
        // 과거: Settlement DB 조회 -> 없으면 생성 -> DB에 저장 + 반환
        if (settlementMonth.isBefore(now)) {
            OffsetDateTime startDate = dateTimeUtils.toStartDateTime(settlementMonth);
            OffsetDateTime endDate = dateTimeUtils.toEndDateTime(settlementMonth);

            Settlement settlement =
                    settlementRepository
                            .findByCreatorAndYearMonth(creator, req.settlementMonth().atDay(1))
                            .orElseGet(() -> {
                                try {
                                    return settlementRepository
                                            .saveAndFlush(
                                                    createSettlement(
                                                            creator,
                                                            settlementMonth,
                                                            startDate,
                                                            endDate
                                                    )
                                            );
                                } catch (DataIntegrityViolationException e) {
                                    throw new CustomLogicException(ErrorCode.SETTLEMENT_CONFLICT);
                                }
                            });

            return toSettlementMonthlyReadResponse(settlement);
        } else if (settlementMonth.equals(now)) { // 현재: List<Sale>, List<Cancel> 조회 후 계산 + 반환
            OffsetDateTime startDate = dateTimeUtils.toStartDateTime(req.settlementMonth());
            OffsetDateTime endDate = OffsetDateTime.now(); // 월 1일부터 현재 날짜까지 데이터 조회

            Settlement settlement = createSettlement(
                    creator,
                    settlementMonth,
                    startDate,
                    endDate
            );
            return toSettlementMonthlyReadResponse(settlement);
        } else { // 미래: 예외 처리
            throw new CustomLogicException(ErrorCode.INVALID_DATE);
        }
    }

    @Transactional(readOnly = true)
    public SettlementSummaryReadResponse getSettlementSummary(
            SettlementSummaryReadRequest req
    ) {

        LocalDate startDate = req.startMonth().atDay(1);
        LocalDate endDate = req.endMonth().atEndOfMonth();
        
        BigDecimal totalPendingAmount = BigDecimal.ZERO;
        BigDecimal totalConfirmedAmount = BigDecimal.ZERO;
        BigDecimal totalPaidAmount = BigDecimal.ZERO;

        // 크리에이터별 정산 합산
        List<SettlementSummaryRow> settlementSummaryRows =
                settlementRepository.findBySettlementMonthBetween(
                        startDate, endDate
                );
        
        Map<String, CreatorSettlementSummary> creatorSettlementSummaryMap = new HashMap<>();
        for (SettlementSummaryRow row : settlementSummaryRows) {
            creatorSettlementSummaryMap.computeIfAbsent(row.creatorId(), creatorId ->
                    new CreatorSettlementSummary(creatorId)
            );

            BigDecimal amount = row.amount();
            CreatorSettlementSummary summary = creatorSettlementSummaryMap.get(row.creatorId());
            switch(row.status()) {
                case SettlementStatus.PENDING -> {
                    summary.addPendingAmount(amount);
                    totalPendingAmount = totalPendingAmount.add(amount);
                }
                case SettlementStatus.CONFIRMED -> {
                    summary.addConfirmedAmount(amount);
                    totalConfirmedAmount = totalConfirmedAmount.add(amount);
                }
                case SettlementStatus.PAID -> {
                    summary.addPaidAmount(amount);
                    totalPaidAmount = totalPaidAmount.add(amount);
                }
            }

        }

        SettlementTotalSummary total = 
                new SettlementTotalSummary(
                        totalPendingAmount, 
                        totalConfirmedAmount, 
                        totalPaidAmount
                );

        return new SettlementSummaryReadResponse(
                creatorSettlementSummaryMap.values().stream().toList(),
                total
        );
    }

    private Settlement createSettlement(
            Creator creator,
            YearMonth settlementMonth,
            OffsetDateTime startDate,
            OffsetDateTime endDate
    ) {

        CommissionRate commissionRate = commissionRateRepository.getCurrentCommissionRate()
                .orElseThrow(() -> new CustomLogicException(ErrorCode.COMMISSION_NOT_FOUND));

        SalesAndCancelsSummaryDto summary =
                calculateTotalSaleAndCancel(
                        creator,
                        startDate,
                        endDate
                );

        SettlementAmountsDto amounts =
                calculateAmounts(
                        summary.totalSaleAmount(),
                        summary.totalCancelAmount(),
                        commissionRate.getRate()
                );


        return Settlement.builder()
                .creator(creator)
                .settlementMonth(settlementMonth)
                .totalSaleAmount(summary.totalSaleAmount())
                .totalCancelAmount(summary.totalCancelAmount())
                .netSaleAmount(amounts.netSaleAmount())
                .commission(amounts.commission())
                .totalSettlementAmount(amounts.totalSettlementAmount())
                .saleCount(summary.saleCount())
                .cancelCount(summary.cancelCount())
                .commissionRate(commissionRate)
                .build();
    }

    private SalesAndCancelsSummaryDto calculateTotalSaleAndCancel(
            Creator creator,
            OffsetDateTime startDate,
            OffsetDateTime endDate
    ) {
        // 총 판매 금액 계산 + 총 판매 수
        BigDecimal totalSaleAmount = BigDecimal.ZERO;

        List<SaleRecord> saleRecords =
                saleRecordRepository.findByCreatorAndPaidAtBetween(
                        creator,
                        startDate,
                        endDate
                );

        for (SaleRecord saleRecord : saleRecords) {
            totalSaleAmount = totalSaleAmount.add(saleRecord.getAmount());
        }

        // 총 취소 금액 계산 + 총 취소 수
        BigDecimal totalCancelAmount = BigDecimal.ZERO;

        List<Cancel> cancels =
                cancelRepository.findByCreatorAndCanceledAtBetween(
                        creator,
                        startDate,
                        endDate
                );

        for (Cancel cancel : cancels) {
            totalCancelAmount = totalCancelAmount.add(cancel.getAmount());
        }

        return new SalesAndCancelsSummaryDto(
                totalSaleAmount,
                totalCancelAmount,
                saleRecords.size(),
                cancels.size()
        );
    }

    private SettlementAmountsDto calculateAmounts(
            BigDecimal totalSaleAmount,
            BigDecimal totalCancelAmount,
            BigDecimal commissionRate
    ) {

        // 순 판매 금액 계산(총 판매 - 환불)
        BigDecimal netSaleAmount = totalSaleAmount.subtract(totalCancelAmount);

        // 수수료(순판매의 현재 수수료율 적용 값(20%), 순판매가 음수면 수수료 적용 X)
        BigDecimal commission = BigDecimal.ZERO;
        if (netSaleAmount.compareTo(commission) > 0) {
            commission = netSaleAmount
                    .multiply(commissionRate)
                    .divide(ONE_HUNDRED, 2, RoundingMode.HALF_UP);
        }

        // 정산 예정 금액 계산(순 판매 - 수수료)
        BigDecimal totalSettlementAmount = netSaleAmount.subtract(commission);

        return new SettlementAmountsDto(
                netSaleAmount,
                commission,
                totalSettlementAmount
        );
    }

    private SettlementMonthlyReadResponse toSettlementMonthlyReadResponse(Settlement settlement) {
        return new SettlementMonthlyReadResponse(
                settlement.getTotalSaleAmount(),
                settlement.getTotalCancelAmount(),
                settlement.getNetSaleAmount(),
                settlement.getCommission(),
                settlement.getTotalSettlementAmount(),
                settlement.getSaleCount(),
                settlement.getCancelCount()
        );
    }

}
