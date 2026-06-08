package run.tecn.jdknf.test;

/**
 * @author lujianhua
 * @version 1.0
 * @company holdmoral
 * @date 2021/2/26 13:39
 */
public class ThreadTest {
    public static void main(String[] args) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                for(;;){}
            }
        });
        //thread.setDaemon(true);
        thread.start();
        System.out.println("main thread is over");
    }
}
