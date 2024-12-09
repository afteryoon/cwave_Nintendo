package com.app.cwave_nintendo.domain.entity;

import java.time.LocalDateTime;

import com.app.cwave_nintendo.domain.enums.ReservationStatus;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reservation {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "reservation_id")
	private Long reservationId;

	@Column(nullable = false)
	private String visitorName;  // 예약자 이름

	@Column(nullable = false)
	private String phoneNumber;  // 연락처

	@Column(nullable = false)
	private LocalDateTime reservationTime;  // 예약 신청 시간

	@Enumerated(EnumType.STRING)
	private ReservationStatus status;  // 예약 상태

}
