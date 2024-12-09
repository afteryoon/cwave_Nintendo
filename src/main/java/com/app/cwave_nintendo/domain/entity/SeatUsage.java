package com.app.cwave_nintendo.domain.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SeatUsage {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "seat_usage_id")
	private Long SeatUsageId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "seat_id")
	private Seat seat;

	@Column
	private LocalDateTime startTime;
	@Column
	private LocalDateTime endTime;
	@Column
	private Integer usageTime;  // 실제 사용 시간(분)
	@Column
	private Boolean isCompleted; //끝나면 true

	//연관 관계 메서드
	public void addSeat(Seat seat) {
		this.seat = seat;
	}

	//business mthod
	public void endUsage() {
		this.endTime = LocalDateTime.now();
		this.isCompleted = true;
		this.usageTime = (int) ChronoUnit.MINUTES.between(this.getStartTime(), LocalDateTime.now());
	}

}
