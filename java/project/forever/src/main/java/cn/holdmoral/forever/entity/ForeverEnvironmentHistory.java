package cn.holdmoral.forever.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author lujianhua
 * @version 1.0
 * @company holdmoral
 * @date 2022/6/1 17:19
 */
@Data
@TableName("iot_ff_env_history")
public class ForeverEnvironmentHistory {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    @TableField("device_number")
    private Integer deviceNumber;
    @TableField("client_id")
    private String clientId;
    @TableField("temperature")
    private Float temperature;
    @TableField("humidity")
    private Float humidity;
    @TableField("co2")
    private Float co2;
    @TableField("nh3")
    private Float nh3;
    @TableField("location_id")
    private Integer locationId;
    @TableField(value = "env_date")
    private LocalDateTime envDate;
}
