package com.codecool.ticatactoeaiservice.controller;

import com.codecool.ticatactoeaiservice.service.TTTGame;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import java.io.IOException;
import java.util.HashMap;

@RestController
@RequestMapping("/api")
public class TTTAI {

    private String url = "http://tttapi.herokuapp.com/api/v1/%s/%s";
    private JSONObject obj;

    @RequestMapping(
            value = "/player/{board}/{move}/{playerIcon}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public String playerMove(@PathVariable char[] board,
                                 @PathVariable int move,
                                 @PathVariable char playerIcon) {
        obj = new JSONObject();

        // is player move valid
        if (board[move] == '-') {
            try {
                // player moves
                board[move] = playerIcon;

                // is game WON?
                if (TTTGame.isGameWon(board, playerIcon)) {
                    obj.put("board", TTTGame.getString(board));
                    obj.put("outcome", String.format("%s WON", playerIcon));
                    return obj.toString();
                }
                // is game over?
                if (TTTGame.isGameOver(board)) {
                    obj.put("board", TTTGame.getString(board));
                    obj.put("outcome", "DRAW");
                    return obj.toString();
                }

                // format URL and get response from AI API
                String formattedUrl = String.format(url, TTTGame.getString(board), "X");
                HttpResponse<String> result = Unirest.get(formattedUrl).asString();

                // get data from AI API response
                HashMap resultMap = new ObjectMapper().readValue(result.getBody(), HashMap.class);
                board = ((String) resultMap.get("game")).toCharArray();
                char AIIcon = ((String) resultMap.get("player")).toCharArray()[0];
                int recommendation = (int) resultMap.get("recommendation");

                // AI moves
                board[recommendation] = AIIcon;

                // is game WON?
                if (TTTGame.isGameWon(board, AIIcon)) {
                    obj.put("board", TTTGame.getString(board));
                    obj.put("outcome", String.format("%s WON", AIIcon));
                    return obj.toString();
                }
                // is game over?
                if (TTTGame.isGameOver(board)) {
                    obj.put("board", TTTGame.getString(board));
                    obj.put("outcome", "DRAW");
                    return obj.toString();
                }

                // build and send response to overcomplicated tictactoe
                obj.put("board", TTTGame.getString(board));
                return obj.toString();

            } catch (HttpClientErrorException | IOException | UnirestException e) {
                e.printStackTrace();
                obj.put("error", "ClientError");
                return obj.toString();
            }
        } else {
            obj.put("error", "ClientError");
            return obj.toString();
        }
    }
}
