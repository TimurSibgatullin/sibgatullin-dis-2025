package ru.itis.dis403.lab03;


import java.util.List;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameState {
    private boolean gameOver = false;
    private String message = "";
    private final List<Row> table = List.of(new Row(), new Row(), new Row());

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Row> getTable() {
        return table;
    }

    public boolean isEmpty(int row, int col) {
        String val = getCell(row, col);
        return val.equals("пусто.png");
    }

    public String getCell(int row, int col) {
        Row r = table.get(row);
        return switch (col) {
            case 0 -> r.getF();
            case 1 -> r.getS();
            case 2 -> r.getT();
            default -> "пусто.png";
        };
    }

    public void setCell(int row, int col, String value) {
        Row r = table.get(row);
        switch (col) {
            case 0 -> r.setF(value);
            case 1 -> r.setS(value);
            case 2 -> r.setT(value);
        }
    }

    public boolean hasEmpty() {
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                if (isEmpty(i, j)) return true;
        return false;
    }

    public void aiMove() {
        if (gameOver) return;
        List<int[]> emptyCells = new ArrayList<>();
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                if (isEmpty(i, j)) emptyCells.add(new int[]{i, j});
        if (emptyCells.isEmpty()) return;

        int[] cell = emptyCells.get(new Random().nextInt(emptyCells.size()));
        setCell(cell[0], cell[1], "нолик.png");
    }

    public boolean checkWin(String symbol) {
        for (int i = 0; i < 3; i++) {
            if (getCell(i, 0).equals(symbol) && getCell(i, 1).equals(symbol) && getCell(i, 2).equals(symbol))
                return true;
            if (getCell(0, i).equals(symbol) && getCell(1, i).equals(symbol) && getCell(2, i).equals(symbol))
                return true;
        }
        return (getCell(0, 0).equals(symbol) && getCell(1, 1).equals(symbol) && getCell(2, 2).equals(symbol))
                || (getCell(0, 2).equals(symbol) && getCell(1, 1).equals(symbol) && getCell(2, 0).equals(symbol));
    }
}