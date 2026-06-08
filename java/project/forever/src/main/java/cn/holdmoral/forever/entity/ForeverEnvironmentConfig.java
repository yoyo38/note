package cn.holdmoral.forever.entity;

import cn.holdmoral.forever.model.FixedEnvGrowNode;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author lujianhua
 * @version 1.0
 * @company holdmoral
 * @date 2022/6/1 17:19
 */
@Data
@TableName(value = "iot_ff_env_config",autoResultMap = true)
public class ForeverEnvironmentConfig {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    @TableField("device_number")
    private Integer deviceNumber;
    @TableField("client_id")
    private String clientId;
    @TableField("location_id")
    private Integer locationId;
    @TableField("light_mode")
    private Integer lightMode;
    @TableField("light_time1")
    private String lightTime1;
    @TableField("light_time2")
    private String lightTime2;
    @TableField("light_time3")
    private String lightTime3;
    @TableField("wet_window_mode")
    private Integer wetWindowMode;
    @TableField("wet_window_temp")
    private Float wetWindowTemp;
    @TableField("wet_window_humidity")
    private Integer wetWindowHumidity;
    @TableField("wet_window_run")
    private Integer wetWindowRun;
    @TableField("wet_window_stop")
    private Integer wetWindowStop;
    @TableField("shower_mode")
    private Integer showerMode;
    @TableField("shower_temp")
    private Float showerTemp;
    @TableField("shower_humidity")
    private Integer showerHumidity;
    @TableField("shower_run")
    private Integer showerRun;
    @TableField("shower_stop")
    private Integer showerStop;
    @TableField("heat_mode")
    private Integer heatMode;
    @TableField("heat_temp")
    private Float heatTemp;
    @TableField("heat_run")
    private Integer heatRun;
    @TableField("heat_stop")
    private Integer heatStop;
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @TableField("grow_birthday")
    private LocalDate growBirthday;
    @TableField("grow_weight")
    private Float growWeight;
    @TableField("grow_node_quantity")
    private Integer growNodeQuantity;
    @TableField("grow_animal_quantity")
    private Integer growAnimalQuantity;
    @TableField("grow_enable")
    private Integer growEnable;
    @TableField(typeHandler = FastjsonTypeHandler.class)
    private List<FixedEnvGrowNode> nodes;
    @TableField(value = "modify_date")
    private LocalDateTime modifyDate;
}
