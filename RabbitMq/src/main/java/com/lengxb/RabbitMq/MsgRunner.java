package com.lengxb.RabbitMq;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class MsgRunner implements CommandLineRunner {

    private final RabbitTemplate rabbitTemplate;

    public MsgRunner(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Sending message...");
        for (int i = 20000; i < 20100; i++) {
        	new MsgProducer(rabbitTemplate).sendMsg("This is my test "+i);
        	rabbitTemplate.convertAndSend(RabbitConfig.topicExchangeName, "foo.bar.baz", "This is Hello "+i);
		}
    }

}