package cn.holdmoral.forever;

import cn.holdmoral.forever.util.ThreadUtil;
import io.netty.channel.ChannelFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author lujianhua
 * @version 1.0
 * @company holdmoral
 * @date 2022/5/27 13:49
 */
@SpringBootApplication
public class ForeverApplication implements CommandLineRunner {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(ForeverApplication.class, args);
    }

    @Autowired
    NettyTcpServer nettyTcpServer;


    /**
     * Callback used to run the bean.
     *
     * @param args incoming main method arguments
     * @throws Exception on error
     */
    @Override
    public void run(String... args) throws Exception {
        ThreadUtil.execute(()->{
            ChannelFuture start = nettyTcpServer.start();
            start.channel().closeFuture().syncUninterruptibly();
        },1);
        System.out.println("---------------------- start success ----------------------");
    }
}
