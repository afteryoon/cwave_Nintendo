package com.app.cwave_nintendo.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.app.cwave_nintendo.dto.request.ReservationRequest;
import com.app.cwave_nintendo.dto.response.DailyStatsResponse;
import com.app.cwave_nintendo.dto.response.ReservationResponse;
import com.app.cwave_nintendo.dto.response.SeatResponse;
import com.app.cwave_nintendo.service.SeatService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MainController {

	private final SeatService seatService;

	@GetMapping("/")
	public String index(Model model) {

		List<SeatResponse> seats = seatService.getAllSeats();
		DailyStatsResponse dailyStats = seatService.getTodayStats();
		List<ReservationResponse> reservations = seatService.getAllReservation();

		model.addAttribute("seats", seats);
		model.addAttribute("stats",dailyStats);
		model.addAttribute("reservations", reservations);

		return "index";
	}

	//예약 추가
	@PostMapping("api/reservations")
	@ResponseBody
	public ResponseEntity<?> addReservation(@RequestBody ReservationRequest request) {
		log.info("name ::: {}",request.getVisitorName());
		log.info("number ::: {}",request.getPhoneNumber());
		ReservationResponse createReservation = seatService.addReservation(request);

		return  ResponseEntity.ok(createReservation);
	}
	//예약 삭제
	@DeleteMapping("/api/reservations/{currentReservationId}")
	public ResponseEntity<?> deleteReservation(@PathVariable("currentReservationId") Long currentReservationId) {

		seatService.deleteReservation(currentReservationId);

		return ResponseEntity.ok().build();
	}

	//좌석 시작
	@PostMapping("api/seats/{seatNumber}/start")
	@ResponseBody
	public ResponseEntity<?> seatStart(@PathVariable Integer seatNumber) {

		SeatResponse seatResponse = seatService.startUsage(seatNumber);

		return ResponseEntity.ok(seatResponse);
	}

	//좌석 시작
	@PostMapping("api/seats/{seatNumber}/end")
	@ResponseBody
	public ResponseEntity<?> seatEnd(@PathVariable Integer seatNumber) {

		SeatResponse seatResponse = seatService.endUsage(seatNumber);

		return ResponseEntity.ok(seatResponse);
	}


}
