package com.unifun.sigproxy.sigtran.service.rabbit;

import com.unifun.sigproxy.sigtran.service.rabbit.pojo.MapSupplementaryMessageRabbit;

public interface ProducerService {
    void send(MapSupplementaryMessageRabbit message); //TODO return ack ? , add queue ?
}
