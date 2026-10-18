package com.digitalfix.digitalfix_notify.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service 
public class NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    public void processEmail(String message) {
        validate(message);
        log.info("Procesando notificacion: {}", message);

        // TODO: Integrar envio real de correo o push
    }

    public void processDispatch(String message) {
        validate(message);
        log.info("Procesando despacho: {}", message);

        // TODO: Generar o notificar ticket de tecnico
    }

    public void processReport(String message) {
        validate(message);
        log.info("Procesando reporte: {}", message);

        // TODO: Integrar generacion real de PDF
    }

    private void validate(String message) {
        if (message == null || message.isBlank()) {
            throw new IllegalArgumentException(
                    "El mensaje no puede estar vacio"
            );
        }

        // Error controlado para probar reintentos y DLQ.
        // Solo para desarrollo; eliminar en produccion.
        if (message.contains("SIMULAR_ERROR")) {
            throw new IllegalStateException(
                    "Error simulado de procesamiento"
            );
        }
    }
}
