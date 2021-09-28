package com.unifun.sigproxy.service.map;

import com.unifun.sigproxy.service.rabbit.pojo.MapSupplementaryMessageRabbit;
import org.restcomm.protocols.ss7.map.api.MAPDialog;

public interface DialogService {
    void send(MAPDialog dialog);
    void convertAndSend(MapSupplementaryMessageRabbit messageRabbit); //TODO boolean or smth ?
}
