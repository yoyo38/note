package run.tecn.jdknf.test;

import com.sun.org.apache.xerces.internal.dom.PSVIDocumentImpl;
import org.springframework.web.client.RestTemplate;
import run.tecn.jdknf.model.Apple;
import run.tecn.jdknf.model.Food;
import run.tecn.jdknf.model.Fruit;
import run.tecn.jdknf.model.Pear;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author lujianhua
 * @version 1.0
 * @company holdmoral
 * @date 2020/10/19 19:15
 */
public class Test {
    public static void main(String[] args) {
//        RestTemplate restTemplate= new RestTemplate();
//        String object = restTemplate.getForObject("http://localhost:8080".replace("8080", "8081") + "/dict/init?groupId={1}", String.class, 8848);
//        Timestamp timestamp= Timestamp.valueOf(LocalDateTime.now());
//        long now = timestamp.getTime();
//        System.out.println(timestamp.getTime());
//        try{
//            Thread.sleep(1000);
//        }catch (Exception e){
        LocalDate today = LocalDate.now();
        LocalDate date = LocalDate.of(2021,3,28);
        long days = ChronoUnit.DAYS.between(today, date);
        System.out.println(days);
        ScheduledExecutorService service = Executors.newScheduledThreadPool(2);
        Worker worker = new Worker(18,"rose");
        for(int i=0;i<10;i++){
            service.schedule(worker,3, TimeUnit.SECONDS);
        }

        Queue<String> queue;

        System.out.println("done");
//        }
//        System.out.println(Timestamp.valueOf(LocalDateTime.now()).getTime());

//        Apple apple = new Apple();
//        Fruit fruit = new Fruit();
//        System.out.println(Apple.class.isAssignableFrom(fruit.getClass()));
    }
}
