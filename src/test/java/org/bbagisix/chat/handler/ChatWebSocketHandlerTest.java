package org.bbagisix.chat.handler;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

public class ChatWebSocketHandlerTest {

	@Test
	void 메시지를_받으면_Echo로_응답한다() throws Exception {
		// given
		ChatWebSocketHandler handler = new ChatWebSocketHandler();
		WebSocketSession session = mock(WebSocketSession.class);
		TextMessage incoming = new TextMessage("hello");

		ArgumentCaptor<TextMessage> captor = ArgumentCaptor.forClass(TextMessage.class);

		// when
		handler.handleTextMessage(session, incoming);

		// then
		verify(session).sendMessage(captor.capture());
		assertEquals("Echo: hello", captor.getValue().getPayload());
	}
}
