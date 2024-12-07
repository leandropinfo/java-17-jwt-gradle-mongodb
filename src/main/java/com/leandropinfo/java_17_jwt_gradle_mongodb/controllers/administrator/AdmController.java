package com.leandropinfo.java_17_jwt_gradle_mongodb.controllers.administrator;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/adm")
public class AdmController {

    @GetMapping("/")
    public String home() {
        return "Adm OK!";
    }

}
