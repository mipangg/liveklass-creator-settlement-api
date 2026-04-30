package io.mipangg.liveklasscreatorsettlementapi.domain.salerecord.service;

import io.mipangg.liveklasscreatorsettlementapi.domain.salerecord.dto.SaleRecordCreateRequest;
import io.mipangg.liveklasscreatorsettlementapi.domain.salerecord.repository.SaleRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SaleRecordService {

    private final SaleRecordRepository saleRecordRepository;

    public void saveSaleRecord(SaleRecordCreateRequest req) {

    }
}
