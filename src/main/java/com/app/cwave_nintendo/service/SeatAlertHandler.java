package com.app.cwave_nintendo.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.app.cwave_nintendo.dto.response.SeatAlertMessage;
import com.app.cwave_nintendo.dto.response.SeatResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class SeatAlertHandler extends TextWebSocketHandler {

	private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
	private final ObjectMapper objectMapper;

	@Override
	public void afterConnectionEstablished(WebSocketSession session) {
		log.info(" 모니터링 세션 연결: {}", session.getId());
		sessions.put(session.getId(), session);
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
		log.info(" 모니터링 세션 종료: {}", session.getId());
		sessions.remove(session.getId());
	}

	// 현재 사용중 리스트
	public void sendAlertList(List<SeatResponse> seatResponses) {
		try {
			String message = objectMapper.writeValueAsString(seatResponses);
			sendMessageToAll(message);
		} catch (JsonProcessingException e) {
			log.error("JSON 변환 실패 - 좌석 목록", e);
		}
	}

	private void sendMessageToAll(String message) {
		TextMessage textMessage = new TextMessage(message);
		sessions.values().forEach(session -> {
			try {
				if (session.isOpen()) {
					session.sendMessage(textMessage);
				}
			} catch (IOException e) {
				log.error("메시지 전송 실패. Session ID: {}", session.getId(), e);
			}
		});
	}
}
