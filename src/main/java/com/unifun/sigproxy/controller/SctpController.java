package com.unifun.sigproxy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("sctp")
public class SctpController {

    @GetMapping(value = "/get", produces = "application/json")
    public @ResponseBody
    String getInfo() {
        return "{}";
    }
}