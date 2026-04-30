package io.mipangg.liveklasscreatorsettlementapi.domain.cancel.repository;

import io.mipangg.liveklasscreatorsettlementapi.domain.cancel.entity.Cancel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CancelRepository extends JpaRepository<Cancel, Long> {

}
