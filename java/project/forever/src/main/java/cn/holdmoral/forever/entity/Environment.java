package cn.holdmoral.forever.entity;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.*;

import java.time.LocalDateTime;

/**
 * @author lujianhua
 * @version 1.0
 * @company holdmoral
 * @date 2022/6/1 17:19
 */
@Data
@TableName("iot_ff_env")
public class Environment {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    @TableField("device_number")
    private Integer deviceNumber;
    @TableField("client_id")
    private String clientId;
    @TableField("contract")
    private Integer contract;
    @TableField("temperature")
    private Float temperature;
    @TableField("humidity")
    private Float humidity;
    @TableField("co2")
    private Float co2;
    @TableField("nh3")
    private Float nh3;
    @TableField("temperature_outside")
    private Float temperatureOutside;
    @TableField("humidity_outside")
    private Float humidityOutside;
    @TableField("nh3_outside")
    private Float nh3Outside;
    @TableField("temperature_set")
    private Float temperatureSet;
    @TableField("wind_status")
    private String windStatus;
    @TableField("ventilation")
    private Integer ventilation;
    @TableField("water_pump")
    private Integer waterPump;
    @TableField("curve_window")
    private Integer curveWindow;
    @TableField("small_window")
    private Integer smallWindow;
    @TableField("heat")
    private Integer heat;
    @TableField("frequency_conversion1")
    private Integer frequencyConversion1;
    @TableField("frequency_conversion2")
    private Integer frequencyConversion2;
    @TableField("status")
    private Integer status;
    @TableField("alarm_status")
    private Integer alarmStatus;
    @TableField("location_id")
    private Integer locationId;
    @TableField(value = "modify_date")
    private LocalDateTime modifyDate;
}
