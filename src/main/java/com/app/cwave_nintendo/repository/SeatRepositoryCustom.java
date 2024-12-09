package com.app.cwave_nintendo.repository;

import java.util.Optional;

import com.app.cwave_nintendo.dto.response.SeatWithUsageDto;

public interface SeatRepositoryCustom {

	Optional<SeatWithUsageDto> findSeatWithCurrentUsage(Integer seatNumber);

}
