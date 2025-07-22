<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>WebSocket Echo Test</title>
</head>
<body>
<h1>WebSocket Echo 테스트</h1>

<input type="text" id="messageInput" placeholder="메시지를 입력하세요">
<button onclick="sendMessage()">전송</button>

<div id="chatLog" style="margin-top:20px; border:1px solid #ccc; padding:10px;">
    <strong>메시지 로그:</strong>
    <div id="log"></div>
</div>

<script>
    const socket = new WebSocket("ws://localhost:8080/ws/chat");

    socket.onopen = () => {
        log("연결되었습니다.");
    };

    socket.onmessage = (event) => {
        log("수신: " + event.data);
    };

    socket.onclose = () => {
        log("연결이 종료되었습니다.");
    };

    function sendMessage() {
        const input = document.getElementById("messageInput");
        const msg = input.value;
        socket.send(msg);
        log("전송: " + msg);
        input.value = "";
    }

    function log(message) {
        const logDiv = document.getElementById("log");
        const p = document.createElement("p");
        p.textContent = message;
        logDiv.appendChild(p);
    }
</script>
</body>
</html>
