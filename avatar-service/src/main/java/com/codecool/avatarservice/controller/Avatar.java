package com.codecool.avatarservice.controller;

import com.codecool.avatarservice.model.User;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class Avatar {

    @RequestMapping(
            value = "/avatar/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public String getAvatar(@PathVariable int id) {

        User user = User.getUser(id);
        return String.format("{\"id\": %s, \"img\": \"%s\"}", id, user.getUri());
    }
}
