package io.mipangg.liveklasscreatorsettlementapi.domain.cancel.service;

import io.mipangg.liveklasscreatorsettlementapi.domain.cancel.dto.CancelCreateRequest;
import io.mipangg.liveklasscreatorsettlementapi.domain.cancel.entity.Cancel;
import io.mipangg.liveklasscreatorsettlementapi.domain.cancel.repository.CancelRepository;
import io.mipangg.liveklasscreatorsettlementapi.domain.salerecord.entity.SaleRecord;
import io.mipangg.liveklasscreatorsettlementapi.domain.salerecord.repository.SaleRecordRepository;
import io.mipangg.liveklasscreatorsettlementapi.global.exception.CustomLogicException;
import io.mipangg.liveklasscreatorsettlementapi.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CancelService {

    private final CancelRepository cancelRepository;
    private final SaleRecordRepository saleRecordRepository;

    public void saveCancel(CancelCreateRequest req) {

        SaleRecord saleRecord = saleRecordRepository.findById(req.saleId())
                .orElseThrow(() -> new CustomLogicException(ErrorCode.SALE_RECORD_NOT_FOUND));

        try {
            cancelRepository.save(
                    Cancel.builder()
                            .saleRecord(saleRecord)
                            .amount(req.amount())
                            .canceledAt(req.canceledAt())
                            .build()
            );
        } catch (DataIntegrityViolationException e) {
            throw new CustomLogicException(ErrorCode.CANCEL_CONFLICT);
        }

    }
}
