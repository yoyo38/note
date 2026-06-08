package cn.holdmoral.forever;

import cn.holdmoral.forever.model.FixedEnvConfigDto;
import cn.holdmoral.forever.util.ByteUtil;
import cn.holdmoral.forever.util.MaxSizeHashMap;
import cn.holdmoral.forever.util.ThreadUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author lujianhua
 * @version 1.0
 * @company holdmoral
 * @date 2022/1/14 17:48
 */
public class EnvCenter {
    public static final MaxSizeHashMap<Integer, Integer> responseSetTempMap = new MaxSizeHashMap<>(32);
    public static final MaxSizeHashMap<Integer, Float> cacheTempSetMap = new MaxSizeHashMap<>(128);
    public static final MaxSizeHashMap<String, Channel> channelMap = new MaxSizeHashMap<>(128);
    public static final MaxSizeHashMap<String, Queue<ByteBuf>> queueMap = new MaxSizeHashMap<>(128);
    public static final MaxSizeHashMap<Integer, Channel> deviceNumberChannelMap = new MaxSizeHashMap<>(256);
    public static final MaxSizeHashMap<String, Integer> addressDeviceNumberMap = new MaxSizeHashMap<>(256);
    public static Set<Integer> activeDeviceNumbers = new HashSet<>();

    //public static Queue<ByteBuf> queue = new LinkedList<>();

    public static void send(Queue<ByteBuf> queue, ByteBuf byteBuf) {
        queue.offer(byteBuf);
    }


    public static void initActiveDevice(Channel channel0) {
        //Integer result = EnvCenter.setTemperatureByDeviceNumber(2, 232);
        Queue<ByteBuf> queue = new LinkedList<>();
        String address = channel0.remoteAddress().toString();
        queueMap.put(address, queue);
        ThreadUtil.scheduleAtFixedRate(() -> {
            ByteBuf byteBuf = queue.poll();
            if (byteBuf != null) {
                System.out.println(byteBuf);
                channel0.writeAndFlush(byteBuf);
            }
        }, 0, 2, TimeUnit.SECONDS);
        ThreadUtil.execute(() -> {
            byte[] header = ByteUtil.hexString2Bytes("FF3333FF");
            for (int i = 1; i < 256; i++) {
                byte[] data = ByteUtil.mergeBytes(ByteUtil.hexString2Bytes("06"), ByteUtil.intToByteTransfer(i), ByteUtil.hexString2Bytes("0000"));
                byte[] crc = ByteUtil.hexString2Bytes(ByteUtil.getCRC2(data));
                byte length = (byte) (data.length + crc.length);
                byte[] dataLength = ByteUtil.intToByteLittle(length);
                byte[] message = ByteUtil.mergeBytes(header, dataLength, data, crc);
                send(queue, Unpooled.copiedBuffer(message));
            }
        });
        ThreadUtil.scheduleAtFixedRate(() -> {
//                int deviceNumberIndex = LocalDateTime.now().getMinute() % size;
//                List<Integer> deviceNumberList = new ArrayList<>(activeDeviceNumbers.keySet());
//                Integer deviceNumber = deviceNumberList.get(deviceNumberIndex);
//                byte[] header = ByteUtil.hexString2Bytes("FF3333FF");
//                byte[] data = ByteUtil.mergeBytes(ByteUtil.hexString2Bytes("06"), ByteUtil.intToByteTransfer(deviceNumber), ByteUtil.hexString2Bytes("0000"));
//                byte[] crc = ByteUtil.hexString2Bytes(ByteUtil.getCRC2(data));
//                byte length = (byte) (data.length + crc.length);
//                byte[] dataLength = ByteUtil.intToByteLittle(length);
//                byte[] message = ByteUtil.mergeBytes(header, dataLength, data, crc);
//                activeDeviceNumbers.put(deviceNumber, 0);
//                send(Unpooled.copiedBuffer(message));
            LocalDateTime localDateTime = LocalDateTime.now();
            if (localDateTime.getHour() == 1 && localDateTime.getMinute() == 1) {
                byte[] header = ByteUtil.hexString2Bytes("FF3333FF");
                for (int i = 1; i < 256; i++) {
                    byte[] data = ByteUtil.mergeBytes(ByteUtil.hexString2Bytes("06"), ByteUtil.intToByteTransfer(i), ByteUtil.hexString2Bytes("0000"));
                    byte[] crc = ByteUtil.hexString2Bytes(ByteUtil.getCRC2(data));
                    byte length = (byte) (data.length + crc.length);
                    byte[] dataLength = ByteUtil.intToByteLittle(length);
                    byte[] message = ByteUtil.mergeBytes(header, dataLength, data, crc);
                    send(queue, Unpooled.copiedBuffer(message));
                }
            }
        }, 5, 1, TimeUnit.MINUTES);
    }

    public static Integer setTemperatureByDeviceNumber(int deviceNumber, int temp) {
        Channel channel = deviceNumberChannelMap.get(deviceNumber);
        if (channel != null) {
            ThreadUtil.execute(() -> {
                byte[] message = ByteUtil.getForeverTemperatureSetBytes(deviceNumber,temp);
                //System.out.println("发送设定温度命令："+ByteUtil.bytes2HexString(message));
                System.out.println("发送设定温度命令：");
                channel.writeAndFlush(Unpooled.copiedBuffer(message));
            });

        }
        return 1;
    }

