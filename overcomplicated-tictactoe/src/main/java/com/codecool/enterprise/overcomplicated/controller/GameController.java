package com.codecool.enterprise.overcomplicated.controller;

import com.codecool.enterprise.overcomplicated.model.Player;
import com.codecool.enterprise.overcomplicated.model.TictactoeGame;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Controller
@SessionAttributes({"player", "game"})
public class GameController {

    private JacksonJsonParser jacksonJsonParser = new JacksonJsonParser();
    private RestTemplate restTemplate = new RestTemplate();

    @ModelAttribute("player")
    public Player getPlayer() {
        return new Player();
    }

    @ModelAttribute("game")
    public TictactoeGame getGame() {
        return new TictactoeGame();
    }

    @ModelAttribute("avatar_uri")
    public String getAvatarUri() {
        String uri = String.format("http://localhost:60003/api/avatar/%s", getPlayer().getUserId());
        String avatarJSON = restTemplate.getForObject(uri, String.class);

        Map<String, Object> avatarMap = jacksonJsonParser.parseMap(avatarJSON);

        return avatarMap.get("img").toString();
    }

    @GetMapping(value = "/")
    public String welcomeView(@ModelAttribute Player player) {
        return "welcome";
    }

    @PostMapping(value="/changeplayerusername")
    public String changPlayerUserName(@ModelAttribute Player player) {
        return "redirect:/game";
    }

    @GetMapping(value = "/game")
    public String gameView(@ModelAttribute("player") Player player, Model model) {

        String funfactJSON = restTemplate.getForObject("http://localhost:60001/api/random", String.class);
        String comicJSON = restTemplate.getForObject("http://localhost:60002/api/random", String.class);

        Map<String, Object> funfactMap = jacksonJsonParser.parseMap(funfactJSON);
        String funfact = funfactMap.get("value").toString();

        Map<String, Object> comicMap = jacksonJsonParser.parseMap(comicJSON);
        String comicUrl = comicMap.get("img").toString();

        model.addAttribute("comic_uri", comicUrl);
        model.addAttribute("funfact", funfact);
        return "game";
    }

    @GetMapping(value = "/game-move")
    public String gameMove(@ModelAttribute("player") Player player, @ModelAttribute("move") int move) {
        System.out.println("Player moved " + move);
        return "redirect:/game";
    }
}
