package com.unifun.sigproxy.service.rabbit.impl;

import com.unifun.sigproxy.service.rabbit.ProducerService;
import com.unifun.sigproxy.service.rabbit.pojo.MapSupplementaryMessageRabbit;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class ProducerServiceImpl implements ProducerService {
    private final RabbitConfiguration rabbitConfiguration;
    private final RabbitTemplate rabbitTemplate;
    private final Queue sendToRouting;

    @Override
    public void send(MapSupplementaryMessageRabbit message) {
        this.rabbitTemplate.convertAndSend(this.sendToRouting.getActualName(), this.rabbitConfiguration.getSendByGtQueue());
        //this.rabbitTemplate.convertSendAndReceive();
    }
}
