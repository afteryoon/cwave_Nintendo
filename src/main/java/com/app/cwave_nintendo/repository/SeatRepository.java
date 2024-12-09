package com.app.cwave_nintendo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.cwave_nintendo.domain.entity.Seat;
import com.app.cwave_nintendo.domain.enums.SeatStatus;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {

	Optional<Seat> findBySeatNumber(Integer seatNumber);

	List<Seat> findByStatus(SeatStatus seatStatus);
}
