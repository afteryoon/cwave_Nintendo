package com.app.cwave_nintendo.domain.entity;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import com.app.cwave_nintendo.domain.enums.SeatStatus;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Seat {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "seat_id")
	private Long seatId;

	@Column(nullable = false, unique = true)
	private Integer seatNumber; // 실제 좌석 번호 (1-51)
	@Column(nullable = false)
	private boolean isOccupied; // 사용 중 여부
	@Column(nullable = false)
	private LocalDateTime startTime; // 시작 시간
	@Column(nullable = false)
	private LocalDateTime endTime; // 종료 예정 시간
	@Enumerated(EnumType.STRING)
	private SeatStatus status; // 좌석 상태 (AVAILABLE, IN_USE ,)
	@Transient
	private Integer remainingMinutes;

	@OneToMany(mappedBy = "seat" , cascade = CascadeType.ALL)
	private List<SeatUsage> seatUsages;

	//연관 관계 메서드
	public void addSeatUsage(SeatUsage seatUsage) {
		this.seatUsages.add(seatUsage);
		seatUsage.addSeat(this);
	}

	//business method
	//좌석 시작
	public void seatStart() {
		this.isOccupied = true;
		this.startTime = LocalDateTime.now();
		this.endTime =  LocalDateTime.now().plusHours(2);
		this.status = SeatStatus.IN_USE;
	}
	//좌석 초기화
	public void seatEnd() {
		this.isOccupied = false;
		this.endTime = null;
		this.startTime = null;
		this.status = SeatStatus.AVAILABLE;
	}

	//사용 확인
	public boolean isOccupied() {
		return this.isOccupied;
	}

	// 남은 시간 계산 메서드
	public Integer getRemainingMinutes() {
		if (!isOccupied || endTime == null) {
			return null;
		}

		long remainingMinutes = ChronoUnit.MINUTES.between(LocalDateTime.now(), endTime);
		return (int) (remainingMinutes < 0 ? 0 : remainingMinutes);
	}

}
