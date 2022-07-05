package com.meta.websocket;

import com.google.gson.Gson;
import com.meta.model.MetaClaims;
import com.meta.model.enums.AccountTypeEnum;
import com.meta.model.request.ReadRecordWebSocketRequest;
import com.meta.model.request.UpdateReadRecordRequest;
import com.meta.service.ReadRecordServiceImpl;
import com.meta.utils.OauthJWTUtil;
import com.meta.utils.RedisKeysUtil;
import com.meta.utils.RedisUtil;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.Map;
import java.util.function.Consumer;

@Log4j2
@Component
public class ReadRecordHandler extends AbstractWebSocketHandler {

    @Resource
    private WebSocketService webSocketService;
    @Resource
    private OauthJWTUtil oauthJWTUtil;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private ReadRecordServiceImpl readRecordService;

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
        if (ObjectUtils.isEmpty(request.getAuthorization())){
            throw new RuntimeException("AUTHORIZATION_ERROR");
        }
        log.info("解析 token ");
        MetaClaims claims = oauthJWTUtil.getClaims(request.getAuthorization());
        if (ObjectUtils.isEmpty(claims.getAccountId())){
            throw new RuntimeException("AUTHORIZATION_ERROR");
        }
        if (ObjectUtils.isEmpty(request.getSourceId()) || ObjectUtils.isEmpty(request.getSourceType())){
            throw new RuntimeException("SOURCE_ERROR");
        }
        if (ObjectUtils.isEmpty(request.getPageIndex())){
            throw new RuntimeException("PAGE_INDEX_ERROR");
        }
        log.info("获取 redis 存在的阅读记录");
        String readRecordKey = RedisKeysUtil.readRecordKey(session.getId(), request.getSourceId(), claims.getAccountId());
        Map<Object, Object> readRecordMap = redisUtil.hGetAll(readRecordKey);
        if (ObjectUtils.isNotEmpty(readRecordMap)){
            log.info("redis 中存在阅读记录");
            // 获取页码阅读时间
            readRecordMap.keySet().stream().forEach((pageIndexObject)->{
                Long pageIndex = Long.valueOf(pageIndexObject.toString());
                Object timesObject = readRecordMap.get(pageIndexObject);
                Long times = Long.valueOf(timesObject.toString());
                // 计算阅读时间
                long currentTimeMillis = System.currentTimeMillis();
                long readTimes = (currentTimeMillis - times) / 1000l;
                log.info("阅读页码:{}，阅读开始时间:{}，阅读时间:{}", pageIndex, times, readTimes);
                if (0 < readTimes){
                    // 增加阅读记录
                    Timestamp startTime = new Timestamp(times);
                    UpdateReadRecordRequest updateReadRecordRequest = UpdateReadRecordRequest.builder().sourceId(request.getSourceId())
                            .sourceType(request.getSourceType())
                            .accountId(claims.getAccountId())
                            .accountType(AccountTypeEnum.MEMBERS)
                            .pageIndex(pageIndex)
                            .startTime(startTime)
                            .times(readTimes)
                            .webSocketSessionId(session.getId()).build();
                    readRecordService.updateReadRecord(updateReadRecordRequest);
                }
            });
            // 删除旧的阅读记录
            redisUtil.delete(readRecordKey);
            // 新增阅读记录
            redisUtil.hPut(readRecordKey, request.getPageIndex().toString(), String.valueOf(System.currentTimeMillis()));
        }else{
            log.info("redis 不存在阅读记录");
            // 新增阅读记录
            redisUtil.hPut(readRecordKey, request.getPageIndex().toString(), String.valueOf(System.currentTimeMillis()));
        }
    }

    /**
     * 发生异常关闭连接
     * */
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.error("发生异常:{}", exception.getMessage());
        webSocketService.sendMsg(session, exception.getMessage());
        log.info("删除{}次连接缓存", session.getId());
        String readRecordLikeKey = RedisKeysUtil.readRecordLikeKey(session.getId());
        redisUtil.deleteAll(readRecordLikeKey);
        log.info("关闭 websocket 连接, session:{}", session.getId());
        WebSocketSessionManager.removeAndClose(session.getId());
    }

    /**
     * socket 连接关闭后触发
     * */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info("删除{}次连接缓存", session.getId());
        String readRecordLikeKey = RedisKeysUtil.readRecordLikeKey(session.getId());
        redisUtil.deleteAll(readRecordLikeKey);
        log.info("关闭 websocket 连接, session:{}", session.getId());
        WebSocketSessionManager.removeAndClose(session.getId());
    }



}
