package com.app.cwave_nintendo.repository.impl;

import java.util.Optional;

import com.app.cwave_nintendo.domain.entity.QSeat;
import com.app.cwave_nintendo.domain.entity.QSeatUsage;
import com.app.cwave_nintendo.repository.SeatRepositoryCustom;
import com.app.cwave_nintendo.dto.response.SeatWithUsageDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SeatRepositoryCustomImpl implements SeatRepositoryCustom {

	private final JPAQueryFactory queryFactory;


	@Override
	public Optional<SeatWithUsageDto> findSeatWithCurrentUsage(Integer seatNumber) {

		QSeat seat = QSeat.seat;
		QSeatUsage usage = QSeatUsage.seatUsage;

		return Optional.ofNullable(queryFactory
			.select(Projections.constructor(SeatWithUsageDto.class,
				seat,
				usage))
			.from(seat)
			.leftJoin(seat.seatUsages, usage)
			.where(seat.seatNumber.eq(seatNumber)
				.and(usage.isCompleted.eq(false)))
			.fetchOne());
	}

	// @Override
	// public Optional<SeatWithUsageDto> findSeatWithCurrentUsage(Integer seatNumber) {
	// 	return Optional.empty();
	// }
}
