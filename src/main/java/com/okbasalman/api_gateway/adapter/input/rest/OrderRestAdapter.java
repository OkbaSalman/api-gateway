package com.okbasalman.api_gateway.adapter.input.rest;



import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/api/orders")
public class OrderRestAdapter {
    @GetMapping
    public String Greeting(){
        return "Hello Hasan!";
    }
}
