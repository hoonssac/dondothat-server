package org.bbagisix.chat.handler;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		String payload = message.getPayload();
		log.info("받은 메시지: {}", payload);

		// Echo 응답
		session.sendMessage(new TextMessage("Echo: " + payload));
	}

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		log.info("웹소캣 연결됨: {}", session.getId());
	}
}
