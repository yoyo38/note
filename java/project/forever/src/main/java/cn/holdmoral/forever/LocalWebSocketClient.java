package cn.holdmoral.forever;

import cn.holdmoral.forever.model.FixedEnvConfigDto;
import cn.holdmoral.forever.model.WebSocketMessageDto;
import cn.holdmoral.forever.util.ThreadUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author lujianhua
 * @version 1.0
 * @company holdmoral
 * @date 2022/1/19 16:57
 */
@Slf4j
public class LocalWebSocketClient extends WebSocketClient {
    public static final String HEARTBEAT_CMD = "{ \"code\": 1}";

    public LocalWebSocketClient(URI serverUri) {
        super(serverUri, new Draft_6455());
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        //开启心跳保活
        heartbeat(this);
        log.info("===建立连接,心跳保活开启===");
    }

    @Override
    public void onMessage(String message) {
        log.info("{}时来自服务端的消息：{}", LocalDateTime.now(), message);
        WebSocketMessageDto messageRequestDto = JSON.parseObject(message, WebSocketMessageDto.class);
        if (messageRequestDto.getCode() == 200) {

        }
        if (messageRequestDto.getCode() == 1) {
            FixedEnvConfigDto envConfigDto = JSON.parseObject(message, FixedEnvConfigDto.class);
            if (envConfigDto.getTemperature() != null) {
                int temperature = (int) (envConfigDto.getTemperature() * 10);
                EnvCenter.setTemperatureByDeviceNumber(envConfigDto.getDeviceNumber(), temperature);
            }
            if(envConfigDto.getLightMode()!=null){
                EnvCenter.setLightByDeviceNumber(envConfigDto.getDeviceNumber(),envConfigDto);
            }
            EnvCenter.setWetWindowByDeviceNumber(envConfigDto.getDeviceNumber(),envConfigDto);
        }

//        if (messageRequestDto.getCode() == 2) {
//            //getRoomList
//            List<Room> roomList = new ArrayList<>();
//            List<Integer> activeList = EnvCenter.activeDeviceNumbers.values().stream().flatMap(Collection::stream).collect(Collectors.toList());
//            Collections.sort(activeList);
//            for (Integer activeDeviceNumber : activeList) {
//                Room room = new Room();
//                room.setId(activeDeviceNumber);
//                room.setRoomName("设备" + activeDeviceNumber);
//                TempEnv temp = EnvCenter.cacheTempMap.get(activeDeviceNumber);
//                if (temp != null) {
//                    room.setTemperature(temp.getTemperature());
//                    room.setHumi(temp.getHumi());
//                    room.setCO2(temp.getCO2());
//                    room.setNH3(temp.getNH3());
//                    if (temp.getVentilation() != null) {
//                        room.setVentilation(Float.parseFloat(temp.getVentilation().toString()));
//                    }
//                }
//                room.setDeviceState(false);
//                room.setSetTemperature(EnvCenter.cacheTempSetMap.get(activeDeviceNumber));
//                roomList.add(room);
//            }
//            MessageRequestDto<List<Room>> response = new MessageRequestDto();
//            response.setRequestId(messageRequestDto.getRequestId());
//            response.setCode(messageRequestDto.getCode() + 200);
//            response.setMessage("success");
//            response.setData(roomList);
//            this.send(JSON.toJSONString(response));
//        }
//        if (messageRequestDto.getCode() == 3) {
//            //getRoomDetail
//            MessageRequestDto<RoomDetailRequestData> requestDto = JSON.parseObject(message, new TypeReference<MessageRequestDto<RoomDetailRequestData>>() {
//            });
//            Integer roomId = requestDto.getData().getRoomID();
//            TempEnv temp = EnvCenter.getTemperatureByDeviceNumberFromCache(roomId);
//            RoomDetail roomDetail = new RoomDetail();
//            roomDetail.setId(roomId);
//            if (temp != null) {
//                roomDetail.setTemperature(temp.getTemperature());
//                roomDetail.setHumi(temp.getHumi());
//                roomDetail.setCO2(temp.getCO2());
//                roomDetail.setNH3(temp.getNH3());
//                roomDetail.setTargetVentilation(0f);
//                if (temp.getVentilation() != null) {
//                    roomDetail.setVentilation(Float.parseFloat(temp.getVentilation().toString()));
//                }
//            }
//            WindEnv windEnv = EnvCenter.getWindByDeviceNumberFromCache(roomId);
//            if (windEnv != null && windEnv.getWindState() != null && windEnv.getWindState().length() >= 8) {
//                int fanStatus = Short.valueOf(windEnv.getWindState().substring(0, 8), 2).intValue();
//                roomDetail.setFanStatus(fanStatus);
//            }
//            roomDetail.setSetTemp(EnvCenter.cacheTempSetMap.get(roomId));
//            RoomDetailOut roomDetailOut = new RoomDetailOut();
//            roomDetailOut.setRoom(roomDetail);
//            MessageRequestDto<RoomDetailOut> response = new MessageRequestDto();
//            response.setRequestId(messageRequestDto.getRequestId());
//            response.setCode(messageRequestDto.getCode() + 200);
//            response.setMessage("success");
//            response.setData(roomDetailOut);
//            this.send(JSON.toJSONString(response));
//        }
//        if (messageRequestDto.getCode() == 4) {
//            //updateRoomVentilation
//            MessageRequestDto<UpdateRoomVentilationRequestDto> requestDto = JSON.parseObject(message, new TypeReference<MessageRequestDto<UpdateRoomVentilationRequestDto>>() {
//            });
//            UpdateRoomVentilationRequestDto updateRoomVentilationRequestDto = requestDto.getData();
//            if (updateRoomVentilationRequestDto.getSetTemp() != null) {
//                int temp = (int) (updateRoomVentilationRequestDto.getSetTemp() * 10);
//                System.out.println("start change temperature:" + temp);
//                Integer result = EnvCenter.setTemperatureByDeviceNumber(updateRoomVentilationRequestDto.getRoomID(), temp);
//                if (result != null) {
//                    MessageRequestDto response = new MessageRequestDto();
//                    response.setRequestId(messageRequestDto.getRequestId());
//                    response.setCode(204);
//                    response.setMessage("success");
//                    this.send(JSON.toJSONString(response));
//                }
//            }
//
//        }

    }

    @Override
    public void onClose(int a, String s, boolean b) {
        //重连
        log.info("由于:{},连接被关闭,开始尝试重新连接", s);
        ReconnectThreadEnum.getInstance().reconnectWs(this);
    }

    @Override
    public void onError(Exception e) {
        log.error("====websocket出现错误====" + e.getMessage());
    }


    /**
     * 心跳保活
     *
     * @param var1
     */
    private void heartbeat(LocalWebSocketClient var1) {
        Runnable runnable = () -> {
            if (var1 != null) {
                var1.send(HEARTBEAT_CMD);
            }
        };
        ThreadUtil.scheduleAtFixedRate(runnable, 0, 15, TimeUnit.SECONDS);
    }
}
