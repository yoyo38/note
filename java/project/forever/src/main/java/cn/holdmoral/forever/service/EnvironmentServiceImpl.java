package cn.holdmoral.forever.service;

import cn.holdmoral.forever.EnvCenter;
import cn.holdmoral.forever.entity.Environment;
import cn.holdmoral.forever.entity.ForeverEnvironmentConfig;
import cn.holdmoral.forever.entity.ForeverEnvironmentHistory;
import cn.holdmoral.forever.entity.Location;
import cn.holdmoral.forever.mapper.EnvironmentMapper;
import cn.holdmoral.forever.mapper.ForeverEnvironmentHistoryMapper;
import cn.holdmoral.forever.mapper.LocationMapper;
import cn.holdmoral.forever.util.BeanUtils;
import cn.holdmoral.forever.util.ByteUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Service实现
 *
 * @author lujianhua
 * @date 2020-06-04 16:10:18
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class EnvironmentServiceImpl extends ServiceImpl<EnvironmentMapper, Environment> {
    @Autowired
    private EnvironmentConfigServiceImpl environmentConfigService;
    @Value("${client.id}")
    private String clientId;
    @Resource
    private LocationMapper locationMapper;
    @Resource
    private ForeverEnvironmentHistoryMapper environmentHistoryMapper;

    private Map<Integer, LocalDateTime> updateTimeMap = new HashMap<>();
    private Map<Integer, Integer> historyFlag = new HashMap<>();

    public void hexHandle(String remoteAddress, String hex) {
        if (!hex.contains("AA5555AA")) {
            return;
        }
        String content = hex.replace("AA5555AA", "");
        String hexContent = content.substring(4);
        int len = new BigInteger(content.substring(0, 2), 16).intValue();
        if (hexContent.length() != len * 2) {
            return;
        }
        String command = hexContent.substring(0, 4);
        System.out.println("command:" + command);
        if (command.equals("0180")) {
            //AA5555AA1A0001802CE15B190200F400870200004000180000000000000037F7
            String deviceNumber = hexContent.substring(4, 12);
            int number = ByteUtil.littleEndian2Int(ByteUtil.hexString2Bytes(deviceNumber));
            System.out.println("设备编号 ：" + number);
            EnvCenter.activeDeviceNumbers.add(number);
            EnvCenter.addressDeviceNumberMap.put(remoteAddress, number);
            EnvCenter.deviceNumberChannelMap.put(number, EnvCenter.channelMap.get(remoteAddress));
            LambdaQueryWrapper<Environment> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Environment::getClientId, clientId);
            queryWrapper.eq(Environment::getDeviceNumber, number);
            Environment environment = getOne(queryWrapper);
            if (environment == null) {
                environment = new Environment();
                environment.setClientId(clientId);
                environment.setDeviceNumber(number);
                Location location = locationMapper.selectOne(Wrappers.<Location>query().eq("client_id", clientId).eq("device_number", number));
                if (location != null) {
                    environment.setLocationId(location.getId());
                }
            }
            String contract = hexContent.substring(12, 16);
            int contractNumber = ByteUtil.littleEndian2Short(ByteUtil.hexString2Bytes(contract));
            environment.setContract(contractNumber);
            System.out.println("协议版本 ：" + contractNumber);
            if (contractNumber < 3) {
                String temperatureString = hexContent.substring(16, 20);
                int temperature = ByteUtil.localHexString2Int(temperatureString);
                System.out.println("温度：" + temperature);
                environment.setTemperature((float) temperature / 10);
                String dioxideHex = hexContent.substring(20, 24);
                int dioxide = ByteUtil.localHexString2Int(dioxideHex);
                environment.setCo2((float) dioxide);
                System.out.println("二氧化碳：" + dioxide);
                String nh3Hex = hexContent.substring(24, 28);
                int nh3 = ByteUtil.localHexString2Int(nh3Hex);
                environment.setNh3((float) nh3);
                System.out.println("氨气：" + nh3);
                String humidityHex = hexContent.substring(28, 32);
                int humidity = ByteUtil.localHexString2Int(humidityHex);
                environment.setHumidity((float) humidity);
                System.out.println("湿度：" + humidity);
                String temperatureOutSideString = hexContent.substring(32, 36);
                int temperatureOutside = ByteUtil.localHexString2Int(temperatureOutSideString);
                environment.setTemperatureOutside((float) temperatureOutside / 10);
                System.out.println("室外温度：" + temperatureOutside);
                String humidityOutsideHex = hexContent.substring(36, 40);
                int humidityOutside = ByteUtil.localHexString2Int(humidityOutsideHex);
                environment.setHumidityOutside((float) humidityOutside);
                System.out.println("室外湿度：" + humidityOutside);
            } else if (contractNumber == 3) {
                String nh3Hex = hexContent.substring(16, 20);
                int nh3 = ByteUtil.localHexString2Int(nh3Hex);
                environment.setNh3((float) nh3);
                System.out.println("氨气：" + nh3);
                String nh3OutsideHex = hexContent.substring(20, 24);
                int nh3Outside = ByteUtil.localHexString2Int(nh3OutsideHex);
                environment.setNh3Outside((float) nh3Outside);
                System.out.println("室外氨气：" + nh3Outside);
            }
            if (contractNumber == 1) {
                LocalDateTime updateTime = updateTimeMap.get(number);
                if (updateTime != null) {
                    long seconds = ChronoUnit.SECONDS.between(updateTime, LocalDateTime.now());
                    if (seconds > 90) {
                        EnvCenter.getWindByDeviceNumber(number);
                        updateTimeMap.put(number, LocalDateTime.now());
                    }
                } else {
                    updateTimeMap.put(number, LocalDateTime.now());
                }
            }
            environment.setModifyDate(LocalDateTime.now());
            environment.setStatus(1);
            if (environment.getId() == null)
                save(environment);
            else {
                updateById(environment);
            }
            if (LocalDateTime.now().getMinute() % 5 == 0) {
                Integer minutes = historyFlag.get(number);
                if (minutes == null || LocalDateTime.now().getMinute() != minutes) {
                    ForeverEnvironmentHistory foreverEnvironmentHistory = BeanUtils.transformFrom(environment, ForeverEnvironmentHistory.class);
                    foreverEnvironmentHistory.setId(null);
                    foreverEnvironmentHistory.setEnvDate(LocalDateTime.now());
                    environmentHistoryMapper.insert(foreverEnvironmentHistory);
                    historyFlag.put(number, LocalDateTime.now().getMinute());
                }
            }
        }
        if (command.equals("0280")) {
            //028016010000000191910000000EDC0000000004020000003446
            System.out.println("receive wind " + hex);
            String deviceNumber = hexContent.substring(4, 12);
            int number = ByteUtil.littleEndian2Int(ByteUtil.hexString2Bytes(deviceNumber));
            LambdaQueryWrapper<Environment> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Environment::getClientId, clientId);
            queryWrapper.eq(Environment::getDeviceNumber, number);
            Environment environment = getOne(queryWrapper);
            System.out.println("设备编号 ：" + number);
            String windStatus = hexContent.substring(16, 20);
            System.out.println("风机状态 ：" + windStatus);
            String newWindStatus = windStatus.substring(2, 4) + windStatus.substring(0, 2);
            System.out.println("新风机状态 ：" + newWindStatus);
            //System.out.println("风机状态二进制 ：" + ByteUtil.hex2BinaryString(newWindStatus));
            if (environment != null) {
                environment.setWindStatus(ByteUtil.hex2BinaryString(newWindStatus));
                String waterStatus = hexContent.substring(20, 22);
                System.out.println("水帘水泵状态 ：" + waterStatus);
                environment.setWaterPump(ByteUtil.hex16To10(waterStatus));
                String curveOpen = hexContent.substring(22, 24);
                System.out.println("卷帘开度 ：" + curveOpen);
                environment.setCurveWindow(ByteUtil.hex16To10(curveOpen));
                String smallWindowOpen = hexContent.substring(24, 26);
                environment.setSmallWindow(ByteUtil.hex16To10(smallWindowOpen));
                environment.setFrequencyConversion1(ByteUtil.hex16To10(hexContent.substring(26, 28)));
                environment.setFrequencyConversion2(ByteUtil.hex16To10(hexContent.substring(28, 30)));
                environment.setHeat(ByteUtil.hex16To10(hexContent.substring(30, 32)));
                environment.setVentilation(ByteUtil.hex16To10(hexContent.substring(32, 34)));
                String temperatureString = hexContent.substring(34, 38);
                int temperature = ByteUtil.localHexString2Int(temperatureString);
                environment.setTemperatureSet((float) temperature / 10);
                //environment.setModifyDate(LocalDateTime.now());
                updateById(environment);
            }
            EnvCenter.getLightByDeviceNumber(number);
        }
        if (command.equals("0380")) {
            System.out.println("receive set temperature " + hex);
            hexContent = hexContent.substring(0, 32);
            String deviceNumber = hexContent.substring(2, 4);
            int number = new BigInteger(deviceNumber, 16).intValue();
        }
        if (command.equals("0480")) {
            System.out.println("receive light data " + hex);
            String deviceNumber = hexContent.substring(4, 12);
            int number = ByteUtil.littleEndian2Int(ByteUtil.hexString2Bytes(deviceNumber));
            System.out.println("设备编号 ：" + number);
            LambdaQueryWrapper<ForeverEnvironmentConfig> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(ForeverEnvironmentConfig::getClientId, clientId);
            queryWrapper.eq(ForeverEnvironmentConfig::getDeviceNumber, number);
            ForeverEnvironmentConfig config = environmentConfigService.getOne(queryWrapper);
            if (config == null) {
                config = new ForeverEnvironmentConfig();
                config.setClientId(clientId);
                config.setDeviceNumber(number);
            }
            String mode = hexContent.substring(16, 18);
            config.setLightMode(new BigInteger(mode, 16).intValue());
            StringBuilder sb = new StringBuilder();
            int hour1 = new BigInteger(hexContent.substring(18, 20), 16).intValue();
            int minute1 = new BigInteger(hexContent.substring(20, 22), 16).intValue();
            LocalTime time1 = LocalTime.of(hour1, minute1, 0);
            int hour2 = new BigInteger(hexContent.substring(22, 24), 16).intValue();
            int minute2 = new BigInteger(hexContent.substring(24, 26), 16).intValue();
            LocalTime time2 = LocalTime.of(hour2, minute2, 0);
            sb.append(time1.toString());
            sb.append("-");
            sb.append(time2.toString());
            config.setLightTime1(sb.toString());
            int hour3 = new BigInteger(hexContent.substring(26, 28), 16).intValue();
            int minute3 = new BigInteger(hexContent.substring(28, 30), 16).intValue();
            LocalTime time3 = LocalTime.of(hour3, minute3, 0);
            int hour4 = new BigInteger(hexContent.substring(30, 32), 16).intValue();
            int minute4 = new BigInteger(hexContent.substring(32, 34), 16).intValue();
            LocalTime time4 = LocalTime.of(hour4, minute4, 0);
            StringBuilder sb2 = new StringBuilder();
            sb2.append(time3.toString());
            sb2.append("-");
            sb2.append(time4.toString());
            config.setLightTime2(sb2.toString());
            int hour5 = new BigInteger(hexContent.substring(34, 36), 16).intValue();
            int minute5 = new BigInteger(hexContent.substring(36, 38), 16).intValue();
            LocalTime time5 = LocalTime.of(hour5, minute5, 0);
            int hour6 = new BigInteger(hexContent.substring(38, 40), 16).intValue();
            int minute6 = new BigInteger(hexContent.substring(40, 42), 16).intValue();
            LocalTime time6 = LocalTime.of(hour6, minute6, 0);
            StringBuilder sb3 = new StringBuilder();
            sb3.append(time5.toString());
            sb3.append("-");
            sb3.append(time6.toString());
            config.setLightTime3(sb3.toString());
            if (config.getId() == null)
                environmentConfigService.save(config);
            else {
                environmentConfigService.updateById(config);
            }
            EnvCenter.getWetWindowByDeviceNumber(number);
        }
        if (command.equals("0580")) {
            System.out.println("receive wet window data " + hex);
            String deviceNumber = hexContent.substring(4, 12);
            int number = ByteUtil.littleEndian2Int(ByteUtil.hexString2Bytes(deviceNumber));
            System.out.println("设备编号 ：" + number);
            LambdaQueryWrapper<ForeverEnvironmentConfig> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(ForeverEnvironmentConfig::getClientId, clientId);
            queryWrapper.eq(ForeverEnvironmentConfig::getDeviceNumber, number);
            ForeverEnvironmentConfig config = environmentConfigService.getOne(queryWrapper);
            if (config == null) {
                config = new ForeverEnvironmentConfig();
                config.setClientId(clientId);
                config.setDeviceNumber(number);
            }
            String openTemp = hexContent.substring(16, 20);
            int temperature = ByteUtil.localHexString2Int(openTemp);
            config.setWetWindowTemp((float) temperature / 10);
            config.setWetWindowMode(new BigInteger(hexContent.substring(20, 22), 16).intValue());
            config.setWetWindowRun(ByteUtil.localHexString2Int(hexContent.substring(22, 26)));
            config.setWetWindowStop(ByteUtil.localHexString2Int(hexContent.substring(26, 30)));
            config.setWetWindowHumidity(new BigInteger(hexContent.substring(30, 32), 16).intValue());
            String showTemp = hexContent.substring(32, 36);
            int showTemperature = ByteUtil.localHexString2Int(showTemp);
            config.setShowerTemp((float) showTemperature / 10);
            config.setShowerMode(new BigInteger(hexContent.substring(36, 38), 16).intValue());
            config.setShowerRun(ByteUtil.localHexString2Int(hexContent.substring(38, 42)));
            config.setShowerStop(ByteUtil.localHexString2Int(hexContent.substring(42, 46)));
            config.setShowerHumidity(new BigInteger(hexContent.substring(46, 48), 16).intValue());
            String heatTemp = hexContent.substring(48, 52);
            int heatTemperature = ByteUtil.localHexString2Int(heatTemp);
            config.setHeatTemp((float) heatTemperature / 10);
            config.setHeatMode(new BigInteger(hexContent.substring(52, 54), 16).intValue());
            config.setHeatRun(ByteUtil.localHexString2Int(hexContent.substring(54, 58)));
            config.setHeatStop(ByteUtil.localHexString2Int(hexContent.substring(58, 62)));
            if (config.getId() == null)
                environmentConfigService.save(config);
            else {
                environmentConfigService.updateById(config);
            }
            EnvCenter.getGrowInfoByDeviceNumber(number);
        }
        if (command.equals("0680")) {
            System.out.println("receive grow curve data");
            String deviceNumber = hexContent.substring(4, 12);
            int number = ByteUtil.littleEndian2Int(ByteUtil.hexString2Bytes(deviceNumber));
            System.out.println("设备编号 ：" + number);
            LambdaQueryWrapper<ForeverEnvironmentConfig> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(ForeverEnvironmentConfig::getClientId, clientId);
            queryWrapper.eq(ForeverEnvironmentConfig::getDeviceNumber, number);
            ForeverEnvironmentConfig config = environmentConfigService.getOne(queryWrapper);
            if (config == null) {
                config = new ForeverEnvironmentConfig();
                config.setClientId(clientId);
                config.setDeviceNumber(number);
            }
            int weight = ByteUtil.localHexString2Int(hexContent.substring(16, 20));
            config.setGrowWeight((float) weight / 10);
            int year = ByteUtil.localHexString2Int(hexContent.substring(20, 24));
            int month = new BigInteger(hexContent.substring(24, 26), 16).intValue();
            int day = new BigInteger(hexContent.substring(26, 28), 16).intValue();
            config.setGrowBirthday(LocalDate.of(year, month, day));
            config.setGrowNodeQuantity(new BigInteger(hexContent.substring(28, 30), 16).intValue());
            config.setGrowAnimalQuantity(ByteUtil.localHexString2Int(hexContent.substring(30, 34)));
            config.setGrowEnable(new BigInteger(hexContent.substring(34, 36), 16).intValue());
            if (config.getId() == null)
                environmentConfigService.save(config);
            else {
                config.setModifyDate(LocalDateTime.now());
                environmentConfigService.updateById(config);
            }
        }
        if (command.equals("0780")) {

        }
        if (command.equals("03C0")) {
            //AA5555AA0E0003C0170100010000000000009AD0
            System.out.println("receive set temperature result :" + hex);
            //03C0170100010000000000009AD0
            String deviceNumber = hexContent.substring(4, 12);
            int number = ByteUtil.littleEndian2Int(ByteUtil.hexString2Bytes(deviceNumber));
            System.out.println("设备编号 ：" + number);
            String windStatus = hexContent.substring(12, 16);
            System.out.println("协议版本 ：" + windStatus);
            String waterStatus = hexContent.substring(16, 20);
            System.out.println("返回结果 ：" + waterStatus);
        }
        if (command.equals("04C0")) {
            System.out.println("receive set light result :" + hex);
            String deviceNumber = hexContent.substring(4, 12);
            int number = ByteUtil.littleEndian2Int(ByteUtil.hexString2Bytes(deviceNumber));
            System.out.println("设备编号 ：" + number);
            String windStatus = hexContent.substring(12, 16);
            System.out.println("协议版本 ：" + windStatus);
            String waterStatus = hexContent.substring(16, 20);
            System.out.println("返回结果 ：" + waterStatus);
        }
    }

    public void onlineOrOffline(String clientId, Integer deviceNumber, int online) {
        LambdaUpdateWrapper<Environment> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Environment::getClientId, clientId);
        updateWrapper.eq(Environment::getDeviceNumber, deviceNumber);
        updateWrapper.set(Environment::getStatus, online);
        update(updateWrapper);
    }
}
