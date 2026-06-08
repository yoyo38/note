package run.tecn.jdknf.test;

/**
 * @author lujianhua
 * @version 1.0
 * @company holdmoral
 * @date 2021/2/2 14:06
 */
public class Worker implements Runnable {
    private Integer age;
    private String name;
    public Worker(Integer age,String name){
        this.age = age;
        this.name = name;
    }
    @Override
    public void run(){
        System.out.println("hello "+ name + "age: "+ age);
    }

}
