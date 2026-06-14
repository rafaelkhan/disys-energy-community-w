package at.uastw.energycommunity.percentageservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface CurrentPercentageRepository extends JpaRepository<CurrentPercentageEntity, LocalDateTime> {
}
