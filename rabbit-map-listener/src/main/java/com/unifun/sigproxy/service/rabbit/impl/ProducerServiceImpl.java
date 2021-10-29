package com.unifun.sigproxy.sigtran.service.rabbit.impl;

import com.unifun.sigproxy.sigtran.service.rabbit.ProducerService;
import com.unifun.sigproxy.sigtran.service.rabbit.pojo.MapSupplementaryMessageRabbit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProducerServiceImpl implements ProducerService {
    private  RabbitConfiguration rabbitConfiguration;
    private  RabbitTemplate rabbitTemplate;
    private  Queue sendToRouting;

    @Override
    public void send(MapSupplementaryMessageRabbit message) {
        this.rabbitTemplate.convertAndSend(this.sendToRouting.getActualName(), this.rabbitConfiguration.getSendByGtQueue());
        //this.rabbitTemplate.convertSendAndReceive();
    }
}
