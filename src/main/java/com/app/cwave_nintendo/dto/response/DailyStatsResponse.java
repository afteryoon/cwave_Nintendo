package com.app.cwave_nintendo.dto.response;

import java.time.LocalDate;

import com.app.cwave_nintendo.domain.entity.DailyStats;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DailyStatsResponse {

	private Long id;
	private LocalDate date;
	private Integer totalUsers;
	private Double averageUsageTime;
	private Integer peakConcurrentUsers;
	private Integer currentUsers;
	private Integer amountTime;

	public static DailyStatsResponse dailyStatsToResponse(DailyStats dailyStats) {
		return DailyStatsResponse.builder()
			.id(dailyStats.getDailyStatsId())
			.date(dailyStats.getDate())
			.totalUsers(dailyStats.getTotalUsers())
			.averageUsageTime(dailyStats.getAverageUsageTime())
			.peakConcurrentUsers(dailyStats.getPeakConcurrentUsers())
			.currentUsers(dailyStats.getCurrentUsers())
			.amountTime(dailyStats.getAmountTime())
			.build();
	}
}
