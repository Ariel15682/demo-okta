package com.example.demookta.controllers;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@Controller // controlador mvc
public class AppController {

    @GetMapping("/")
    public String index(){
        return "index";
    }

    @GetMapping("/page1")
    public String page1(Model model){
        model.addAttribute("message", "Securized Page!!");
        return "page1";
    }

    @GetMapping("/page2")
    public String page2(Model model,
            @RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient client,
            @AuthenticationPrincipal OAuth2User user
    ){
        model.addAttribute("clientName", client.getClientRegistration().getClientName());
        model.addAttribute("userName", user.getName());
        model.addAttribute("userAttributes", user.getAttributes());
        return "page2";
    }

    @RestController
    static class ExampleRestController {

        @GetMapping("/hello")
        String sayHello(@AuthenticationPrincipal Jwt jwt) {
            return String.format("Hello, %s!", jwt.getSubject());
        }
    }
}
