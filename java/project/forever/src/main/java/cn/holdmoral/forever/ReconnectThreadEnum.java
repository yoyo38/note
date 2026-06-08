package cn.holdmoral.forever;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author lujianhua
 * @version 1.0
 * @company holdmoral
 * @date 2022/1/19 16:59
 */
public enum ReconnectThreadEnum {
    WebSocketInstance(){

        @Override
        public void reconnectWs(LocalWebSocketClient demoWebSocketClient) {
            cachedThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        //重连间隔一秒
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    demoWebSocketClient.reconnect();
                }

            });
        }

    };

    private static final ExecutorService cachedThreadPool = Executors.newCachedThreadPool();

    public abstract void reconnectWs(LocalWebSocketClient demoWebSocketClient);

    public static ReconnectThreadEnum getInstance(){
        return WebSocketInstance;
    }
}
