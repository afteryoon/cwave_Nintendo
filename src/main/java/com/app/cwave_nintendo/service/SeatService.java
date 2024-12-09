package com.app.cwave_nintendo.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.cwave_nintendo.domain.entity.DailyStats;
import com.app.cwave_nintendo.domain.entity.Reservation;
import com.app.cwave_nintendo.domain.entity.Seat;
import com.app.cwave_nintendo.domain.entity.SeatUsage;
import com.app.cwave_nintendo.domain.enums.ReservationStatus;
import com.app.cwave_nintendo.domain.enums.SeatStatus;
import com.app.cwave_nintendo.dto.request.ReservationRequest;
import com.app.cwave_nintendo.dto.response.DailyStatsResponse;
import com.app.cwave_nintendo.dto.response.ReservationResponse;
import com.app.cwave_nintendo.dto.response.SeatAlertMessage;
import com.app.cwave_nintendo.dto.response.SeatResponse;
import com.app.cwave_nintendo.dto.response.SeatWithUsageDto;
import com.app.cwave_nintendo.repository.DailyStatsRepository;
import com.app.cwave_nintendo.repository.ReservationRepository;
import com.app.cwave_nintendo.repository.SeatRepository;
import com.app.cwave_nintendo.repository.SeatUsageRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class SeatService {
	private final SeatRepository seatRepository;
	private final SeatUsageRepository seatUsageRepository;
	private final SeatAlertHandler seatAlertHandler;
	private final DailyStatsRepository dailyStatsRepository;
	private final ReservationRepository reservationRepository;

	//전체 좌석표
	public List<SeatResponse> getAllSeats() {

		List<Seat> seats = seatRepository.findAll();
		return seats.stream()
			.map(SeatResponse::seatToResponse)
			.toList();
	}

	//금일 집계표
	@Transactional
	public DailyStatsResponse getTodayStats() {
		DailyStats dailyStats = dailyStatsRepository.findByDate(LocalDate.now())
			.orElseGet(() -> dailyStatsRepository.save(createDailyStats()));

		return DailyStatsResponse.dailyStatsToResponse(dailyStats);
	}

	@Scheduled(fixedRate = 60000)
	public void updateSeatsTime() {
		List<Seat> activeSeats = seatRepository.findByStatus(SeatStatus.IN_USE);
		if (!activeSeats.isEmpty()) {  // 활성화된 좌석이 있을 때만 전송
			List<SeatResponse> responses = activeSeats.stream()
				.map(SeatResponse::seatToResponse)
				.toList();

			seatAlertHandler.sendAlertList(responses);
			log.debug("좌석 시간 업데이트 전송 완료 - {} 개", responses.size());
		}
	}

	// 좌석 사용 시작
	@Transactional
	public SeatResponse  startUsage(Integer seatNumber) {
		Seat seat = seatRepository.findBySeatNumber(seatNumber)
			.orElseThrow(() -> new IllegalArgumentException("좌석을 찾을 수 없습니다."));

		if (seat.isOccupied()) {
			throw new IllegalStateException("이미 사용 중인 좌석입니다.");
		}

		DailyStats dailyStats = dailyStatsRepository.findByDate(LocalDate.now())
			.orElseGet(() -> dailyStatsRepository.save(createDailyStats()));

		dailyStats.addSeat();
		// 좌석 시작
		seat.seatStart();

		// 사용 기록 생성
		SeatUsage usage = createSeatUsage(seat);
		seat.addSeatUsage(usage);

		seatRepository.save(seat);

		return SeatResponse.seatToResponse(seat,usage.getSeatUsageId(),DailyStatsResponse.dailyStatsToResponse(dailyStats));
	}

	//대기 목록
	public List<ReservationResponse> getAllReservation() {
		return reservationRepository.findAllByOrderByReservationTimeAsc().stream()
			.map(ReservationResponse::reservationToResponse)
			.toList();
	}

	// 좌석 사용 종료
	@Transactional
	public SeatResponse endUsage(Integer seatNumber) {

		// 현재 진행 중인 사용 기록 조회 및 업데이트
		 SeatWithUsageDto seatWithUsageDto = seatUsageRepository.findSeatWithCurrentUsage(seatNumber)
			.orElseThrow(() -> new IllegalStateException("사용 기록을 찾을 수 없습니다."));

		if (!seatWithUsageDto.getSeat().isOccupied()) {
			throw new IllegalStateException("이미 비어있는 좌석입니다.");
		}

		DailyStats dailyStats = dailyStatsRepository.findByDate(LocalDate.now())
			.orElseGet(() -> dailyStatsRepository.save(createDailyStats()));

		seatWithUsageDto.getCurrentUsage().endUsage();
		seatWithUsageDto.getSeat().seatEnd();

		//금일 집계표 최신화
		dailyStats.removeSeat(seatWithUsageDto.getCurrentUsage());

		Seat updateSeat = seatRepository.save(seatWithUsageDto.getSeat());
		SeatUsage updateSeatUsage = seatUsageRepository.save(seatWithUsageDto.getCurrentUsage());

		return SeatResponse.seatToResponse(updateSeat,updateSeatUsage.getSeatUsageId(),DailyStatsResponse.dailyStatsToResponse(dailyStats));
	}

	//예약자 추가
	@Transactional
	public ReservationResponse addReservation(ReservationRequest request) {

		return ReservationResponse.reservationToResponse(reservationRepository.save(createReservation(request)));
	}

	//예약 삭제
	@Transactional
	public void deleteReservation(Long currentReservationId) {
		reservationRepository.deleteById(currentReservationId);
	}


	public String formatPhoneNumber(String phoneNumber) {
		// 숫자만 추출
		String digits = phoneNumber.replaceAll("\\D", "");

		// 11자리일 경우에만 포맷 적용
		if (digits.length() == 11) {
			return digits.replaceFirst("(\\d{3})(\\d{4})(\\d{4})", "$1-$2-$3");
		}

		// 포맷이 불가능하면 원래 값 반환
		return phoneNumber;
	}

	//convert
	// 금일 결산 테이블 생성
	private DailyStats createDailyStats() {
		return DailyStats.builder()
			.date(LocalDate.now())
			.totalUsers(0)
			.peakConcurrentUsers(0)
			.currentUsers(0)
			.amountTime(0)
			.build();
	}

	//사용 기록 생성
	public SeatUsage createSeatUsage(Seat seat) {
		return SeatUsage.builder()
			.seat(seat)
			.startTime(LocalDateTime.now())
			.isCompleted(false)
			.build();
	}

	// 시간 알람 메세지 작성 - 경고
	private SeatAlertMessage createSeatAlertMessage(Seat seat, String timeLeft) {
		return SeatAlertMessage.builder()
			.seatNumber(seat.getSeatNumber())
			.alertType(SeatAlertMessage.AlertType.TIME_WARNING)
			.message(seat.getSeatNumber() + "번 좌석 이용 시간 종료까지 " + timeLeft + " 남았습니다.")
			.alertTime(LocalDateTime.now())
			.build();
	}

	//시간 알람 메세지 작성 - 종료
	private SeatAlertMessage createSendEndAlert(Seat seat) {
		return SeatAlertMessage.builder()
			.seatNumber(seat.getSeatNumber())
			.alertType(SeatAlertMessage.AlertType.TIME_ENDED)
			.message(seat.getSeatNumber() + "번 좌석 이용 시간이 종료되었습니다.")
			.alertTime(LocalDateTime.now())
			.build();
	}

	private Reservation createReservation(ReservationRequest request){
		return Reservation.builder()
			.phoneNumber(formatPhoneNumber(request.getPhoneNumber()))
			.visitorName(request.getVisitorName())
			.status(ReservationStatus.WAIT)
			.reservationTime(LocalDateTime.now())
			.build();
	}

}
