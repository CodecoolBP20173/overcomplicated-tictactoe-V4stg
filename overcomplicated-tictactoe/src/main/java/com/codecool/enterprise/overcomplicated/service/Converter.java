package com.codecool.enterprise.overcomplicated.service;

public class Converter {
    public static String getString(char[][] gameState) {
        StringBuilder sb = new StringBuilder();
        for (char[] row : gameState) {
            for (char cell : row) {
                sb.append(cell);
            }
        }
        return sb.toString();
    }
}
