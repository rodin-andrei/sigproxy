package com.unifun.sigproxy.sigtran.service.rabbit.impl;

import com.unifun.sigproxy.sigtran.service.map.DialogService;
import com.unifun.sigproxy.sigtran.service.map.MapService;
import com.unifun.sigproxy.sigtran.service.rabbit.ReceiverService;
import com.unifun.sigproxy.sigtran.service.rabbit.pojo.MapSupplementaryMessageRabbit;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.restcomm.protocols.ss7.map.api.MAPDialog;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Component
@AllArgsConstructor
public class ReceiverServiceImpl implements ReceiverService {
    private final DialogService dialogService;
    private final MapService mapService;


    @Override
    @RabbitListener(queues = "receive.routing.by.gt.#")
    @Transactional
    public void handleMessage(MapSupplementaryMessageRabbit message) { //TODO send by type
        log.info("Message received via amqp : " + message.toString() + " \n sending message");
        MAPDialog dialog = this.mapService.createDialogProcessUnstructuredSSRequest(message);
        if(dialog!=null) {
            this.dialogService.send(this.mapService.createDialogProcessUnstructuredSSRequest(message));
        } else {
            throw new NullPointerException("Dialog can not be null"); //TODO beautify
        }
    }
}
