package edu.tmu.group67.scrum_development.auth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
public class UserController {
    @GetMapping("/hello")
    public String hello() {
        return "Hello, Scrum Project!";
    }
}
