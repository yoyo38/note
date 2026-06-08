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
@TableName("iot_ff_location")
public class Location {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    @TableField("dept_id")
    private Integer deptId;
    @TableField("client_id")
    private String clientId;
    @TableField("device_number")
    private Integer deviceNumber;
    @TableField("pigsty_id")
    private Integer pigstyId;
    @TableField("unit_id")
    private Integer unitId;
    @TableField("pigsty_name")
    private Integer pigstyName;
    @TableField("unit_name")
    private Integer unitName;
    @TableField(value = "modify_date")
    private LocalDateTime modifyDate;
}
