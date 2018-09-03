package com.codecool.comicsservice.controller;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Random;

@RestController
@RequestMapping("/api")
public class Comics {

    private String url = "https://xkcd.com/%s/info.0.json";
    private RestTemplate restTemplate = new RestTemplate();

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

            Random r = new Random();
            int random = r.nextInt(1928) + 1;
            String fromattedUrl = String.format(url, random);
            ResponseEntity<String> result = restTemplate.exchange(fromattedUrl, HttpMethod.GET, entity, String.class);

            return result.getBody();
        } catch (HttpClientErrorException e) {
            e.printStackTrace();
            return "{\"error\": \"ClientError\"}";
        }
    }
}
