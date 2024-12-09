package com.app.cwave_nintendo.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SeatAlertMessage {

	private Integer seatNumber;
	private AlertType alertType;
	private String message;
	private LocalDateTime alertTime;

	public enum AlertType {
		TIME_WARNING,    // 종료 임박 경고
		TIME_ENDED      // 시간 종료
	}
}
