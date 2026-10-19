package com.digitalfix.digitalfix_notify.consumer;

import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.function.Consumer;

@Component
public class MessageProcessor {

    private static final Logger log = LoggerFactory.getLogger(MessageProcessor.class);

    private static final int MAX_ATTEMPTS = 3;
    private static final long[] BACKOFF_MS = {1000, 2000};

    public void process(
            String queue,
            String message,
            Channel channel,
            long deliveryTag,
            Consumer<String> operation
    ) throws IOException {

        for (int attempt = 1; attempt <= MAX_ATTEMPTS; attempt++) {
            try {
                operation.accept(message);
            } catch (Exception ex) {
                log.error(
                        "Error en cola={}, intento={}/{}: {}",
                        queue,
                        attempt,
                        MAX_ATTEMPTS,
                        ex.getMessage()
                );
                
                if (attempt == MAX_ATTEMPTS) {
                    // Rechazo definitivo: no reencolar.
                    // RabbitMQ lo enviara a DLQ si la cola
                    // tiene configurado su dead-letter exchange.
                    channel.basicNack(
                            deliveryTag,
                            false,
                            false
                    );

                    log.warn(
                            "NACK definitivo. Cola={}",
                            queue
                    );
                    return;
                }

                try {
                    Thread.sleep(BACKOFF_MS[attempt - 1]);
                } catch (InterruptedException interrupted) {
                    Thread.currentThread().interrupt();

                    channel.basicNack(
                            deliveryTag,
                            false,
                            false
                    );
                    return;
                }

                continue;
            }

            //Solo ejecuta si el procedimiento fue exitoso
            channel.basicAck(deliveryTag, false);

            log.info("ACK enviado. Cola={}, intento={}", queue, attempt);
            return;
        }
    }

}
