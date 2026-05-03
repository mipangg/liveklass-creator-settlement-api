package io.mipangg.liveklasscreatorsettlementapi.domain.settlement.service;

import io.mipangg.liveklasscreatorsettlementapi.domain.common.util.DateTimeUtils;
import io.mipangg.liveklasscreatorsettlementapi.domain.creator.entity.Creator;
import io.mipangg.liveklasscreatorsettlementapi.domain.creator.repository.CreatorRepository;
import io.mipangg.liveklasscreatorsettlementapi.domain.settlement.dto.SettlementReadRequest;
import io.mipangg.liveklasscreatorsettlementapi.domain.settlement.dto.SettlementReadResponse;
import io.mipangg.liveklasscreatorsettlementapi.domain.settlement.repository.SettlementRepository;
import io.mipangg.liveklasscreatorsettlementapi.global.exception.CustomLogicException;
import io.mipangg.liveklasscreatorsettlementapi.global.exception.ErrorCode;
import java.time.OffsetDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SettlementService {

    private final SettlementRepository settlementRepository;
    private final CreatorRepository creatorRepository;

    private final DateTimeUtils dateTimeUtils;

    @Transactional(readOnly = true)
    public SettlementReadResponse findSettlement(SettlementReadRequest req) {

        // 1. req.creator 존재하는지 확인 -> 없으면 Error
        Creator creator = creatorRepository.findById(req.creatorId())
                .orElseThrow(() -> new CustomLogicException(ErrorCode.CREATOR_NOT_FOUND));

        // 2. req.settlementMonth
        // 2-1. 과거: Settlement DB 조회 -> 없으면 생성 -> DB에 저장 + 반환

        // TODO: startDate, endDate 로그 찍어보기





        // 2-2. 현재: List<Sale>, List<Cancel> 조회 후 계산 + 반환
        // 2-3. 미래: 예외 처리

        return null;
    }
}
