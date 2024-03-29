package com.codecool.enterprise.overcomplicated.controller;

import com.codecool.enterprise.overcomplicated.model.Player;
import com.codecool.enterprise.overcomplicated.model.TictactoeGame;
import com.codecool.enterprise.overcomplicated.service.Converter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
@SessionAttributes({"player", "game"})
public class GameController {

    private JacksonJsonParser jacksonJsonParser = new JacksonJsonParser();
    private RestTemplate restTemplate = new RestTemplate();
    private char[][] board = {{'-', '-', '-'}, {'-', '-', '-'}, {'-', '-', '-'}};
    private String outcome;

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
        try {
            String avatarJSON = restTemplate.getForObject(uri, String.class);

            Map<String, Object> avatarMap = jacksonJsonParser.parseMap(avatarJSON);

            return avatarMap.get("img").toString();

        } catch (ResourceAccessException e) {
//            e.printStackTrace();
            return "http://asianinteriorservices.com/wp-content/uploads/2018/04/noImg.png";
        }
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

        String funfact;
        try {
            String funfactJSON = restTemplate.getForObject("http://localhost:60001/api/random", String.class);

            Map<String, Object> funfactMap = jacksonJsonParser.parseMap(funfactJSON);
            funfact = funfactMap.get("value").toString();

        } catch (ResourceAccessException e) {
//            e.printStackTrace();
            funfact = "Chuck is currently not accessible. Please leave a message!";
        }

        String comicUrl;
        try {
            String comicJSON = restTemplate.getForObject("http://localhost:60002/api/random", String.class);

            Map<String, Object> comicMap = jacksonJsonParser.parseMap(comicJSON);
            comicUrl = comicMap.get("img").toString();
        } catch (ResourceAccessException e) {
//            e.printStackTrace();
            comicUrl = "http://asianinteriorservices.com/wp-content/uploads/2018/04/noImg.png";
        }

        model.addAttribute("funfact", funfact);
        model.addAttribute("comic_uri", comicUrl);

        model.addAttribute("board", board);
        model.addAttribute("outcome", outcome);
        return "game";
    }

    @GetMapping(value = "/game-move")
    public String gameMove(@ModelAttribute("player") Player player, @ModelAttribute("move") int move) {
        String playerIcon = "O";

        String url = String.format("http://localhost:60004/api/player/%s/%s/%s",
                Converter.getString(board), move, playerIcon);

        try {
            String response = restTemplate.getForObject(url, String.class);
            HashMap resultMap = new ObjectMapper().readValue(response, HashMap.class);

            String gameStr = (String) resultMap.get("board");
            outcome = (String) resultMap.get("outcome");

            char[] game = gameStr.toCharArray();

            for (int i = 0; i < 3; i++) {
                System.arraycopy(game, i * 3, board[i], 0, 3);
            }

        } catch (ResourceAccessException | IOException e) {
            e.printStackTrace();
        }

        return "redirect:/game";
    }

    @GetMapping(value = "/new-game")
    public String newGame() {

        board = new char[][]{{'-', '-', '-'}, {'-', '-', '-'}, {'-', '-', '-'}};
        outcome = null;

        return "redirect:/game";
    }
}
