package com.meta.websocket;

import com.google.gson.Gson;
import com.meta.model.request.ReadRecordRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

@Log4j2
@Component
public class WebSocketHandler extends AbstractWebSocketHandler {

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        //socket连接成功后触发
        log.info("建立websocket连接");
        WebSocketSessionManager.add(session.getId(), session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // 客户端发送普通文件信息时触发
        log.info("发送文本消息");
        // 获得客户端传来的消息
        String payload = message.getPayload();
        log.info("服务端接收到消息 " + payload);
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        if (message.getPayloadLength() == 0) {
            return;
        }
        ReadRecordRequest msg = new Gson().fromJson(message.getPayload().toString(), ReadRecordRequest.class);
        log.info("服务端接收到消息 " + msg.getSourceId());
    }

    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) throws Exception {
        //客户端发送二进信息是触发
        log.info("发送二进制消息");
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        //异常时触发
        log.error("异常处理");
        WebSocketSessionManager.removeAndClose(session.getId());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // socket连接关闭后触发
        log.info("关闭websocket连接");
        WebSocketSessionManager.removeAndClose(session.getId());
    }



}
