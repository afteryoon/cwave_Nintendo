package com.app.cwave_nintendo.dto.response;

import java.time.LocalDateTime;

import com.app.cwave_nintendo.domain.entity.Seat;
import com.app.cwave_nintendo.domain.enums.SeatStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SeatResponse {

	private Long id;
	private Integer seatNumber;
	private Boolean isOccupied;
	private LocalDateTime startTime;
	private LocalDateTime endTime;
	private String status;
	private Integer remainingMinutes;
	private Long seatUsageId;
	private DailyStatsResponse dailyStats;

	public static SeatResponse seatToResponse(Seat seat) {
		return  SeatResponse.builder()
			.id(seat.getSeatId())
			.seatNumber(seat.getSeatNumber())
			.isOccupied(seat.isOccupied())
			.startTime(seat.getStartTime())
			.endTime(seat.getEndTime())
			.status(seat.getStatus().toString())
			.remainingMinutes(seat.getRemainingMinutes())
			.build();
	}

	public static SeatResponse seatToResponse(Seat seat,Long seatUsageId, DailyStatsResponse dailyStats) {
		return  SeatResponse.builder()
			.id(seat.getSeatId())
			.seatNumber(seat.getSeatNumber())
			.isOccupied(seat.isOccupied())
			.startTime(seat.getStartTime())
			.endTime(seat.getEndTime())
			.status(seat.getStatus().toString())
			.remainingMinutes(seat.getRemainingMinutes())
			.seatUsageId(seatUsageId)
			.dailyStats(dailyStats)
			.build();
	}
}
