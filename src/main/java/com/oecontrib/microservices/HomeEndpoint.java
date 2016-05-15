package com.oecontrib.microservices;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeEndpoint {
    @RequestMapping("/")
    @ResponseBody
    String home() {
        return "Microservices that expose OpenEye Toolkits functionality as REST services.";
    }

}
