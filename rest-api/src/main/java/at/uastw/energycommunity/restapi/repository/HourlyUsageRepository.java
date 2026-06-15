package at.uastw.energycommunity.restapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface HourlyUsageRepository extends JpaRepository<HourlyUsageEntity, LocalDateTime> {

    List<HourlyUsageEntity> findByHourBetweenOrderByHour(LocalDateTime start, LocalDateTime end);
}
