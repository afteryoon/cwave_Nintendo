package com.app.cwave_nintendo.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.cwave_nintendo.domain.entity.DailyStats;

@Repository
public interface DailyStatsRepository extends JpaRepository<DailyStats, Long> {

	Optional<DailyStats> findByDate(LocalDate now);
}
