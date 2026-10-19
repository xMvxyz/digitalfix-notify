package com.digitalfix.digitalfix_notify.consumer;

import com.digitalfix.digitalfix_notify.config.RabbitMQConfig;
import com.digitalfix.digitalfix_notify.service.NotificationService;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class EmailConsumer {

    private final NotificationService service;
    private final MessageProcessor processor;

    public EmailConsumer(NotificationService service, MessageProcessor processor) {
        this.service = service;
        this.processor = processor;
    }

    @RabbitListener(queues = RabbitMQConfig.EMAIL_QUEUE)
    public void consume(
            String message,
            Channel channel,
            @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag
    ) throws IOException {

        processor.process(
                RabbitMQConfig.EMAIL_QUEUE,
                message,
                channel,
                deliveryTag,
                service::processEmail
        );
    }
}
