package com.app.cwave_nintendo.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.cwave_nintendo.domain.entity.Reservation;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

	Collection<Reservation> findAllByOrderByReservationTimeAsc();
}
