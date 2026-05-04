package io.mipangg.liveklasscreatorsettlementapi.domain.cancel.service;

import static java.math.BigDecimal.valueOf;
import static java.time.OffsetDateTime.parse;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.mipangg.liveklasscreatorsettlementapi.domain.cancel.dto.CancelCreateRequest;
import io.mipangg.liveklasscreatorsettlementapi.domain.cancel.entity.Cancel;
import io.mipangg.liveklasscreatorsettlementapi.domain.cancel.repository.CancelRepository;
import io.mipangg.liveklasscreatorsettlementapi.domain.salerecord.entity.SaleRecord;
import io.mipangg.liveklasscreatorsettlementapi.domain.salerecord.repository.SaleRecordRepository;
import io.mipangg.liveklasscreatorsettlementapi.global.TestUtil;
import io.mipangg.liveklasscreatorsettlementapi.global.exception.CustomLogicException;
import io.mipangg.liveklasscreatorsettlementapi.global.exception.ErrorCode;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CancelServiceTests {

    @InjectMocks
    private CancelService cancelService;

    @Mock
    private CancelRepository cancelRepository;

    @Mock
    private SaleRecordRepository saleRecordRepository;

    @Test
    @DisplayName("취소 내역을 등록할 수 있다")
    void saveCancelSuccessTest() {

        CancelCreateRequest req = TestUtil.genCancelCreateRequest();
        SaleRecord saleRecord = TestUtil.genSaleRecords().get(2);

        when(saleRecordRepository.findByIdForUpdate(req.saleId())).thenReturn(Optional.of(saleRecord));

        cancelService.saveCancel(req);

        verify(saleRecordRepository).findByIdForUpdate(anyString());
        verify(cancelRepository).save(any(Cancel.class));

    }

    @Test
    @DisplayName("한 판매에 대해 환불액의 합이 원결제액보다 적은 한에서 여러 환불을 등록할 수 있다")
    void saveCancelMultipleCancelsSuccessTest() {

        SaleRecord saleRecord = TestUtil.genSaleRecords().get(2);

        List<Cancel> cancels = new ArrayList<>();

        when(saleRecordRepository.findByIdForUpdate(anyString()))
                .thenReturn(Optional.of(saleRecord));

        when(cancelRepository.findBySaleRecord(saleRecord))
                .thenAnswer(invocation -> new ArrayList<>(cancels));

        doAnswer(invocation -> {
            Cancel cancel = invocation.getArgument(0);
            cancels.add(cancel);
            return cancel;
        }).when(cancelRepository).save(any(Cancel.class));

        cancelService.saveCancel(
                new CancelCreateRequest(
                        "sale-3", 
                        valueOf(20000),
                        parse("2025-03-21T09:00:00+09:00")
                )
        );
        cancelService.saveCancel(
                new CancelCreateRequest(
                        "sale-3", 
                        valueOf(30000),
                        parse("2025-03-22T09:00:00+09:00")
                )
        );

        assertThat(cancels).hasSize(2);

        BigDecimal total = BigDecimal.ZERO;
        for (Cancel c : cancels) {
            total = total.add(c.getAmount());
        }

        assertThat(total).isEqualByComparingTo("50000");
    }
    
    @Test
    @DisplayName("환불 총액이 원결제 액과 같아도 정상적으로 취소 등록할 수 있다")
    void saveCancelExactAmountSuccessTest() {

        SaleRecord saleRecord = TestUtil.genSaleRecords().get(2);

        when(saleRecordRepository.findByIdForUpdate(anyString()))
                .thenReturn(Optional.of(saleRecord));
        when(cancelRepository.findBySaleRecord(saleRecord)).thenReturn(List.of());

        cancelService.saveCancel(
                new CancelCreateRequest(
                        "sale-3",
                        valueOf(80000),
                        parse("2025-03-21T09:00:00+09:00")
                )
        );
    }

    @Test
    @DisplayName("환불액이 원결제 액보다 큰 경우 예외가 발생한다")
    void saveCancelFailTest() {

        SaleRecord saleRecord = TestUtil.genSaleRecords().get(2);

        when(saleRecordRepository.findByIdForUpdate(anyString()))
                .thenReturn(Optional.of(saleRecord));
        when(cancelRepository.findBySaleRecord(saleRecord)).thenReturn(List.of());

        assertThatThrownBy(
                () -> {
                    cancelService.saveCancel(
                            new CancelCreateRequest(
                                    "sale-3",
                                    valueOf(100000),
                                    parse("2025-03-21T09:00:00+09:00")
                            )
                    );
                }
        ).isInstanceOf(CustomLogicException.class)
                .hasMessage(ErrorCode.CANCEL_AMOUNT_EXCEEDED.getMessage());

    }

}