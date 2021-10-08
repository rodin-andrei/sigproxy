package com.unifun.sigproxy.service.rabbit;

import com.unifun.sigproxy.service.rabbit.pojo.MapSupplementaryMessageRabbit;

public interface ProducerService {
    void send(MapSupplementaryMessageRabbit message); //TODO return ack ? , add queue ?
}
