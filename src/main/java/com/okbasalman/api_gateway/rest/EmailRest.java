package com.okbasalman.api_gateway.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.okbasalman.api_gateway.dto.email.CheckEmailDto;
import com.okbasalman.api_gateway.dto.email.SendEmailDto;
import com.okbasalman.api_gateway.grpc.EmailGrpc;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/email")
public class EmailRest {
    private EmailGrpc emailGrpc;

    public EmailRest(EmailGrpc emailGrpc){
        this.emailGrpc=emailGrpc;
    }

    @PostMapping("/sendCode")
    public ResponseEntity<?> sendCode(@RequestBody SendEmailDto request){
        boolean res= emailGrpc.sendEmail(request);
        return ResponseEntity.status(res?200:400).body(res);
    }

    @PostMapping("/checkCode")
    public ResponseEntity<?> checkCode(@RequestBody CheckEmailDto request){
        System.out.println(request.getCode()+"  "+request.getEmail()+ "  sfdas");
        boolean res= emailGrpc.checkEmail(request);
        return ResponseEntity.status(res?200:400).body(res);
    }
}
