package io.mipangg.liveklasscreatorsettlementapi.domain.settlement.service;

import io.mipangg.liveklasscreatorsettlementapi.domain.settlement.dto.SettlementReadRequest;
import io.mipangg.liveklasscreatorsettlementapi.domain.settlement.dto.SettlementReadResponse;
import io.mipangg.liveklasscreatorsettlementapi.domain.settlement.repository.SettlementRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SettlementService {

    private final SettlementRepository settlementRepository;

    @Transactional(readOnly = true)
    public SettlementReadResponse findSettlement(@Valid SettlementReadRequest req) {
        return null;
    }
}
