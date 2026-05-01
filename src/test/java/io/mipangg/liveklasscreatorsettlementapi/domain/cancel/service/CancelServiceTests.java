package io.mipangg.liveklasscreatorsettlementapi.domain.cancel.service;

import static io.mipangg.liveklasscreatorsettlementapi.global.TestUtil.genCancelCreateRequest;
import static io.mipangg.liveklasscreatorsettlementapi.global.TestUtil.genSaleRecords;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.mipangg.liveklasscreatorsettlementapi.domain.cancel.dto.CancelCreateRequest;
import io.mipangg.liveklasscreatorsettlementapi.domain.cancel.entity.Cancel;
import io.mipangg.liveklasscreatorsettlementapi.domain.cancel.repository.CancelRepository;
import io.mipangg.liveklasscreatorsettlementapi.domain.salerecord.entity.SaleRecord;
import io.mipangg.liveklasscreatorsettlementapi.domain.salerecord.repository.SaleRecordRepository;
import io.mipangg.liveklasscreatorsettlementapi.global.exception.CustomLogicException;
import io.mipangg.liveklasscreatorsettlementapi.global.exception.ErrorCode;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

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

        CancelCreateRequest req = genCancelCreateRequest();
        SaleRecord saleRecord = genSaleRecords().get(2);

        when(saleRecordRepository.findById(anyString())).thenReturn(Optional.of(saleRecord));

        cancelService.saveCancel(req);

        verify(saleRecordRepository).findById(anyString());
        verify(cancelRepository).save(any(Cancel.class));

    }

    @Test
    @DisplayName("이미 존재하는 취소 내역을 등록하려 시도하면 예외가 발생한다")
    void saveCancelConflictFailTest() {

        CancelCreateRequest req = genCancelCreateRequest();
        SaleRecord saleRecord = genSaleRecords().get(2);

        when(saleRecordRepository.findById(anyString())).thenReturn(Optional.of(saleRecord));
        when(cancelRepository.save(any(Cancel.class)))
                .thenThrow(DataIntegrityViolationException.class);

        assertThatThrownBy(
                () -> {
                    cancelService.saveCancel(req);
                }
        ).isInstanceOf(CustomLogicException.class)
                .hasMessage(ErrorCode.CANCEL_CONFLICT.getMessage());

    }

}