    public static Integer setLightByDeviceNumber(int deviceNumber, FixedEnvConfigDto dto) {
        Channel channel = deviceNumberChannelMap.get(deviceNumber);
        if (channel != null) {
            ThreadUtil.execute(() -> {
                byte[] message = ByteUtil.getLightSetBytes(deviceNumber,dto);
                System.out.println("发送设定照明命令：");
                channel.writeAndFlush(Unpooled.copiedBuffer(message));
            });

        }
        return 1;
    }
    public static Integer setWetWindowByDeviceNumber(int deviceNumber, FixedEnvConfigDto dto) {
        Channel channel = deviceNumberChannelMap.get(deviceNumber);
        if (channel != null) {
            ThreadUtil.execute(() -> {
                byte[] message = ByteUtil.getWetWindowSetBytes(deviceNumber,dto);
                System.out.println("发送设定湿帘命令：");
                channel.writeAndFlush(Unpooled.copiedBuffer(message));
            });

        }
        return 1;
    }

    public static void getWindByDeviceNumber(int deviceNumber) {
        Channel channel = deviceNumberChannelMap.get(deviceNumber);
        if (channel != null) {
            ThreadUtil.execute(() -> {
                byte[] message = ByteUtil.getForeverBytes("0002","0001",deviceNumber);
                System.out.println("发送获取风机命令：");
                channel.writeAndFlush(Unpooled.copiedBuffer(message));
            });
        }
    }
    public static void getLightByDeviceNumber(int deviceNumber) {
        Channel channel = deviceNumberChannelMap.get(deviceNumber);
        if (channel != null) {
            ThreadUtil.execute(() -> {
                byte[] message = ByteUtil.getForeverBytes("0004","0001",deviceNumber);
                System.out.println("发送获取照明命令：");
                channel.writeAndFlush(Unpooled.copiedBuffer(message));
            });
        }
    }
    public static void getWetWindowByDeviceNumber(int deviceNumber) {
        Channel channel = deviceNumberChannelMap.get(deviceNumber);
        if (channel != null) {
            ThreadUtil.execute(() -> {
                byte[] message = ByteUtil.getForeverBytes("0005","0001",deviceNumber);
                System.out.println("发送获取湿帘命令：");
                channel.writeAndFlush(Unpooled.copiedBuffer(message));
            });
        }
    }
    public static void getGrowInfoByDeviceNumber(int deviceNumber) {
        Channel channel = deviceNumberChannelMap.get(deviceNumber);
        if (channel != null) {
            ThreadUtil.execute(() -> {
                byte[] message = ByteUtil.getForeverBytes("0006","0001",deviceNumber);
                System.out.println("发送获取生长曲线命令：");
                channel.writeAndFlush(Unpooled.copiedBuffer(message));
            });
        }
    }

    public static void hexHandle(String remoteAddress, String hex) {
        if (!hex.contains("AA5555AA")) {
            return;
        }
        String content = hex.replace("AA5555AA", "");
        String hexContent = content.substring(4);
        String command = hexContent.substring(0, 4);
        System.out.println("command:" + command);
        if (command.equals("0180")) {
            String deviceNumber = hexContent.substring(4, 6);
            int number = new BigInteger(deviceNumber, 16).intValue();
            System.out.println("设备编号 ：" + number);
            String temperatureString = hexContent.substring(10, 14);
            int temperature = ByteUtil.localHexString2Int(temperatureString);
            System.out.println("温度：" + temperature);
            String dioxideHex = hexContent.substring(14, 18);
            int dioxide = ByteUtil.localHexString2Int(dioxideHex);
            System.out.println("二氧化碳：" + dioxide);
            String nh3Hex = hexContent.substring(18, 22);
            int nh3 = ByteUtil.localHexString2Int(nh3Hex);
            System.out.println("氨气：" + nh3);
            String humidityHex = hexContent.substring(22, 26);
            int humidity = ByteUtil.localHexString2Int(humidityHex);
            System.out.println("湿度：" + humidity);
        }
        if (command.equals("0280")) {
            //028016010000000191910000000EDC0000000004020000003446
            System.out.println("receive wind " + hex);
            String deviceNumber = hexContent.substring(4, 6);
            Integer number = new BigInteger(deviceNumber, 16).intValue();
            System.out.println("设备编号 ：" + number);
            String windStatus = hexContent.substring(10, 14);
            System.out.println("风机状态 ：" + windStatus);
            String waterStatus = hexContent.substring(14, 16);
            System.out.println("水帘水泵状态 ：" + waterStatus);
            String curveOpen = hexContent.substring(16, 18);
            System.out.println("卷帘开度 ：" + curveOpen);
        }
        if (command.equals("0380")) {
            System.out.println("receive set temperature " + hex);
            hexContent = hexContent.substring(0, 32);
            String deviceNumber = hexContent.substring(2, 4);
            int number = new BigInteger(deviceNumber, 16).intValue();
        }
        if (command.equals("0480")) {
            System.out.println("receive light data " + hex);
            hexContent = hexContent.substring(0, 64);
            String deviceNumber = hexContent.substring(2, 4);
            int number = new BigInteger(deviceNumber, 16).intValue();
        }
        if (command.equals("0580")) {
            System.out.println("receive wet window data " + hex);
        }
        if (command.equals("0680")) {
            System.out.println("receive grow curve " + hex);
            hexContent = hexContent.substring(0, 32);
        }
        if (command.equals("0780")) {
            //水表，电表，料塔数据
            System.out.println("receive alarm data :" + hex);
            hexContent = hexContent.substring(0, 32);
        }
    }


}
