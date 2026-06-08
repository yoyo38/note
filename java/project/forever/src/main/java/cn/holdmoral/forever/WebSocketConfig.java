package cn.holdmoral.forever;

import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;

/**
 * @author lujianhua
 * @version 1.0
 * @company holdmoral
 * @date 2022/1/19 16:39
 */
@Slf4j
@Configuration
public class WebSocketConfig {
    @Value("${client.id}")
    private String clientId;
    @Bean
    public WebSocketClient webSocketClient() {
        try {
//            WebSocketClient webSocketClient = new WebSocketClient(new URI("ws://192.168.2.199:8088/ws/gkm123abc"),new Draft_6455()) {
//                @Override
//                public void onOpen(ServerHandshake handshakedata) {
//                    //heartbeat(this);
//                    log.info("===建立连接,心跳保活开启===");
//                }
//
//                @Override
//                public void onMessage(String message) {
//                    log.info("[websocket] 收到消息={}",message);
//
//                }
//
//                @Override
//                public void onClose(int code, String reason, boolean remote) {
//                    log.info("[websocket] 退出连接");
//                }
//
//                @Override
//                public void onError(Exception ex) {
//                    log.info("[websocket] 连接错误={}",ex.getMessage());
//                }
//            };
            //ws://wlw.farmmanager.cn:18087/ws/
            WebSocketClient webSocketClient = new LocalWebSocketClient(new URI("ws://192.168.2.199:8088/ws/"+clientId));
            webSocketClient.connect();
            return webSocketClient;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
