package hello;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * @Auther: wh
 * @date 2019/2/15 9:35
 * @Description: 
 */
@SpringBootApplication
public class Application {

    static final String topicExchangeName = "spring-boot-exchange";

    static final String queueName = "spring-boot";

    /**
     * 声明队列
     */
    @Bean
    Queue queue(){
        return new Queue(queueName,false);
    }

    /**
     * 声明交换器
     * @return
     */
    @Bean
    TopicExchange exchange(){
        return new TopicExchange(topicExchangeName);
    }

    /**
     * 声明队列和交换器之间的绑定
     * @param queue
     * @param topicExchange
     * @return
     */
    @Bean
    Binding binding(Queue queue,TopicExchange topicExchange){
        return BindingBuilder.bind(queue).to(exchange()).with("foo.bar.#");
    }

    /**
     * 配置一个消息侦听器容器
     * @param connectionFactory
     * @param listenerAdapter
     * @return
     */
    @Bean
    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapter){
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(queueName);
        container.setMessageListener(listenerAdapter);
        return container;
    }

    /**
     * 这个bean在container中注册为消息侦听器。
     *它将侦听"spring-boot"队列上的消息。
     * 因为Receiver类是POJO，所以需要被包装在MessageListenerAdapter中，在MessageListenerAdapter中指定调用receiveMessage。
     * @param receiver
     * @return
     */
    @Bean
    MessageListenerAdapter listenerAdapter(Receiver receiver){
        return new MessageListenerAdapter(receiver,"receiveMessage");
    }

    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(Application.class,args).close();
    }
}
