package cn.holdmoral.forever.model;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * @author lujianhua
 * @version 1.0
 * @company holdmoral
 * @date 2021/8/26 14:26
 */
@Data
public class FixedEnvConfigDto {
    private Integer id;
    private Integer deviceNumber;
    private Float temperature;
    private String requestId;
    private Integer lightMode;
    private String lightTime1;
    private String lightTime2;
    private String lightTime3;
    private Integer wetWindowMode;
    private Float wetWindowTemp;
    private Integer wetWindowHumidity;
    private Integer wetWindowRun;
    private Integer wetWindowStop;
    private Integer showerMode;
    private Float showerTemp;
    private Integer showerHumidity;
    private Integer showerRun;
    private Integer showerStop;
    private Integer heatMode;
    private Float heatTemp;
    private Integer heatRun;
    private Integer heatStop;
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private LocalDate growBirthday;
    private Float growWeight;
    private Integer growNodeQuantity;
    private Integer growAnimalQuantity;
    private Integer growEnable;
    //private List<FixedEnvGrowNode> growNodeList;
    private String clientId;
    private Integer code;
}
