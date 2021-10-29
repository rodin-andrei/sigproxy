package com.unifun.sigproxy.sigtran.service.map.impl;

import com.unifun.sigproxy.sigtran.service.map.DialogService;
import com.unifun.sigproxy.sigtran.service.map.MapService;
import com.unifun.sigproxy.sigtran.service.rabbit.pojo.MapSupplementaryMessageRabbit;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.restcomm.protocols.ss7.map.api.MAPDialog;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class DialogServiceImpl implements DialogService {
    private final MapService mapService;

    @Override
    public void send(MAPDialog dialog) {
        try {
            log.debug("Sending map message : " + dialog.toString());
            dialog.send();
        } catch (Exception e){
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public void convertAndSend(MapSupplementaryMessageRabbit messageRabbit) {
        MAPDialog newDialog = this.mapService.createDialogProcessUnstructuredSSRequest(messageRabbit);
        log.debug("Sending map message : " + newDialog.toString());
        try {
            newDialog.send();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
