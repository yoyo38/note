package run.tecn.jdknf.DateAndTime;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class Main {
    public static void main(String[] args) {


        Integer a=23;
        Integer b=a/26;
        Integer c=a%26;
        System.out.println(b);
        System.out.println(c);

        // 获取日期
        LocalDate localDate = LocalDate.now();
        System.out.println(localDate);    // output:2019-08-16
        //获取两个日期的相关天数
        LocalDate before = LocalDate.of(2017, 9, 22);
        LocalDate now = LocalDate.now(ZoneId.systemDefault());
        long betweenDays = before.until(now, ChronoUnit.DAYS);
        System.out.println("相关天数："+betweenDays);
        // 获取时间
        LocalTime localTime = LocalTime.now();
        System.out.println(localTime);    // output:21:09:13.708
        // 获取日期和时间
        LocalDateTime localDateTime = LocalDateTime.now();
        System.out.println(localDateTime);    // output:2019-08-16T21:09:13.708

        /*
        获取时间戳
         */
        long milli = Instant.now().toEpochMilli(); // 获取当前时间戳（精确到毫秒）
        long second = Instant.now().getEpochSecond(); // 获取当前时间戳（精确到秒）
        System.out.println(milli);  // output:1565932435792
        System.out.println(second); // output:1565932435

        // 时间格式化①
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String timeFormat = dateTimeFormatter.format(LocalDateTime.now());
        System.out.println(timeFormat);  // output:2019-08-16 21:15:43
        // 时间格式化②
        String timeFormat2 = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        System.out.println(timeFormat2);    // output:2019-08-16 21:17:48

        //时间转换
        String timeStr = "2019-10-10 06:06:06";
        LocalDateTime dateTime = LocalDateTime.parse(timeStr,DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        System.out.println(dateTime);

        /*
        获得昨天此刻时间
         */
        LocalDateTime today = LocalDateTime.now();
        LocalDateTime yesterday = today.plusDays(-1);
        System.out.println(yesterday);

        /*
        Clock ,Clock 类提供了访问当前日期和时间的方法，Clock 是时区敏感的，可以用来取代 System.currentTimeMillis() 来获取当前的微秒数
         */
        Clock clock = Clock.systemDefaultZone();
        long millis = clock.millis();
        System.out.println(millis);//1552379579043
        Instant instant = clock.instant();
        System.out.println(instant); //2020-02-25T15:44:51.949Z
        Date legacyDate = Date.from(instant);
        System.out.println(legacyDate);//Tue Mar 12 16:32:59 CST 2019

        /*
        Timezones(时区)
         */
        //输出所有区域标识符
        System.out.println(ZoneId.getAvailableZoneIds());

        ZoneId zone1 = ZoneId.of("Europe/Berlin");
        ZoneId zone2 = ZoneId.of("Brazil/East");
        System.out.println(zone1.getRules());// ZoneRules[currentStandardOffset=+01:00]
        System.out.println(zone2.getRules());// ZoneRules[currentStandardOffset=-03:00]

        LocalTime now1 = LocalTime.now(zone1);
        LocalTime now2 = LocalTime.now(zone2);
        System.out.println(now2.isBefore(now1));  // true

        long hoursBetween = ChronoUnit.HOURS.between(now1, now2);
        long minutesBetween = ChronoUnit.MINUTES.between(now1, now2);

        System.out.println(hoursBetween);       // -3
        System.out.println(minutesBetween);     // -239

        LocalDate toda = LocalDate.now();//获取现在的日期
        System.out.println("今天的日期: "+toda);//2019-03-12
        LocalDate tomorrow = toda.plus(1, ChronoUnit.DAYS);
        System.out.println("明天的日期: "+tomorrow);//2019-03-13
        LocalDate yesterda = tomorrow.minusDays(2);
        System.out.println("昨天的日期: "+yesterda);//2019-03-11
        LocalDate independenceDay = LocalDate.of(2019, Month.MARCH, 12);
        DayOfWeek dayOfWeek = independenceDay.getDayOfWeek();
        System.out.println("今天是周几:"+dayOfWeek);//TUESDAY

        String str1 = "2014==04==12 01时06分09秒";
        // 根据需要解析的日期、时间字符串定义解析所用的格式器
        DateTimeFormatter fomatter1 = DateTimeFormatter
                .ofPattern("yyyy==MM==dd HH时mm分ss秒");

        LocalDateTime dt1 = LocalDateTime.parse(str1, fomatter1);
        System.out.println(dt1); // 输出 2014-04-12T01:06:09

        String str2 = "2014$$$四月$$$13 20小时";
        DateTimeFormatter fomatter2 = DateTimeFormatter
                .ofPattern("yyy$$$MMM$$$dd HH小时");
        LocalDateTime dt2 = LocalDateTime.parse(str2, fomatter2);
        System.out.println(dt2); // 输出 2014-04-13T20:00

        LocalDateTime rightNow=LocalDateTime.now();
        String date=DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(rightNow);
        System.out.println(date);//2019-03-12T16:26:48.29
        DateTimeFormatter formatter=DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss");
        System.out.println(formatter.format(rightNow));//2019-03-12 16:26:48

        LocalDateTime sylvester = LocalDateTime.of(2014, Month.DECEMBER, 31, 23, 59, 59);

        DayOfWeek dayOfWeek1 = sylvester.getDayOfWeek();
        System.out.println(dayOfWeek1);      // WEDNESDAY

        Month month = sylvester.getMonth();
        System.out.println(month);          // DECEMBER

        long minuteOfDay = sylvester.getLong(ChronoField.MINUTE_OF_DAY);
        System.out.println(minuteOfDay);    // 1439

        //只要附加上时区信息，就可以将其转换为一个时间点Instant对象，Instant时间点对象可以很容易的转换为老式的java.util.Date
        Instant instant1 = sylvester
                .atZone(ZoneId.systemDefault())
                .toInstant();

        Date legacyDate1 = Date.from(instant1);
        System.out.println(legacyDate1);     // Wed Dec 31 23:59:59 CET 2014
    }
}
