package com.app.cwave_nintendo.dto.response;

import java.time.LocalDateTime;

import com.app.cwave_nintendo.domain.entity.Reservation;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationResponse {
	private Long reservationId;
	private String visitorName;
	private String phoneNumber;
	private LocalDateTime reservationTime;  // 예약 신청 시간

	public static ReservationResponse reservationToResponse(Reservation reservation) {
		return ReservationResponse.builder()
			.reservationId(reservation.getReservationId())
			.visitorName(reservation.getVisitorName())
			.phoneNumber(reservation.getPhoneNumber())
			.reservationTime(reservation.getReservationTime())
			.build();
	}
}
