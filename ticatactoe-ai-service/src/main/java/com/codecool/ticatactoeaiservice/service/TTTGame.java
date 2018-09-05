package com.codecool.ticatactoeaiservice.service;

import org.springframework.web.bind.annotation.PathVariable;

public class TTTGame {
    public static boolean isGameOver(char[] gameState) {
        for (char cell : gameState) {
            if (cell == '-') {
                return false;
            }
        }
        return true;
    }

    public static boolean isGameWon(char[] game, char player) {
        return  game[0] == player && game[1] == player && game[2] == player ||
                game[3] == player && game[4] == player && game[5] == player ||
                game[6] == player && game[7] == player && game[8] == player ||
                game[0] == player && game[3] == player && game[6] == player ||
                game[1] == player && game[4] == player && game[7] == player ||
                game[2] == player && game[5] == player && game[8] == player ||
                game[0] == player && game[4] == player && game[8] == player ||
                game[2] == player && game[4] == player && game[6] == player;
    }

    public static String getString(@PathVariable char[] gamestate) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < gamestate.length; i++) {
            sb.append(gamestate[i]);
        }
        return sb.toString();
    }
}
