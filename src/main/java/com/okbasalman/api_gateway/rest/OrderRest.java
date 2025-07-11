package com.okbasalman.api_gateway.rest;



import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/api/orders")
public class OrderRest {
    @GetMapping
    public String Greeting(){
        return "Hello Hasan!";
    }
}
