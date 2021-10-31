package com.meta.websocket;

import com.google.gson.Gson;
import com.meta.model.request.ReadRecordRequest;
import com.meta.model.request.ReadRecordWebSocketRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

@Log4j2
@Component
public class ReadRecordHandler extends AbstractWebSocketHandler {

    @Autowired
    private WebSocketService webSocketService;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        WebSocketSessionManager.add(session.getId(), session);
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        if (message.getPayloadLength() == 0) {
            return;
        }
        ReadRecordWebSocketRequest request = new Gson().fromJson(message.getPayload().toString(), ReadRecordWebSocketRequest.class);
        // 更新阅读次数中的阅读时间

        // 更新阅读记录中的阅读时间
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
