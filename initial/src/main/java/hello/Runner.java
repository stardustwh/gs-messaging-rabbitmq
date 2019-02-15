package hello;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @Auther: wh
 * @date 2019/2/15 9:50
 * @Description: 发送测试消息，测试消息是由CommandLineRunner发送的，它还等待接收器中的锁存器并关闭应用程序上下文。
 */
@Component
public class Runner implements CommandLineRunner {

    private final RabbitTemplate rabbitTemplate;

    private final Receiver receiver;

    public Runner(Receiver receiver,RabbitTemplate rabbitTemplate){
        this.receiver = receiver;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Sending message...");
        rabbitTemplate.convertAndSend(Application.topicExchangeName,"foo.bar.baz","Hello from  RabbitMQ!");
        receiver.getLatch().await(10000,TimeUnit.MILLISECONDS);
    }


}
