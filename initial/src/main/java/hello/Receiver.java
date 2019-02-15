package hello;

import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

/**
 * @Auther: wh
 * @date 2019/2/15 9:29
 * @Description: 这是一个RabbitMQ消息接收器 其中定义了接受消息的方法。POJO
 * 其中还定义了一个CountDownLatch ,生产环境不太可能会用到它。
 */
@Component
public class Receiver {

    private CountDownLatch latch = new CountDownLatch(1);

    public void receiveMessage(String message){
        System.out.println("Received <"+message+">");
        latch.countDown();
    }

    public CountDownLatch getLatch(){
        return latch;
    }
}
