package com.unifun.sigproxy.controller.management;

import com.unifun.sigproxy.dto.M3uaMessageDto;
import com.unifun.sigproxy.service.impl.M3uaServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.restcomm.protocols.ss7.mtp.Mtp3TransferPrimitive;
import org.restcomm.protocols.ss7.mtp.Mtp3TransferPrimitiveFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

@Controller
@RequestMapping("management/m3ua")
@RequiredArgsConstructor
@Slf4j
public class M3uaManagementController {
    private final M3uaServiceImpl m3uaService;

    @PostMapping(value = "/sendMessage", produces = "application/json")
    @ResponseBody
    public String sendMessage(@RequestBody M3uaMessageDto m3uaMessageDto) {
        Mtp3TransferPrimitiveFactory factory = m3uaService.getM3uaManagement().getMtp3TransferPrimitiveFactory();
        Mtp3TransferPrimitive mtp3TransferPrimitive = factory.createMtp3TransferPrimitive(
                m3uaMessageDto.getSi(),
                m3uaMessageDto.getNi(),
                m3uaMessageDto.getMp(),
                m3uaMessageDto.getOpc(),
                m3uaMessageDto.getDpc(),
                m3uaMessageDto.getSls(),
                m3uaMessageDto.getData().getBytes()
        );
        try {
            m3uaService.getM3uaManagement().sendMessage(mtp3TransferPrimitive);
        } catch (IOException e) {
            //TODO
            log.warn(e.getMessage(), e);
            return e.getMessage();
        }
        return "message sending";
    }
}
