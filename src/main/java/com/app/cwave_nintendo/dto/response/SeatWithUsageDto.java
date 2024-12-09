package com.app.cwave_nintendo.dto.response;

import com.app.cwave_nintendo.domain.entity.Seat;
import com.app.cwave_nintendo.domain.entity.SeatUsage;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SeatWithUsageDto {
	private Seat seat;
	private SeatUsage currentUsage;

	public boolean hasCurrentUsage() {
		return currentUsage != null;
	}

}
