package com.app.cwave_nintendo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.cwave_nintendo.domain.entity.SeatUsage;

@Repository
public interface SeatUsageRepository extends JpaRepository<SeatUsage, Long>, SeatRepositoryCustom{

}
