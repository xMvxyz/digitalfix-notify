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
public class DispatchConsumer {

    private final NotificationService service;
    private final MessageProcessor processor;

    public DispatchConsumer(NotificationService service, MessageProcessor processor) {
        this.service = service;
        this.processor = processor;
    }

    @RabbitListener(queues = RabbitMQConfig.DISPATCH_QUEUE)
    public void consume(
            String message,
            Channel channel,
            @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag
    ) throws IOException {

        processor.process(
                RabbitMQConfig.DISPATCH_QUEUE,
                message,
                channel,
                deliveryTag,
                service::processDispatch
        );
    }
}
