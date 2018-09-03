package com.codecool.enterprise.funfact.controller;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Random;

@RestController
@RequestMapping("/api")
public class Funfact {

    private String url = "https://api.chucknorris.io/jokes/random";
    private RestTemplate restTemplate = new RestTemplate();
    private String[] list = {
            "Chuck Norris kicked down this microservice...",
            "Chuck Norris didn't approve this joke.",
            "All Chuck Norris joke is based on true stories.",
            "Chuck Norris wrote this JSON."
    };

    @RequestMapping(
            value = "/random",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public String getRandom() {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            headers.add("user-agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:61.0) Gecko/20100101 Firefox/61.0");
            HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);

            ResponseEntity<String> result = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            return result.getBody();
        } catch (HttpClientErrorException e) {
            Random r = new Random();
            return list[r.nextInt(list.length)];
        }
    }
}
