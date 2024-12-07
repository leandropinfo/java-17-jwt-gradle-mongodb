package com.leandropinfo.java_17_jwt_gradle_mongodb.controllers.client;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/client")
public class ClientController {

    @GetMapping("/")
    public String home() {
        return "User OK!";
    }

}
