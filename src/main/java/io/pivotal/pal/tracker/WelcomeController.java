package io.pivotal.pal.tracker;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WelcomeController {

    String WelcomeMsg = "";

    @GetMapping("/")
    public String sayHello(){
        return WelcomeMsg;
    }


    public  WelcomeController(@Value("${Welcome.message}") String message){
        WelcomeMsg =  message;

    }

}
