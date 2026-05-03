package io.mipangg.liveklasscreatorsettlementapi.domain.creator.repository;

import io.mipangg.liveklasscreatorsettlementapi.domain.creator.entity.Creator;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreatorRepository extends JpaRepository<Creator, Long> {

    Optional<Creator> findById(String id);

}
