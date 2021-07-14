package com.unifun.sigproxy.controller.management;

import com.unifun.sigproxy.service.impl.M3uaServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.restcomm.protocols.ss7.mtp.Mtp3TransferPrimitiveFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public String sendMessage(@RequestParam String stackName,
                              @RequestParam int si,
                              @RequestParam int ni,
                              @RequestParam int mp,
                              @RequestParam int opc,
                              @RequestParam int dpc,
                              @RequestParam int sls,
                              @RequestParam String data) {
        Mtp3TransferPrimitiveFactory factory = m3uaService.getManagement(stackName).getMtp3TransferPrimitiveFactory();
        var mtp3TransferPrimitive = factory.createMtp3TransferPrimitive(si, ni, mp, opc, dpc, sls, data.getBytes());
        try {
            m3uaService.getManagement(stackName).sendMessage(mtp3TransferPrimitive);
        } catch (IOException e) {
            //TODO
            log.warn(e.getMessage(), e);
            return e.getMessage();
        }
        return "message sending";
    }
}
