package com.unifun.sigproxy.service.rabbit;

import com.unifun.sigproxy.service.rabbit.pojo.MapSupplementaryMessageRabbit;

public interface ReceiverService {
    //TODO check if it's ok to use annotation for listen message in interface
    void handleMessage(MapSupplementaryMessageRabbit message);
}
