package com.taohansen.mod.empregados.services;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogProducer {
    private final RabbitTemplate rabbitTemplate;

    public void sendLogMessage(String message) {
        rabbitTemplate.convertAndSend("logQueue", message);
    }

    public void sendAuditMessage(String message) {
        rabbitTemplate.convertAndSend("auditQueue", message);
    }
}
