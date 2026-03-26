//tester class -controller hangles HTTP requests and responses, and it is responsible for 
// processing incoming requests, handling user input, and returning appropriate responses. 
// It acts as an intermediary between the view (user interface) and the model (data). The 
// controller receives user input from the view, processes it (often by calling methods 
// on the model), and then returns a response to the view to be displayed to the user. In 
// this case, the HelloController class is a simple controller that handles GET requests 
// to the "/hello" endpoint and returns a greeting message.
package edu.tmu.group67.scrum_development.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello, Scrum Project!";
    }
}