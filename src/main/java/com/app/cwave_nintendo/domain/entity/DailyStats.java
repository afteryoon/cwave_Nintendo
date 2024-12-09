package com.app.cwave_nintendo.domain.entity;

import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailyStats {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "daily_stats_id")
	private Long dailyStatsId;

	@Column(unique = true)
	private LocalDate date;

	@Column
	private Integer totalUsers;         // 총 이용자 수
	@Column
	private Double averageUsageTime;    // 평균 이용 시간
	@Column
	private Integer peakConcurrentUsers; // 최대 동시 이용자 수
	@Column
	private Integer currentUsers;		//현재 이용 유저
	@Column
	private Integer amountTime;			//총 이용 시간

	//비지니스
	//새로운 좌석 추가
	public void addSeat(){
		this.totalUsers++;
		this.currentUsers++;
		this.peakConcurrentUsers = Math.max(currentUsers, peakConcurrentUsers);
	}
	//좌석 종료
	public void removeSeat(SeatUsage usage) {
		this.currentUsers--;
		this.amountTime += usage.getUsageTime();
		this.averageUsageTime = (double)(amountTime/totalUsers);
	}
}
